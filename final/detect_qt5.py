# YOLOv5 🚀 by Ultralytics, GPL-3.0 license
"""
Run inference on images, videos, directories, streams, etc.

Usage:
    $ python path/to/detect.py --source path/to/img.jpg --weights yolov5s.pt --img 640
"""

import argparse
import sys
from pathlib import Path

import cv2
import numpy as np
import torch

FILE = Path(__file__).resolve()
ROOT = FILE.parents[0]  # YOLOv5 root directory
if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))  # add ROOT to PATH
ROOT = ROOT.relative_to(Path.cwd())  # relative

from models.experimental import attempt_load
from utils.general import apply_classifier, check_img_size, check_imshow, check_requirements, check_suffix, colorstr, \
    increment_path, non_max_suppression, print_args, set_logging, \
    strip_optimizer, xyxy2xywh, scale_boxes
from utils.plots import Annotator, colors
from utils.torch_utils import  select_device, time_sync
from utils.augmentations import letterbox


class v5detect:
    def __init__(self):
        self.model, self.stride, self.pt, self.dt, self.seen, self.names, self.device = self.myloadModelInitialize()

    def detect(self,img):
        #img为数组形式
        return self.run(img, self.model, self.stride, self.pt, self.dt, self.seen, self.names, self.device)

    @torch.no_grad()
    def myloadModelInitialize(self):
        half = False
        weights = ROOT / 'weights/best.pt'
        imgsz = [640, 640]
        # Initialize
        set_logging()
        device = select_device(' ')
        model = attempt_load(weights, device=device)  # load FP32 model

        half &= device.type != 'cpu'  # half precision only supported on CUDA

        # Load model
        w = weights[0] if isinstance(weights, list) else weights
        classify, suffix, suffixes = False, Path(w).suffix.lower(), ['.pt', '.onnx', '.tflite', '.pb', '']
        check_suffix(w, suffixes)  # check weights have acceptable suffix
        pt, onnx, tflite, pb, saved_model = (suffix == x for x in suffixes)  # backend booleans
        stride, names = 64, [f'class{i}' for i in range(1000)]  # assign defaults
        if pt:
            stride = int(model.stride.max())  # model stride
            names = model.module.names if hasattr(model, 'module') else model.names  # get class names

        imgsz = check_img_size(imgsz, s=stride)  # check image size

        # Dataloader

        # Run inference
        if pt and device.type != 'cpu':
            model(torch.zeros(1, 3, *imgsz).to(device).type_as(next(model.parameters())))  # run once
        dt, seen = [0.0, 0.0, 0.0], 0
        # for path, img, im0s, vid_cap in dataset:
        return model, stride, pt, dt, seen, names, device

    def myLoadImages(self,img0, img_size, stride, auto):
        '''

        :param img: np数组形式的图像
        :return: 填充后的图像
        '''
        # print("myLoadImages")
        img = letterbox(img0, img_size, stride, auto)[0]

        # Convert
        img = img.transpose((2, 0, 1))[::-1]  # HWC to CHW, BGR to RGB
        img = np.ascontiguousarray(img)
        return img

    @torch.no_grad()
    def run(self,im0s, model, stride, pt, dt, seen, names, device,
            imgsz=[1440,1440],  # inference size (pixels)
            conf_thres=0.25,  # confidence threshold
            iou_thres=0.45,  # NMS IOU threshold
            max_det=1000,  # maximum detections per image

            view_img=True,  # show results
            save_img=True,  # save cropped prediction boxes
            classes=None,  # filter by class: --class 0, or --class 0 2 3
            agnostic_nms=False,  # class-agnostic NMS
            augment=False,  # augmented inference
            line_thickness=3,  # bounding box thickness (pixels)
            hide_labels=False,  # hide labels
            hide_conf=False,  # hide confidences
            half=False,  # use FP16 half-precision inference
            ):

        '''上面代码运行一次即可，下面代码直接替换im0s变量，可以检测不同文件'''
        img = self.myLoadImages(im0s, imgsz, stride, pt)
        t1 = time_sync()

        img = torch.from_numpy(img).to(device)
        img = img.half() if half else img.float()  # uint8 to fp16/32
        img = img / 255.0  # 0 - 255 to 0.0 - 1.0
        if len(img.shape) == 3:
            img = img[None]  # expand for batch dim
        t2 = time_sync()
        dt[0] += t2 - t1

        # Inference
        if pt:
            visualize = False
            pred = model(img, augment=augment, visualize=visualize)[0]

        else :
            return -1

        t3 = time_sync()
        dt[1] += t3 - t2

        # NMS
        pred = non_max_suppression(pred, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)
        dt[2] += time_sync() - t3
        line = None
        # Process predictions
        myRes = []

        for i, det in enumerate(pred):  # per image
            seen += 1

            s, im0 =  '', im0s.copy()

            s += '%gx%g ' % img.shape[2:]  # print string

            annotator = Annotator(im0, line_width=line_thickness, example=str(names))
            if len(det):
                # Rescale boxes from img_size to im0 size
                det[:, :4] = scale_boxes(img.shape[2:], det[:, :4], im0.shape).round()

                # Print results
                for c in det[:, -1].unique():
                    n = (det[:, -1] == c).sum()  # detections per class
                    s += f"{n} {names[int(c)]}{'s' * (n > 1)}, "  # add to string

                # Write results
                for *xyxy, conf, cls in reversed(det):

                    xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) ).view(-1).tolist()  #  xywh
                    line = (int(cls), *xywh, float(conf))   # label format
                    myRes.append(line)
                    if save_img  or view_img:  # Add bbox to image
                        c = int(cls)  # integer class
                        label = None if hide_labels else (names[c] if hide_conf else f'{names[c]} {conf:.2f}')
                        annotator.box_label(xyxy, label, color=colors(c, True))
            im0 = annotator.result()
        return myRes, im0


if __name__ == '__main__':
    v5 = v5detect()
