# -*- coding: utf-8 -*-
from darkflow.net.build import TFNet
import cv2
import os
import json
import numpy as np
from PIL import Image
import random
import csv
import sys
import math
from scipy import genfromtxt
from sklearn.cluster import KMeans
import pandas as pd
from sklearn.externals import joblib
from sklearn import svm
import matplotlib

save_path = 'sample_img/output/'
open_path = 'sample_img/'

#detection関数
def inputdata(image_name):
    os.chdir('/var/www/KB_1810/learn/')
    options = {"model": "/var/www/KB_1810/learn/cfg/yolov2-voc.cfg", "load": "/var/www/KB_1810/learn/bin/yolo_learn.weights", "threshold": 0.4, "gpu": 0.3}

    tfnet = TFNet(options)


    input_image = image_name
    image_folder = "sample_img"
    current_path  = os.getcwd()
    output_file = "out"
    current_path =  os.path.join(current_path,image_folder)
    output_path =  os.path.join(current_path,output_file)
    if not os.path.exists(output_path):
        print('Creating output path {}'.format(output_path))
        os.mkdir(output_path)

    src = cv2.imread(os.path.join(current_path,input_image))
    dst = src
    #cv2.imshow("img", src)

    result, result1 = tfnet.return_predict(src,dst)
    print(result)

    #cv2.imshow("img_out", dst)
    cv2.waitKey()
    cv2.imwrite(output_path + '\\' + input_image, dst)
    cv2.imwrite("result1.png",dst)

    return result1


#detectionされた部分を画像にする
def image_split(img_name):
    global save_path
    global open_path
    #save_path1 = 'sample_img'
    img = img_name
    #detectionする boxdataにはobjectの座標が入る
    boxdata = inputdata(img)

    subregion = list()
    pic = Image.open(open_path + img)


    #detection画像の分割
    for boxs in boxdata:
        box = (int(boxs[0]), int(boxs[2]), int(boxs[1]), int(boxs[3]))
        #print(box)
        subregion.append(pic.crop(box))

    for num in range(len(boxdata)):
        subregion[num].save(save_path +str(num) + 'bus.jpg',"JPEG")

    return boxdata

# 点p0に一番近い点を点群psから抽出
def serch_neighbourhood(p0, ps):
    L = np.array([])
    for i in range(ps.shape[0]):
        L = np.append(L,np.linalg.norm(ps[i]-p0))
    return ps[np.argmin(L)]

#detection画像の色抽出
def color_detection(image_name):
    box = image_split(image_name)
    print(box)
    color = np.empty((0,3), int)
    count1 = 0
    while(count1 <= len(box) -1):
        #global save_path
        #global open_path
        z=str(count1)
        gazou = save_path + z + 'bus.jpg'
        imag = Image.open(gazou)
        a = imag.size[0]
        r = imag.size[1]
        #img = np.array(Image.open(gazosu))
        img = cv2.imread(gazou)
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        #print(img)
        img_base = img.reshape(-1,3)

        img_flat=img_base
        color = np.append(color, img_flat,axis = 0)

        count1 = count1 + 1

    '''
    #クラスタリング
    kmeans_modeluv = KMeans(n_clusters=5).fit(color)
    labelsuv = kmeans_modeluv.labels_
    #print 'center' + str(kmeans_modeluv.cluster_centers_)
    #代表色
    center = kmeans_modeluv.cluster_centers_
    print(center)

    #離散化
    ps = np.array([[255,255,255],[0,0,0],[159,160,160],[255,0,0],[0,0,255],[0,105,62],[255,241,0],[235,97,0],[241,158,194],[146,7,131],[149,86,41],[128,128,0],[0,0,128]])

    for i, repcolor in enumerate(center):
        center[i] = serch_neighbourhood(repcolor ,ps)

    print(center)

    dis_center = np.array([])

    for i in range(len(center)):
        for j in range(len(ps)):
            if all(center[i] == ps[j]):
                dis_center = np.append(dis_center, [j])

    print(dis_center)

    #降順
    labels = kmeans_modeluv.labels_
    print(np.sum(labels == 0), np.sum(labels == 1), np.sum(labels == 2), np.sum(labels == 3), np.sum(labels == 4))
    print(len(labels))
    '''
    kmeans_modeluv = KMeans(n_clusters=5).fit(color)

    labelsuv = kmeans_modeluv.labels_
    #print 'center' + str(kmeans_modeluv.cluster_centers_)
    #代表色
    center = kmeans_modeluv.cluster_centers_
    #print(center)

    img_compuv=kmeans_modeluv.cluster_centers_[kmeans_modeluv.labels_]
    uvlabel2 = np.c_[img_compuv,labelsuv]


    rows, cols = np.where(uvlabel2 != 0)
    np.delete(uvlabel2,rows[np.where(cols==3)],0)
    uv20 = np.delete(uvlabel2,np.where(uvlabel2 != 0)[0][np.where(np.where(uvlabel2 != 0)[1] == 3)],0)
    uv20 = uv20[0]

    rows, cols = np.where(uvlabel2 != 1)
    np.delete(uvlabel2,rows[np.where(cols==3)],0)
    uv21 = np.delete(uvlabel2,np.where(uvlabel2 != 1)[0][np.where(np.where(uvlabel2 != 1)[1] == 3)],0)
    uv21 = uv21[0]

    rows, cols = np.where(uvlabel2 != 2)
    np.delete(uvlabel2,rows[np.where(cols==3)],0)
    uv22 = np.delete(uvlabel2,np.where(uvlabel2 != 2)[0][np.where(np.where(uvlabel2 != 2)[1] == 3)],0)
    uv22 = uv22[0]

    rows, cols = np.where(uvlabel2 != 3)
    np.delete(uvlabel2,rows[np.where(cols==3)],0)
    uv23 = np.delete(uvlabel2,np.where(uvlabel2 != 3)[0][np.where(np.where(uvlabel2 != 3)[1] == 3)],0)
    uv23 = uv23[0]

    rows, cols = np.where(uvlabel2 != 4)
    np.delete(uvlabel2,rows[np.where(cols==3)],0)
    uv24 = np.delete(uvlabel2,np.where(uvlabel2 != 4)[0][np.where(np.where(uvlabel2 != 4)[1] == 3)],0)
    uv24 = uv24[0]

    uv20 = np.delete(uv20,3)
    uv21 = np.delete(uv21,3)
    uv22 = np.delete(uv22,3)
    uv23 = np.delete(uv23,3)
    uv24 = np.delete(uv24,3)
    #uv24 = np.delete(uv24, 2)

    labels = kmeans_modeluv.labels_
    uv20 = np.append(uv20, np.sum(labels == 0)/len(labels))
    uv21 = np.append(uv21, np.sum(labels == 1)/len(labels))
    uv22 = np.append(uv22, np.sum(labels == 2)/len(labels))
    uv23 = np.append(uv23, np.sum(labels == 3)/len(labels))
    uv24 = np.append(uv24, np.sum(labels == 4)/len(labels))

    #uv20 = np.array([uv20])
    arr = np.empty((0,4), int)

    arr = np.append(arr, np.array([uv20]), axis=0)
    arr = np.append(arr, np.array([uv21]), axis=0)
    arr = np.append(arr, np.array([uv22]), axis=0)
    arr = np.append(arr, np.array([uv23]), axis=0)
    arr = np.append(arr, np.array([uv24]), axis=0)

    #print(arr)


    daihyousp = arr[arr[:,3].argsort()[::-1],:]
    #print(daihyousp)
    daihyouspi = np.delete(daihyousp, 3, 1)
    print(daihyousp)
    per_list = daihyousp[:,[3]]
    #print(daihyousp)


    #離散化
    ps = np.array([[255,255,255],[0,0,0],[159,160,160],[255,0,0],[0,0,255],[0,105,62],[255,241,0],[235,97,0],[241,158,194],[146,7,131],[149,86,41],[128,128,0],[0,0,128]])

    for i, repcolor in enumerate(daihyouspi):
        daihyouspi[i] = serch_neighbourhood(repcolor ,ps)



    dis_center = np.array([])

    for i in range(len(daihyouspi)):
        for j in range(len(ps)):
            if all(daihyouspi[i] == ps[j]):
                dis_center = np.append(dis_center, [j])

    return dis_center,per_list 

def color_point(img_name):
    center, per_list  = color_detection(img_name)
    target = np.array([center])
    clf = joblib.load('clf.pkl')
    point = clf.predict(target)
    return center, per_list, point


#center, per_list, point = color_point('kondate.jpg')
#print(center, per_list, point)
