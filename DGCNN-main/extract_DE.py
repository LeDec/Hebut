import scipy.io as scio
import os
import numpy as np


if __name__ == '__main__':
    extrac_path = 'E:/project/DGCNN-main/SEED/SEED_code/ExtractedFeatures/session1/'
    label_path = 'E:/project/DGCNN-main/SEED/SEED_code/ExtractedFeatures/session1/label.mat'
    save_path = 'E:/project/DGCNN-main/SEED/SEED_code/DE/session1/'
    dir_list = os.listdir(extrac_path)

    label = scio.loadmat(label_path)
    label = label['label'][0]

    for f in dir_list:
        if '_' not in f:
            continue

        S = scio.loadmat(extrac_path + f)
        DE = []
        labelAll = []
        for i in range(15):
            data = S['de_LDS' + str(i + 1)]
            if len(DE):
                DE = np.concatenate((DE, data), axis=1)
            else:
                DE = data

            if len(labelAll):
                labelAll = np.concatenate((labelAll, np.zeros([data.shape[1], 1]) + label[i]), axis = 0)
            else:
                labelAll = np.zeros([data.shape[1], 1]) + label[i]

        #print(DE.shape)
        #print(labelAll.shape)

        mdic = {"DE": DE, "labelAll": labelAll, "label": "experiment"}

        scio.savemat(save_path + f, mdic)
        print(extrac_path + f, '->', save_path + f)


