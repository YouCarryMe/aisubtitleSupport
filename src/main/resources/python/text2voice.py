# ！/usr/bin/env python 2 # -*- coding: utf-8 -*-
import sys
from sys import argv
from base64 import b64decode
from uuid import uuid4
from tencentcloud.common import credential
from tencentcloud.common.exception.tencent_cloud_sdk_exception import TencentCloudSDKException
from tencentcloud.aai.v20180522.models import TextToVoiceRequest
from tencentcloud.aai.v20180522.aai_client import AaiClient
from shutil import copyfile
import os
import shutil


def biansheng(a: str, b: int, c: int, d: str):  # (文本，声音类型，语言类型，输出路径)
    try:
        # 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
        cred = credential.Credential("AKIDf7mFTUJuDdvA1dlPJx3h8VfdR66yB4Pa", "8C6m0HWWLo1h7MM5YlIgqjurXwBEVVEo")
        # 实例化要进行语音合成请求的client对象
        client = AaiClient(cred, 'ap-beijing')
        # 实例化一个请求对象
        req = TextToVoiceRequest()
        # 请求对象属性封装
        req.Text = a  # type: str # 要合成语音的文本
        req.SessionId = uuid4()  # type: int # 一次请求对应一个SessionId，会原样返回，建议传入类似于uuid的字符串防止重复
        req.ModelType = 1  # type: int # 模型类型，默认值为1
        req.Volume = 5.0  # type: float # 音量大小，范围：[0，10]，分别对应10个等级的音量，默认为0
        req.Speed = -1  # type: float # 语速，范围：[-2，2]，分别对应不同语速：
        # -2代表0.6倍 -1代表0.8倍 0代表1.0倍（默认）1代表1.2倍 2代表1.5倍 如果需要更细化的语速，可以保留小数点后一位，例如0.5 1.1 1.8等。

        req.ProjectId = 10086  # type: int # 项目id，用户自定义，默认为0
        # type: int # 0-云小宁，亲和女声(默认) 1-云小奇，亲和男声 2-云小晚，成熟男声 4-云小叶，温暖女声
        req.VoiceType = b
        # 5-云小欣，情感女声 6-云小龙，情感男声 7-云小曼，客服女声
        # 1000-智侠，情感男声 1001-智瑜，情感女声 1002-智聆，通用女声
        # 1003-智美，客服女声 1050-WeJack，英文男声 1051-WeRose，英文女声
        # type: int # 主语言类型1:中文，最大100个汉字（标点符号算一个汉字）语言类型2:英文，最大支持400个字母（标点符号算一个字母)
        req.PrimaryLanguage = c
        req.SampleRate = 16000  # type: int # 音频采样率，16000：16k，8000：8k，默认16k
        req.Codec = 'mp3'  # type: str # 返回音频格式，可取值：wav(默认)，mp3

        # 通过client对象调用想要访问的接口，需要传入请求对象
        rep = client.TextToVoice(req)
        # rep为响应对象
        print(rep)
        """
          {
          "Audio": "UklGRlR/AABXQVZFZm10IBAAAAABAAEAgD4AAAB9AAACABAAZGF0YSx9AAD+////AQD//wAAAAAAAAIAAQADAAMABgAEAAYABQAGAAUABwAIAAgACQAAE......AAgACAAEAAgADAAIAAwACAAQAAwACAAIAAgADAAMAAgACAAIAAwABAAAAAAAAAAAAAAD/////AAAAAAAA//8AAP///v/9//7//v///////v8AAP///////wAA/////wAA/////wAAAAAAAAAAAAAAAAAAAAAAAAAA",
          "RequestId": "9a7a1615-3e09-4db2-8032-5c6f497f7e6a",
          "SessionId": "session-1234"
          }
          Audio对应的值为经过base64编码,
          RequestId为返回的唯一请求id,
          SessionId为发送请求时传入的id即uuid4()
        """

       # copyfile('voice.mp3', 'C:\\Users\\song\\Desktop')
        # content为base64解码后的二进制流
        content = b64decode(rep.Audio)
        # I/O操作
        with open(d, 'wb') as f:
            f.write(content)
    except TencentCloudSDKException as e:
        print(e)

if __name__ == '__main__':
    text = argv[1]
    voice_type = argv[2]
    lang_type = argv[3]
    voice_path = argv[4]
    biansheng(text, int(voice_type), int(lang_type), voice_path)
#    biansheng('黑夜给了我黑色的眼睛，我却用它寻找光明', 4, 1, 'voice.mp3')  # (文本，声音类型，语言类型（1-正文2-英文），输出路径)
# 0-云小宁，亲和女声(默认) 1-云小奇，亲和男声 2-云小晚，成熟男声 4-云小叶，温暖女声
# 5-云小欣，情感女声 6-云小龙，情感男声 7-云小曼，客服女声
# 1000-智侠，情感男声 1001-智瑜，情感女声 1002-智聆，通用女声
# 1003-智美，客服女声 1050-WeJack，英文男声 1051-WeRose，英文女声
