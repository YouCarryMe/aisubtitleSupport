# AI-subtitle support 

本仓库提供了视频音频处理的相关支持。

## 文件简介

+ App.java：测试代码
+ ExecuteCommand.java：执行命令行
+ FFmpegJ.java：调用ffmpeg
+ MediaProcess.java：处理多媒体文件
    + 获取文件信息（大小、时长、格式、比特率）
    + 视频压缩
    + 音频导出
    + 字幕导入
    + 视频封面
    + 变声器
    + wav转mp3
    + 替换音频
    + json格式字幕数据转srt
+ TencentAI.java：使用腾讯ai平台开发的功能
    + 语音识别导出字幕
    + 字幕翻译
    + 字幕合并
    + 人脸融合
    + 获取人脸融合素材列表
    + 语音合成
+ resources文件夹中是一些静态资源和调用到的python脚本