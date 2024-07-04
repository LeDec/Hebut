##网络模型；这里要全部换掉

import time
import cv2
import torch
from PIL import Image
from torchvision import transforms
from torchvision.models import MobileNetV2
import os
#import contorl.moto_logistic as ml
#import RPi.GPIO as GPIO

class MyModel:
    def __init__(self):
        self.model = None
        self.state = 1
        self.load_model()#load weights on model
        


    def load_model(self):
        # os.system('fswebcam -r 1280x720 --no-banner image.jpg >/dev/null 2>&1')
        # time.sleep(1)
        model_weight_path = "mobilenet_v2_14_bestacc.pth"
        self.model = MobileNetV2(num_classes=14)
        self.model.load_state_dict(torch.load(model_weight_path, map_location=torch.device('cpu')))

    def predict(self):
        os.system('fswebcam -r 1920x1080 --no-banner image.jpg')
        img = Image.open("image.jpg")
        data_transform = transforms.Compose(
            [transforms.Resize(256),
             transforms.CenterCrop(224),
             transforms.ToTensor(),
             transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])])
        img = data_transform(img)
        img = torch.unsqueeze(img, dim=0)
       
        self.model.eval()
        with torch.no_grad():
            # predict class
            output = torch.squeeze(self.model(img))
            predict = torch.softmax(output, dim=0)
            predict_det = int(torch.argmax(predict).numpy())
        print("predict_class=" + str(predict_det))
        cate_max_final, predict_cla = self.classfic(predict_det)
        print("cate_max_final=", cate_max_final)
        print(predict_cla)

        return predict_cla,predict_det

    def throw(self, predict_cla):
        if predict_cla != 5:
            print("get garbage!")
            self.state = ml.moto_loop(predict_cla, self.state)
        else:
            print("no garbage!")


    def classfic(self, predict_det):
        if predict_det >= 0 and predict_det <= 2:
            cate_max_final = "可回收垃圾"
            predict_cla = 1
        elif predict_det >= 3 and predict_det <= 5:
            cate_max_final = "有害垃圾"
            predict_cla = 4
        elif predict_det >= 6 and predict_det <= 8:
            cate_max_final = "厨余垃圾"
            predict_cla = 3
        elif predict_det >= 9 and predict_det<= 10:
            cate_max_final = "其他垃圾"
            predict_cla = 2
        else:
            cate_max_final = "empty garbage"
            predict_cla = 5
        return cate_max_final,predict_cla