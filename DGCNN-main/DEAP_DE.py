from scipy.io import loadmat
import numpy as np
import torch
import os
from scipy.signal import stft
from sklearn.preprocessing import MinMaxScaler
import scipy.io as scio
from scipy import signal
import math

data_dir = "./DEAP/data_preprocessed_matlab/"
save_dir = "./DEAP/DEAP_DE/"
fs = 128


def compute_DE(signal):
    variance = np.var(signal, ddof=1)  # 求得方差
    return math.log(2 * math.pi * math.e * variance) / 2  # 微分熵求取公式

def data_calibrate(data):
    """
    数据标定
    :param data: 原始数据
    :return: 标定后的数据
    """
    baseline_time = 3  # 基线数据的时间长度
    # 将 3s 基线时间与 60s 数据分开
    baseline_data, normal_data = np.split(data, [baseline_time * fs], axis=-1)
    # 将基线数据重复 20 次，补成 60s
    baseline_data = np.concatenate([baseline_data] * 20, axis=-1)
    # 用 60s 数据减去基线数据，去除噪声
    baseline_data = normal_data - baseline_data
    return baseline_data

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

    divided_data = []
    for i in range(0, num * step, step):
        segment = data[:, :, i * fs: (i + window_size) * fs]
        divided_data.append(segment)
    divided_data = np.vstack(divided_data)
    divided_label = np.vstack([label] * num)

    return divided_data, divided_label


def feature_extract(data):
    datasets_x, datasets_y = [], []
    fStart = [4, 8, 12, 30]
    fEnd = [8, 12, 30, 64]

    for i in range(40):
        dataset_x = []
        data_piece = data[i, :32, 128*3:]
        scaler = MinMaxScaler()
        data_piece = scaler.fit_transform(data_piece)  # 归一化

        for band_index, band in enumerate(fStart):
            b, a = signal.butter(4, [fStart[band_index] / fs, fEnd[band_index] / fs], 'bandpass')  # 配置滤波器 4 表示滤波器的阶数
            filtedData = signal.filtfilt(b, a, data_piece)  # data为要过滤的信号
            filtedData_de = []

            for lead in range(32):
                filtedData_split = []
                # 计算微分熵
                for de_index in range(0, filtedData.shape[1] - fs, fs):
                    filtedData_split.append(compute_DE(filtedData[lead, de_index: de_index + fs]))
                filtedData_de.append(filtedData_split)
            filtedData_de = np.array(filtedData_de)
            dataset_x.append(filtedData_de)
        dataset_x = np.asarray(dataset_x)
        dataset_x = dataset_x.transpose(2, 1, 0)
        if i == 0:
            datasets_x = dataset_x
        else:
            datasets_tmp = datasets_x
            datasets_x = np.concatenate((datasets_tmp,dataset_x))

    return np.asarray(datasets_x)


def set_label(labels):
    """
    打标签
    :param labels: 标签
    :return: 处理后的标签
    """
    # three_label = []
    # for label in labels:
    #     if 0 <= label < 4:
    #         three_label.append(0)
    #     elif 4 <= label < 7:
    #         three_label.append(1)
    #     else:
    #         three_label.append(2)
    # return torch.tensor(three_label, dtype=torch.long)
    return torch.tensor(np.where(labels < 5, 0, 1), dtype=torch.long)



def load_data(filename_data):
    datasets_y = []
    data_all = loadmat(data_dir + filename_data)
    data = data_all['data']
    labels = data_all['labels']
    # # 数据标定
    # data = data_calibrate(data[:, :32])
    # # 数据分割
    # data, labels = data_divide(data, labels)
    # 打标签
    labels = set_label(labels[:,0])
    # 特征提取
    data = feature_extract(data)

    datasets_x = np.asarray(data)
    labels = np.asarray(labels)
    for label in labels:
        for i in range(59):
            datasets_y.append(label)
    datasets_y = np.asarray(datasets_y)
    return  datasets_x, datasets_y


if __name__ == "__main__":

    for filename_data in os.listdir(data_dir):
        datasets_x, datasets_y = load_data(filename_data)
        label = datasets_y.astype(np.double)
        data = datasets_x.transpose(1, 0, 2)
        label = label.reshape(-1, 1)
        mdic = {"DE": data, "labelAll": label, "label": "experiment"}
        scio.savemat(save_dir + filename_data, mdic)
        print(save_dir + filename_data + 'processed')
