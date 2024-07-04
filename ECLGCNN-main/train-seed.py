import matplotlib.pyplot as plt
import os

from sklearn.model_selection import KFold
import numpy as np
import torch
from torch import nn

from seed import SeedDateset
from model_seed import ECLGCNN
from sklearn.metrics import confusion_matrix




device = torch.device('cuda:0')
loss_fn = nn.CrossEntropyLoss().to(device)

model_dir = './seed/model/'
if not os.path.exists(model_dir):
    os.mkdir(model_dir)

img_dir = './seed/imgs/'
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


params = {
    'T': 6,
    'batch_size': 32,
    'max_iteration': 1000,  # maximum number of iterations
    'k_fold': 2,
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
            loss = loss_fn(output, label.long())

            if step % 10 == 0:
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

    acc_cnt = 0

    label_t = []
    label_p = []

    with torch.no_grad():
        for i, data in enumerate(val_data):
            output = model([data.to(device)])
            result = torch.argmax(output, dim=-1).flatten().item()
            label = val_labels[i]
            label_t.append(result)
            label_p.append(label)
            if result == label:
                acc_cnt += 1

    acc = acc_cnt / len(val_data)


    print(f'acc: {acc}')

    return (acc,
        # precision, recall, f_score,
        label_t, label_p)


def output_figure(path, acc, title):
    fig = plt.figure(figsize=(10, 5))
    axes = fig.add_axes([0.1, 0.1, 0.8, 0.8])
    x = np.arange(1, 16, dtype=int)

    axes.plot(x, acc, 'rs-')  # 红色，正方形点，实线
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
        train_labels = []
        for i in train_idx:
            train_data.append(data[i])
            train_labels.append(labels[i])
        train_labels = torch.tensor(train_labels)
        val_data = []
        val_labels = []
        for i in val_idx:
            val_data.append(data[i])
            val_labels.append(labels[i])
        val_labels = torch.tensor(val_labels)
        model = ECLGCNN(K=params['K'], T=params['T'], num_cells=params['num_cells'])
        optimizer = torch.optim.Adam(model.parameters(), lr=params['lr'], weight_decay=params['alpha'])

        trained_model = train(model, device, train_data, train_labels, loss_fn, optimizer)
        fold_models.append(trained_model)

        acc, label_t, label_p = validate(trained_model, device, val_data, val_labels)
        acc_list.append(acc)


    avg_acc = np.array(acc_list).mean()

    return (fold_models,avg_acc, label_t, label_p)


def subject_dependent_exp():
    print('------------------------------------------------')
    print('|         subject dependent experiment         |')
    print('------------------------------------------------')

    params['K'] = 2
    params['num_cells'] = 30
    params['e'] = 0.1
    params['lr'] = 0.003
    params['alpha'] = 0.0008

    dataset = SeedDateset('./seed')

    for seed in seeds:
        acc_list = []
        f_score_list = []
        label_true = []
        label_pred = []

        step = 1
        for data, label in dataset:
            print(f'-------seed {seed}, subject {step}-------')
            used_label = label
            model_path = model_dir + f'seed{seed}/model_{step}.pt'
            if os.path.exists(model_path):
                print('model already exists, loading...')
                trained_models, acc, label_t, label_p = torch.load(model_path)
                print('model loaded.')
            else:
                trained_models, acc, label_t, label_p = cross_validation(data, used_label, seed)
                torch.save([trained_models, acc, label_t, label_p], model_path)
                print('model saved.')

            acc_list.append(acc)
            label_true.append(label_t)
            label_pred.append(label_p)
            print(f'avg_acc: {acc}')

            step += 1

        acc_array = np.array(acc_list)
        label_true_array = np.array(label_true).reshape(-1)
        label_pred_array = np.array(label_pred).reshape(-1)
        img_path = img_dir + f'seed{seed}/dependent.svg'
        matrix_path = img_dir + f'seed{seed}/Confusion_Matrix.svg'
        output_figure(img_path, acc_array, 'acc of SEED')

        draw_confusion_matrix(label_true=label_true_array,
                              label_pred=label_pred_array,
                              label_name=["positive", "neutral", "negative"],
                              title=f'Confusion Matrix on SEED',
                              pdf_save_path=matrix_path,
                              dpi=300)

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
