import copy
import os
import sys

import matplotlib.pyplot as plt
import numpy as np
import torch
import torch.optim as optim
from scipy import io as scio
from scipy.stats import zscore
from sklearn.metrics import confusion_matrix
from torch import nn
from torch.utils.data import DataLoader

from model import DGCNN
# from tqdm import tqdm
from utils import eegDataset

# os.environ["CUDA_VISIBLE_DEVICES"] = "0"
os.environ['TORCH_HOME'] = './'  # setting the environment variable

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')


def load_DE_SEED(load_path):
    filePath = load_path
    datasets = scio.loadmat(filePath)
    DE = datasets['DE']
    dataAll = np.transpose(DE, [1, 0, 2])
    labelAll = datasets['labelAll'].flatten()


    return dataAll, labelAll


def load_dataloader(data_train, data_test, label_train, label_test):
    batch_size = 64
    train_iter = DataLoader(dataset=eegDataset(data_train, label_train),
                            batch_size=batch_size,
                            shuffle=True,
                            num_workers=1)

    test_iter = DataLoader(dataset=eegDataset(data_test, label_test),
                           batch_size=batch_size,
                           shuffle=True,
                           num_workers=1)

    return train_iter, test_iter


def train(train_iter, test_iter, model, criterion, optimizer, num_epochs, sub_name):
    # Train

    print('began training on', device, '...')

    acc_test_best = 0.0
    pred_best = []
    label_best = []
    n = 0
    for ep in range(num_epochs):
        model.train()
        n += 1
        batch_id = 1
        correct, total, total_loss = 0, 0, 0.
        for images, labels in train_iter:
            images = images.float().to(device)
            labels = labels.to(device)

            # Compute loss & accuracy
            output = model(images)

            loss = criterion(output, labels.long())  # 标签从0开始！！！！！！！！！！！！！！必须要保证label是0开始的连续整数，因为label是一种索引

            # correct = 0
            # a = len(labels)
            pred = output.argmax(dim=1)

            correct += (pred == labels).sum().item()
            total += len(labels)
            accuracy = correct / total
            total_loss += loss
            loss.backward()
            # scheduler.step()
            optimizer.step()
            # print(optimizer.state_dict)
            optimizer.zero_grad()

            print('Epoch {}, batch {}, loss: {}, accuracy: {}'.format(ep + 1,
                                                                      batch_id,
                                                                      total_loss / batch_id,
                                                                      accuracy))

            batch_id += 1

        print('Total loss for epoch {}: {}'.format(ep + 1, total_loss))

        acc_test, m_pred, m_label = evaluate(test_iter, model)

        if acc_test >= acc_test_best:
            n = 0
            acc_test_best = acc_test
            model_best = model
            pred_best = m_pred
            label_best = m_label
        # 学习率逐渐下降，容易进入局部最优，当连续10个epoch没有跳出，且有所下降，强制跳出
        if n >= num_epochs // 10 and acc_test < acc_test_best - 0.1:
            print('#########################reload#########################')
            n = 0
            model = model_best
        # find best test acc model in all epoch(not last epoch)

        print('>>> best test Accuracy: {}'.format(acc_test_best))

    return acc_test_best, pred_best, label_best


def evaluate(test_iter, model):
    # Eval
    print('began test on', device, '...')
    model.eval()
    correct, total = 0, 0
    m_pred = []
    m_label = []
    for images, labels in test_iter:
        # Add channels = 1
        images = images.float().to(device)

        # Categogrical encoding
        labels = labels.to(device)

        output = model(images)

        pred = output.argmax(dim=1)
        correct += (pred == labels).sum().item()
        total += len(labels)
        m_pred.extend(pred.cpu().numpy())
        m_label.extend(labels.cpu().numpy())
    print('test Accuracy: {}'.format(correct / total))
    return correct / total, m_pred, m_label


def draw_confusion_matrix(label_true, label_pred, label_name, title="Confusion Matrix", pdf_save_path=None, dpi=100):
    """

    @param label_true: 真实标签，比如[0,1,2,7,4,5,...]
    @param label_pred: 预测标签，比如[0,5,4,2,1,4,...]
    @param label_name: 标签名字，比如['cat','dog','flower',...]
    @param title: 图标题
    @param pdf_save_path: 是否保存，是则为保存路径pdf_save_path=xxx.png | xxx.pdf | ...等其他plt.savefig支持的保存格式
    @param dpi: 保存到文件的分辨率，论文一般要求至少300dpi
    @return:

    """
    cm = confusion_matrix(y_true=label_true, y_pred=label_pred, normalize='true')
    plt.figure()
    plt.imshow(cm, cmap='Blues')
    plt.title(title)
    plt.xlabel("Predict label")
    plt.ylabel("Truth label")
    plt.yticks(range(label_name.__len__()), label_name)
    plt.xticks(range(label_name.__len__()), label_name, rotation=45)

    plt.tight_layout()

    plt.colorbar()

    for i in range(label_name.__len__()):
        for j in range(label_name.__len__()):
            color = (1, 1, 1) if i == j else (0, 0, 0)  # 对角线字体白色，其他黑色
            value = float(format('%.2f' % cm[j, i]))
            plt.text(i, j, value, verticalalignment='center', horizontalalignment='center', color=color)

    if not pdf_save_path is None:
        plt.savefig(pdf_save_path, bbox_inches='tight', dpi=dpi)


def main_LOCV():
    dir = './DEAP/DEAP_DE/'  # 04-0.9916， 0.86
    # os.chdir(dir) # 可能在寻找子文件的时候路径进了data
    file_list = os.listdir(dir)
    sub_num = len(file_list)

    xdim = [128, 32, 4]  # batch_size * channel_num * freq_num
    k_adj = 40
    num_out = 64
    num_epochs = 1
    acc_mean = 0
    acc_all = []
    label_best_all = []
    pred_best_all = []
    for sub_i in range(sub_num):

        load_path = dir + file_list[sub_i]  # ../表示上一级目录
        data_test, label_test = load_DE_SEED(load_path)  # data （‘采样点’，通道，4频带， 1080 59 4）   lable  对应‘采样点’的标签 1080

        # if device.type == 'cuda':
        #         print('empty cuda cache...')
        #         torch.cuda.empty_cache()

        data_test = zscore(data_test)

        model = DGCNN(xdim, k_adj, num_out, nclass=2).to(device)

        criterion = nn.CrossEntropyLoss().to(device)  # 使用这个函数需要注意：标签是整数，不要onehot，已经包含了softmax
        optimizer = optim.Adam(model.parameters(),
                               lr=0.001,
                               weight_decay=0.0001)
        # momentum=0.9)
        # scheduler = lr_scheduler.CosineAnnealingLR(optimizer, T_max=30, eta_min=1e-6)

        train_list = copy.deepcopy(file_list)
        train_list.remove(file_list[sub_i])
        train_num = len(train_list)

        data_train = []
        label_train = []

        for train_i in range(train_num):

            train_path = dir + train_list[train_i]
            data, label = load_DE_SEED(train_path)

            data = zscore(data)

            if train_i == 0:
                data_train = data
                label_train = label
            else:
                data_train = np.concatenate((data_train, data), axis=0)
                label_train = np.concatenate((label_train, label), axis=0)

        # data_train = zscore(data_train)

        ## 训练的数据要求导，必须使用torch.tensor包装
        data_train = torch.tensor(data_train)
        label_train = torch.tensor(label_train)

        train_iter, test_iter = load_dataloader(data_train, data_test, label_train, label_test)
        acc_test_best, m_pred, m_label = train(train_iter, test_iter, model, criterion, optimizer, num_epochs,
                                               file_list[sub_i])
        acc_mean = acc_mean + acc_test_best / sub_num
        acc_all.append(acc_test_best)
        label_best_all.extend(m_label)
        pred_best_all.extend(m_pred)

    print('save...')
    scio.savemat('./result/acc_all/acc_de_DEAP_LOCV.mat', {'acc_all': acc_all, \
                                                           'sub_list': np.array(file_list, dtype=np.object_)})

    print('>>> LOSV test acc: ', acc_all)
    print('>>> LOSV test mean acc: ', acc_mean)
    print('>>> LOSV test std acc: ', np.std(np.array(acc_all)))
    draw_confusion_matrix(label_true=label_best_all,
                          label_pred=pred_best_all,
                          label_name=["positive", "negative"],
                          title=f'Confusion Matrix on DEAP',
                          pdf_save_path='./result/confusion_matrix/DEAP.svg',
                          dpi=300)


if __name__ == '__main__':
    sys.exit(main_LOCV())
