import matplotlib.pyplot as plt
import os

from sklearn.model_selection import KFold
import numpy as np
import torch
from torch import nn

from deap import DeapDataset
from model import ECLGCNN
from sklearn.metrics import confusion_matrix


device = torch.device('cuda:0')
loss_fn = nn.CrossEntropyLoss().to(device)

model_dir = './model/'
if not os.path.exists(model_dir):
    os.mkdir(model_dir)

img_dir = './imgs/'
if not os.path.exists(img_dir):
    os.mkdir(img_dir)

seeds = range(0, 3)
for seed in seeds:
    model_seed_dir = model_dir + f'seed{seed}'
    if not os.path.exists(model_seed_dir):
        os.mkdir(model_seed_dir)

    img_seed_dir = img_dir + f'seed{seed}'
    if not os.path.exists(img_seed_dir):
        os.mkdir(img_seed_dir)

label_types = ['valence', 'arousal', 'dominance']
params = {
    'T': 6,
    'batch_size': 32,
    'max_iteration': 1000,  # maximum number of iterations
    'k_fold': 5,
}


def train(model, device, train_data, train_labels, loss_fn, optimizer):
    print('training...')

    for data in train_data:
        data.to(device)

    model.to(device)
    model.train()
    batch_size = params['batch_size']

    step = 0
    flag = True
    while flag:
        for i in range(0, len(train_data), batch_size):
            batch = train_data[i: i + batch_size]
            label = train_labels[i: i + batch_size].to(device)

            output = model(batch)
            loss = loss_fn(output, label)

            if step % 100 == 0:
                print(f'step: {step}, Loss: {loss.item()}')

            if loss < params['e']:
                flag = False
                break

            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

            step += 1
            if step >= params['max_iteration']:
                flag = False
                break

    print('training successfully ended.')
    return model


def validate(model, device, val_data, val_labels):
    print('validating...')
    model.to(device)
    model.eval()

    TP = 0
    TN = 0
    FP = 0
    FN = 0

    label_t = []
    label_p = []

    with torch.no_grad():
        for i, data in enumerate(val_data):
            output = model([data.to(device)])
            result = torch.argmax(output, dim=-1).flatten().item()
            label = val_labels[i]
            label_t.append(result)
            label_p.append(label)
            if result == 0 and result == label:
                TP += 1
            if result == 0 and result != label:
                FP += 1
            if result == 1 and result == label:
                TN += 1
            if result == 1 and result != label:
                FN += 1

    acc = (TP + TN) / (TP + TN + FP + FN)

    if TP + FP == 0:
        precision = 0
    else:
        precision = TP / (TP + FP)

    if TP + FN == 0:
        recall = 0
    else:
        recall = TP / (TP + FN)

    if precision + recall == 0:
        f_score = 0
    else:
        f_score = 2 * precision * recall / (precision + recall)

    print(f'acc: {acc}')
    print(f'precision: {precision}')
    print(f'recall: {recall}')
    print(f'F_score: {f_score}')

    return acc, precision, recall, f_score, label_t, label_p


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


def output_figure(path, acc, f_score, title):
    fig = plt.figure(figsize=(10, 5))
    axes = fig.add_axes([0.1, 0.1, 0.8, 0.8])
    x = np.arange(1, 33, dtype=int)

    axes.plot(x, acc, 'rs-')  # 红色，正方形点，实线
    axes.plot(x, f_score, 'bo--')  # 蓝色，圆点，虚线
    axes.legend(labels=('acc', 'F-score'), loc='lower right', fontsize=16)

    axes.set_ylim(0, 1)
    axes.set_yticks(np.arange(0, 1.1, 0.1))
    axes.set_yticklabels([0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1], fontsize=16)

    axes.set_xticks(x)
    axes.set_xticklabels(x, fontsize=12)
    axes.set_xlabel('subject', fontsize=14)

    axes.set_title(title, fontsize=18)
    axes.grid(True)

    fig.savefig(path)
    fig.show()


def cross_validation(data, labels, seed):
    splits = KFold(n_splits=params['k_fold'], shuffle=True, random_state=seed)

    acc_list = []
    f_score_list = []
    fold_models = []
    label_t = []
    label_p = []

    for fold, (train_idx, val_idx) in enumerate(splits.split(np.arange(len(data)))):
        print(f'******fold {fold+1}******')
        train_data = []
        for i in train_idx:
            train_data.append(data[i])
        train_labels = labels[train_idx]

        val_data = []
        for i in val_idx:
            val_data.append(data[i])
        val_labels = labels[val_idx]

        train_data, train_labels = binary_sampling(train_data, train_labels)


        model = ECLGCNN(K=params['K'], T=params['T'], num_cells=params['num_cells'])


        optimizer = torch.optim.Adam(model.parameters(), lr=params['lr'], weight_decay=params['alpha'])

        trained_model = train(model, device, train_data, train_labels, loss_fn, optimizer)
        fold_models.append(trained_model)

        acc, precision, recall, f_score, label_t, label_p = validate(trained_model, device, val_data, val_labels)
        acc_list.append(acc)
        f_score_list.append(f_score)

    avg_acc = np.array(acc_list).mean()
    avg_f_score = np.array(f_score_list).mean()
    return fold_models, avg_acc, avg_f_score, label_t, label_p


def subject_dependent_exp():
    print('------------------------------------------------')
    print('|         subject dependent experiment         |')
    print('------------------------------------------------')

    params['K'] = 2
    params['num_cells'] = 30
    params['e'] = 0.1
    params['lr'] = 0.003
    params['alpha'] = 0.0008

    dataset = DeapDataset('./date')
    avg_acc_list = []
    avg_f_score_list = []

    for i in range(3):
        print(f'=============={label_types[i]}==============')

        acc_arrays = []
        f_score_arrays = []

        for seed in seeds:
            acc_list = []
            f_score_list = []
            label_true = []
            label_pred = []

            step = 1
            for data, label in dataset:
                print(f'-------seed {seed}, subject {step}-------')
                used_label = label[:, i]

                model_path = model_dir + f'seed{seed}/model_{step}_{label_types[i]}.pt'
                if os.path.exists(model_path):
                    print('model already exists, loading...')
                    trained_models, acc, f_score, label_t, label_p = torch.load(model_path)
                    print('model loaded.')
                else:
                    trained_models, acc, f_score, label_t, label_p = cross_validation(data, used_label, seed)
                    torch.save([trained_models, acc, f_score, label_t, label_p], model_path)
                    print('model saved.')

                acc_list.append(acc)
                f_score_list.append(f_score)
                label_true.append(label_t)
                label_pred.append(label_p)
                print(f'avg_acc: {acc}, avg_f_score: {f_score}')

                step += 1

            acc_array = np.array(acc_list)
            f_score_array = np.array(f_score_list)
            label_true_array = np.array(label_true).reshape(-1)
            label_pred_array = np.array(label_pred).reshape(-1)
            img_path = img_dir + f'seed{seed}/dependent_{label_types[i]}.svg'
            matrix_path = img_dir + f'seed{seed}/Confusion_Matrix_{label_types[i]}.svg'
            output_figure(img_path, acc_array, f_score_array, f'{label_types[i]}')

            draw_confusion_matrix(label_true=label_true_array,
                                  label_pred=label_pred_array,
                                  label_name=["positive", "negative"],
                                  title=f'Confusion Matrix on DEAP of {label_types[i]}',
                                  pdf_save_path=matrix_path,
                                  dpi=300)
            acc_arrays.append(acc_array)
            f_score_arrays.append(f_score_array)

        # calculate the average Acc and F-score of 3 seeds
        avg_acc = sum(acc_arrays) / len(acc_arrays)
        avg_acc_list.append(avg_acc)

        avg_f_score = sum(f_score_arrays) / len(f_score_arrays)
        avg_f_score_list.append(avg_f_score)

        output_figure(f'./imgs/dependent_{label_types[i]}_avg.png', avg_acc, avg_f_score, f'{label_types[i]}_avg')

    print('-------------------RESULT-------------------')
    for i in range(3):
        print(f'{label_types[i]}')
        print(f'    acc: {avg_acc_list[i]}')
        print(f'    f-score: {avg_f_score_list[i]}')
        print(f'    avg_acc = {np.mean(avg_acc_list[i])}, avg_f_score = {np.mean(avg_f_score_list[i])}')


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


if __name__ == '__main__':
    subject_dependent_exp()
