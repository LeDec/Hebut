import threading
import time

import cv2
from PyQt5.QtCore import QStringListModel
from PyQt5.QtGui import QImage, QPixmap

from detect_qt5 import v5detect

class Display:
    def __init__(self, ui, mainWnd):

        self.ui = ui
        self.mainWnd = mainWnd
        self.capfile = cv2.VideoCapture('test.mp4')
        self.frameRate = 30

        # 轮次垃圾桶初始化id
        self.diedao1 = ""
        self.diedao2 = ""
        self.diedao3 = ""
        self.diedao4 = ""
        self.time1 = 0
        self.time2 = 0
        self.time3 = 0
        self.time4 = 0
        self.diedao1time = ""
        self.diedao2time = ""
        self.diedao3time = ""
        self.diedao4time = ""

        self.freeFrame = 0

        self.warningTime = 0.50

        # 表示第几个垃圾
        self.num = self.diedao1 + self.diedao2 + self.diedao3 + self.diedao4

        # 垃圾详细信息
        self.listView = ["本轮跌倒检测详情"]
        self.slm = QStringListModel()

        # 刷新界面数据
        self.refresh_num()

        # 初始化按键，比赛应该用不到
        self.cleck_init()

        # 控制视频播放状态
        self.videos = 0

        # 加载模型
        self.model = v5detect()

        # 播放视频线程
        thv = threading.Thread(target=self.Displav)
        thv.setDaemon(True)
        thv.start()

    # 刷新显示当前垃圾桶信息，数据发生更改后及时调用
    def refresh_num(self):
        self.ui.label_6.setText(str(self.diedao1))
        self.ui.label_8.setText(str(self.diedao2))
        self.ui.label_10.setText(str(self.diedao3))
        self.ui.label_12.setText(str(self.diedao4))
        self.ui.label_5.setText(str(self.diedao1time))
        self.ui.label_7.setText(str(self.diedao2time))
        self.ui.label_9.setText(str(self.diedao3time))
        self.ui.label_11.setText(str(self.diedao4time))
        self.slm.setStringList(self.listView)
        self.ui.listView.setModel(self.slm)

    # 重置本轮次跌倒检测信息
    def global_init(self):
        self.diedao1 = ""
        self.diedao2 = ""
        self.diedao3 = ""
        self.diedao4 = ""
        self.count = ""
        self.kes = ""
        self.hais = ""
        self.chus = ""
        self.others = ""
        self.que = ''
        self.listView = ["本轮跌倒检测详情"]

        # 数据更新之后刷新界面
        self.refresh_num()

    # 初始化点击指令链接函数
    def cleck_init(self):
        self.ui.new_one.clicked.connect(self.global_init)  # 重置本轮次垃圾投放信息
        self.ui.switch_2.clicked.connect(self.switch)  # 视频播放暂停控制


    def switch(self):
        if self.videos == 0:
            self.videos = 1
            self.ui.switch_2.setText('点击播放')
        else:
            self.videos = 0
            self.ui.switch_2.setText('暂停播放')

    def Displav(self):
        frame_counter = 0
        while self.capfile.isOpened() and True:
            if self.videos == 1:
                continue
            success, im0 = self.capfile.read()
            label, frame = self.model.detect(im0)
            frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
            img = QImage(frame.data, frame.shape[1], frame.shape[0], QImage.Format_RGB888)
            self.ui.disvideo.setPixmap(QPixmap.fromImage(img).scaled(721, 401))
            time.sleep(self.frameRate / 1000)
            for i in range(len(label)):
                if label[i][0] == 1:
                    if i == 0:
                        self.freeFrame = 0
                        self.time1 += 1
                        fallTime1 = round((self.time1 / self.frameRate),1)
                        if fallTime1 > self.warningTime:
                            add = str(i) + "号位置发现跌倒！已经跌倒" + str(fallTime1) + "秒，当前置信度为" + str(round(label[i][5],4))
                            self.listView.append(add)
                        self.diedao1 = str(i)
                        self.diedao1time = str(fallTime1) + "秒"
                    if i == 1:
                        self.time1 += 1
                        fallTime2 = round((self.time2 / self.frameRate), 1)
                        if fallTime2 > self.warningTime:
                            self.diedao2 = str(i)
                            self.diedao2time = str(fallTime2) + "秒"
                            add = str(i) + "号位置发现跌倒！已经跌倒" + str(fallTime2) + "秒，当前置信度为" + str(round(label[i][5], 4))
                            self.listView.append(add)
                        self.listView.append(add)
                    self.refresh_num()
            if len(label) == 0:
                self.freeFrame += 1
                if self.freeFrame > 2 * self.frameRate:
                    self.diedao1 = ""
                    self.diedao1time = ""
                    self.time1 = 0
                    self.diedao2 = ""
                    self.diedao2time = ""
                    self.time2 = 0
                    self.refresh_num()
            frame_counter += 1
            if frame_counter == int(self.capfile.get(cv2.CAP_PROP_FRAME_COUNT)):
                frame_counter = 0
                self.capfile.set(cv2.CAP_PROP_POS_FRAMES, 0)