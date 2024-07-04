import torch
from torch import nn
from torch_geometric.data import Batch
from torch_geometric.nn import ChebConv
from torch_geometric.nn.norm import BatchNorm
from deap import DeapDataset


class ECLGCNN(nn.Module):
    def __init__(self, K, T, num_cells):
        """
        :param K: 切比雪夫阶数
        :param num_cells: LSTM 隐藏层单元数
        :param T: GCNN 单元数
        """
        super(ECLGCNN, self).__init__()
        self.K = K
        self.T = T

        self.convs = nn.ModuleList()
        self.batch_norms = nn.ModuleList()

        for i in range(T):
            self.convs.append(ChebConv(5, 5, self.K, normalization='sym'))
            self.batch_norms.append(BatchNorm(5))

        self.lstm = nn.LSTM(62 * 5, num_cells, batch_first=True)
        self.batch_norm1 = nn.BatchNorm1d(self.T)
        self.linear = nn.Linear(num_cells * self.T, 3)
        self.softmax1 = nn.Softmax()
        self.softmax2 = nn.Softmax()
        self.softmax3 = nn.Softmax()

        for param in self.parameters():
            if len(param.shape) < 2:
                nn.init.xavier_uniform_(param.unsqueeze(0))
            else:
                nn.init.xavier_uniform_(param)

    def forward(self, x):
        """
        前向传播
        :param x: list of Batch objects
        """
        batch_size = len(x)

        # GCNN layer
        y_list = []
        for i in range(self.T):
            xi = []
            for data in x:
                xi.append(data.get_example(i))
            batch_i = Batch.from_data_list(xi)
            yi = self.convs[i](batch_i.x, batch_i.edge_index, batch_i.edge_attr, batch_i.batch)
            yi = self.batch_norms[i](yi)
            y_list.append(yi)

        y = torch.stack(y_list)
        # y = self.softmax1(y)

        # LSTM layer
        y = y.transpose(0, 1)
        y = torch.reshape(y, (batch_size, self.T, -1))
        y, (h, c) = self.lstm(y)
        y = self.batch_norm1(y)
        # y = self.softmax2(y)

        # Dense layer
        y = torch.reshape(y, (batch_size, -1))
        y = self.linear(y)
        # y = self.softmax3(y)
        return y


if __name__ == '__main__':
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    dataset = DeapDataset('./date')
    subject_data, labels = dataset[0]

    batch = subject_data[0:20]
    for data in batch:
        data.to(device)
    label = labels[0:20].to(device)

    model = ECLGCNN(K=2, T=6, num_cells=30)
    model.to(device)

    loss_func = nn.CrossEntropyLoss().to(device)
    optimizer = torch.optim.Adam(model.parameters(), lr=0.1, weight_decay=0.1)

    output = model(batch)
    print(output)
    print(output.shape)

    loss = loss_func(output, label[:, 0])
    print(loss)

    optimizer.zero_grad()
    loss.backward()
    optimizer.step()

    result = torch.argmax(output, dim=-1)
    print(result)
