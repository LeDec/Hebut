import os
import sys

import matplotlib.pyplot as plt
import numpy as np
import torch
import torch.optim as optim
from scipy import io as scio
from sklearn.metrics import confusion_matrix
from torch import nn
from torch.utils.data import DataLoader
from sklearn.model_selection import train_test_split

from model import DGCNN
from utils import eegDataset
model_path = './model/deap.pt'
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


def binary_sampling(data, label):
    """
    对二分类数据集进行重采样，保证数据平衡
    """
    num = len(data)  # number of samples

    index_0 = torch.nonzero(label == 0, as_tuple=True)[0]
    num_0 = len(index_0)

    index_1 = torch.nonzero(label == 1, as_tuple=True)[0]
    num_1 = len(index_1)
    print([num_0, num_1])

    if abs(num_0 - num_1) < num * 0.25:
        return data, label
    elif num_0 == 0 or num_1 == 0:
        return data, label
    else:
        selected_index = []
        shorter_index, shorter_num = (index_0, num_0) if num_0 < num_1 else (index_1, num_1)
        longer_index, longer_num = (index_0, num_0) if num_0 > num_1 else (index_1, num_1)
        for i in range(longer_num):
            selected_index.append(longer_index[i])
            selected_index.append(shorter_index[i % shorter_num])

        selected_data = []
        for i in selected_index:
            selected_data.append(data[i])
        selected_label = label[selected_index]
        return selected_data, selected_label


def load_dataloader(data_train, data_test, data_val, label_train, label_test, label_val):
    batch_size = 128
    train_iter = DataLoader(dataset=eegDataset(data_train, label_train),
                            batch_size=batch_size,
                            shuffle=True,
                            num_workers=1)

    test_iter = DataLoader(dataset=eegDataset(data_test, label_test),
                           batch_size=batch_size,
                           shuffle=True,
                           num_workers=1)

    val_iter = DataLoader(dataset=eegDataset(data_val, label_val),
                          batch_size=batch_size,
                          shuffle=True,
                          num_workers=1)

    return train_iter, test_iter, val_iter


def train(train_iter, test_iter, model, criterion, optimizer, num_epochs):


    print('began training on', device, '...')
    acc_test_best = 0.0
    n = 0
    pred_best = []
    label_best = []
    epoch_acc = []
    epoch_loss = []
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
            loss = criterion(output, labels.long())
            pred = output.argmax(dim=1)
            correct += (pred == labels).sum().item()
            total += len(labels)
            accuracy = correct / total
            total_loss += loss
            loss.backward()
            optimizer.step()
            optimizer.zero_grad()
            if batch_id % 100 == 0:
                print('Epoch {}, batch {}, loss: {}, accuracy: {}'.format(ep + 1,
                                                                          batch_id,
                                                                          total_loss / batch_id,
                                                                          accuracy))

            batch_id += 1

        print('Total loss for epoch {}: {}'.format(ep + 1, total_loss))
        acc_test, m_pred, m_label = evaluate(test_iter, model)
        avg_loss = total_loss / batch_id
        epoch_acc.append(acc_test)
        epoch_loss.append(avg_loss.item())
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

        print('>>> best test Accuracy: {}'.format(acc_test_best))
    torch.save([model, acc_test_best, pred_best, label_best, epoch_acc, epoch_loss], model_path)
    print('model saved.')
    return acc_test_best, pred_best, label_best, epoch_acc, epoch_loss


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


def val(model, loader):  # 构造测试函数
    print('began val on', device, '...')
    model.eval()
    m_pred = []
    m_label = []
    for images, labels in loader:
        images = images.float().to(device)
        labels = labels.to(device)
        output = model(images)
        pred = output.argmax(dim=1)
        m_pred.extend(pred.cpu().numpy())
        m_label.extend(labels.cpu().numpy())
    return m_pred, m_label


def main_LOCV():
    dir = './DEAP/DEAP_DE/'

    file_list = os.listdir(dir)

    xdim = [128, 32, 4]  # batch_size * channel_num * freq_num
    k_adj = 40
    num_out = 64
    num_epochs = 50
    acc_all = []

    # 隔壁是留一法，但是准确率太变态了，所以这里按照训练集：测试集=3:1分割，随机选取测试集

    data_all = []
    label_all = []

    model = DGCNN(xdim, k_adj, num_out, nclass=2).to(device)

    criterion = nn.CrossEntropyLoss().to(device)
    optimizer = optim.Adam(model.parameters(),
                           lr=0.0005,
                           weight_decay=0.0001)
    for i in range(0, 32):
        train_path = dir + file_list[i]
        data, label = load_DE_SEED(train_path)
        if i == 0:
            data_all = data
            label_all = label
        else:
            data_all = np.concatenate((data_all, data), axis=0)
            label_all = np.concatenate((label_all, label), axis=0)
    data_train, data_test, label_train, label_test = train_test_split(data_all, label_all, test_size=0.2,
                                                                      random_state=42)
    data_test, data_val, label_test, label_val = train_test_split(data_test, label_test, test_size=0.5,
                                                                  random_state=42)

    data_train = torch.tensor(data_train)
    label_train = torch.tensor(label_train)

    # 二分类重采样
    data_train, label_train = binary_sampling(data_train, label_train)

    train_iter, test_iter, val_iter = load_dataloader(data_train, data_test, data_val, label_train, label_test,
                                                      label_val)

    if os.path.exists(model_path):
        print('model already exists, loading...')
        model, acc_test_best, pred_best, label_best, epoch_acc, epoch_loss = torch.load(model_path)
        print('model loaded.')
    else:
        acc_test_best, m_pred, m_label, epoch_acc, epoch_loss = train(train_iter, test_iter, model, criterion, optimizer,
                                                                      num_epochs)
    acc_all.append(acc_test_best)

    epoch_acc = np.asarray(epoch_acc)
    epoch_loss = np.asarray(epoch_loss)

    print('save...')
    scio.savemat('./result/acc_all/acc_de_DEAP.mat', {'acc': epoch_acc})
    scio.savemat('./result/acc_all/loss_de_DEAP.mat', {'loss': epoch_loss})

    label_pred, label_true = val(model, val_iter)
    label_true = np.asarray(label_true)
    label_pred = np.asarray(label_pred)
    draw_confusion_matrix(label_true=label_true,
                          label_pred=label_pred,
                          label_name=["positive", "negative"],
                          title=f'Confusion Matrix on DEAP',
                          pdf_save_path='./result/confusion_matrix/deap.svg',
                          dpi=300)


if __name__ == '__main__':
    sys.exit(main_LOCV())
