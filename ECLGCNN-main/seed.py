import numpy as np
import torch
import os
from scipy.signal import stft
from torch_geometric.data import Data
from torch_geometric.data import Dataset
from torch_geometric.data import Batch
import torch.nn.functional as F
import scipy.io as sio
import mne

fs = 200  # 采样频率

labels = [2, 1, 0, 0, 1, 2, 0, 1, 2, 2, 1, 0, 1, 2, 0]




def data_divide(data, label):
    """
    数据分割
    :param data: 标定后的数据
    :param label: 标签
    :return: 分割后的数据和标签
    """
    window_size = 6  # 窗口大小
    step = 3  # 窗口滑动的步长
    num = (60 - window_size) // step + 1  # 分割成的段数
    label = np.asarray(label)
    divided_data = []
    divided_label = []
    for i in range(0, num * step, step):
        segment = data[:, :, i * fs: (i + window_size) * fs]
        divided_data.append(segment)
        divided_label.extend(label)
    divided_data = np.vstack(divided_data)

    return divided_data, divided_label


def feature_extract(data_array):
    """
    提取特征
    :param data_array: 标定、分割过后的数据，每位受试者有 760 条数据
    :return: 特征立方体
    """
    f, t, zxx = stft(data_array, fs=128, window='hann', nperseg=128, noverlap=0, nfft=256, scaling='psd')

    power = np.power(np.abs(zxx), 2)

    fStart = [1, 4, 8, 14, 31]  # 起始频率
    fEnd = [3, 7, 13, 30, 50]  # 终止频率

    de_time = []
    for i in range(1, 7):
        bands = []
        for j in range(len(fStart)):
            index1 = np.where(f == fStart[j])[0][0]
            index2 = np.where(f == fEnd[j])[0][0]
            psd = np.sum(power[:, :, index1:index2, i], axis=2) / (fEnd[j] - fStart[j] + 1)
            de = np.log2(psd)
            bands.append(de)
        de_bands = np.stack(bands, axis=-1)
        de_time.append(de_bands)

    de_features = np.stack(de_time, axis=1)
    return de_features


def gaussian(dist, theta):
    """
    计算高斯核函数
    :param dist: 欧氏距离
    :return: 高斯核函数值
    """
    return torch.exp(- (dist ** 2) / (theta ** 2 * 2))


def get_edge(x, k=5, theta=1, tao=5):
    """
    计算图的边集和边权重
    :param x: 二维张量，每行是一个结点
    :param k: kNN 的 k
    :param theta: 高斯核函数中的参数
    :param tao: 距离有效的阈值
    :return: 边顶点和边权重
    """
    node_num = x.shape[0]
    edge_index = []
    weights = []

    for i in range(node_num):
        # 计算结点 i 与其他结点的距离
        dists = torch.empty(node_num)
        for j in range(node_num):
            dist = F.pairwise_distance(x[i], x[j], p=2, eps=0)
            dists[j] = dist

        # 根据 kNN 策略，选择距离前 k 小的结点
        # 由于结点到自己的距离为 0，排序后第一个元素为 i 自己，因此选择 1-k 的元素
        index = torch.argsort(dists)
        selected_index = index[1:k + 1]
        for j in selected_index:
            if ([i, j] not in edge_index) and (dists[j] <= tao):
                edge_index.append([i, j])
                edge_index.append([j, i])
                # 使用高斯核函数计算边权重
                weight = gaussian(dists[j], theta)
                weights.append(weight)
                weights.append(weight)

    return edge_index, weights


def to_graph(data):
    """
    将数据转化为图结构
    :param data: 数据
    :return: Data 类型的图数据
    """
    graph_list = []
    for i in range(data.shape[0]):
        video_graph = []
        for time_data in data[i]:
            edge_index, edge_attr = get_edge(torch.tensor(time_data))
            graph = Data(x=torch.tensor(time_data, dtype=torch.float32),
                         edge_index=torch.tensor(edge_index, dtype=torch.int64).t().contiguous(),
                         edge_attr=torch.tensor(edge_attr, dtype=torch.float32))
            video_graph.append(graph)
        batch = Batch.from_data_list(video_graph)
        graph_list.append(batch)
    return graph_list


def data_processing(data, labels):
    """
    数据处理
    :param data: 数据
    :param labels: 标签
    :return: 处理后的数据和标签
    """
    # 数据分割
    data,labels = data_divide(data, labels)
    # 特征提取
    data = feature_extract(data)
    # 转换为图结构
    graph_list = to_graph(data)

    return graph_list, labels


def read_one_file(file_path):
    """
    input:单个.mat文件路径
    output:raw格式数据
    """
    data = sio.loadmat(file_path)
    # 获取keys并转化为list，获取数据所在key
    keys = list(data.keys())[3:]
    # print(keys)
    # 获取数据
    raw_list = []
    for i in range(len(keys)):
        # 获取数据
        stamp = data[keys[i]]
        # 添加到raw_list
        raw_list.append(stamp[:, 0:12000])
    return raw_list


class SeedDateset(Dataset):
    def __init__(self, root):
        super().__init__(root)

    @property
    def raw_file_names(self):
        return os.listdir(self.raw_dir)

    @property
    def processed_file_names(self):
        file_names = []
        for file_name in self.raw_file_names:
            root, ext = os.path.splitext(file_name)
            file_names.append(root + '.pt')
        return file_names

    def download(self):
        pass

    def process(self):
        # 读取文件夹下所有.mat文件
        print("read_all_files start...")
        for i in range(len(self.raw_paths)):
            with open(self.raw_paths[i], 'rb') as f:
                raw_list = read_one_file(f)
                raw_list = np.asarray(raw_list)
                processed_data = data_processing(raw_list, labels)
                torch.save(processed_data, self.processed_paths[i])
        print("read ended...")

    def get(self, index):
        data = torch.load(self.processed_paths[index])
        return data

    def len(self):
        return len(self.raw_file_names)


if __name__ == '__main__':
    SeedDateset = SeedDateset('./seed')

    subject_data, labels = SeedDateset[0]
    print(len(subject_data))

    for graph in subject_data:
        print(graph)
        print(graph.edge_index)
        for data in graph.to_data_list():
            print(data.edge_index)
        break

    batch = Batch.from_data_list(subject_data[0:20])
    print(batch)
    print(batch.edge_index)
    print(batch.num_graphs)
