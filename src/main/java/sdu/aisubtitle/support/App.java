package sdu.aisubtitle.support;

import sdu.aisubtitle.support.tencentai.FaceFusion;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {

    /**
     * 测试获取视频信息
     */
    public static void testGetInfo() {
        String filePath = "videos/video.mp4";
        System.out.println(MediaProcess.getTimeLen(filePath));
        System.out.println(MediaProcess.getBitrate(filePath));
        System.out.println(MediaProcess.getSize(filePath));
        System.out.println(MediaProcess.getFormat(filePath));
    }

    /**
     * 测试压缩视频
     */
    public static void testCompressVideo() {
        String filePath = "videos/video.mp4";
        String outputPath = "videos/video_comporessed.mp4";
        System.out.println(MediaProcess.compressVideo(filePath, outputPath, 600));
    }

    /**
     * 测试导出音频
     */
    public static void testExportAudio() {
        String filePath = "videos/video.mp4";
        String outputPath = "audios/audio.mp3";
        System.out.println(MediaProcess.exportAudio(filePath, outputPath));
    }

    /**
     * 测试导入字幕
     */
    public static void testImportSubtitle() {
        String filePath = "videos/video.mp4";
        String subtitlePath = "subtitles/subtitles.srt";
        String outputPath = "videos/video_subtitle.mp4";
        System.out.println(MediaProcess.importSubtitle(filePath, subtitlePath, outputPath));
    }

    /**
     * 测试自定义封面
     */
    public static void testUpdateCoverPage() {
        String pyFilePath = "python/update_cover_page.py";
        String videoPath = "videos/video.mp4";
        String option = "time";
        String params = "00:00:09";
        String outputPath = "videos/video_coverpage.mp4";
        MediaProcess.updateCoverPage(pyFilePath, videoPath, option, params, outputPath);
    }

    /**
     * 测试人脸融合
     */
    public static void testFaceFusion() {
        String imgPath = "imgs/baby.jpg";
//        String imgPath = "imgs/Aragaki.jpg";
//        String outputPath = "imgs/fusion.jpg";
//        String outputPath = "imgs/fusion1.jpg";
        String outputPath = "imgs/fusion2.jpg";
        TencentAI.facefusion(imgPath, outputPath, "qc_303269_803589_6");
    }

    public static void testGetDescribeMaterialList() {
        System.out.println(TencentAI.getDescribeMaterialList());
    }

    /**
     * 测试音频转字幕
     */
    public static void testAudio2zhSubtitle() {
        String pyFilePath = "python/audio2zhSubtitle.py";
        String audioPath = "audios/audio.aac";
        String zhSubtitlePath = "subtitles/zhSubtitle.srt";
        TencentAI.audio2zhSubtitle(pyFilePath, audioPath, zhSubtitlePath);
    }

    /**
     * 测试翻译字幕
     */
    public static void testTranslate() {
        String pyFilePath = "python/demo_translate.py";
        String subtitlePath = "subtitles/zhSubtitle.srt";
        String transSubtitlePath = "subtitles/enSubtitle.srt";
        String source = "zh";
        String target = "en";
        TencentAI.translate(pyFilePath, subtitlePath, transSubtitlePath, source, target);
    }

    /**
     * 测试合并字幕
     */
    public static void testMergeSubtitle() {
        String pyFilePath = "python/demo_merge.py";
        String zhSubtitlePath = "subtitles/zhSubtitle.srt";
        String enSubtitlePath = "subtitles/enSubtitle.srt";
        String mergedSubtitlePath = "subtitles/mergedSubtitle.srt";
        TencentAI.mergeSubtitle(pyFilePath, zhSubtitlePath, enSubtitlePath, mergedSubtitlePath);
    }

    /**
     * 测试语音合成
     */
    public static void testTextToVoice() {
        String pyFilePath = "python/text2voice.py";
        String text = "黑夜给了我黑色的眼睛，我却用它寻找光明。";
        String voiceType = "4";
        String langType = "1";
        String outputPath = "audios/voice.mp3";
        TencentAI.textToVoice(pyFilePath, text, voiceType, langType, outputPath);
    }

    public static void main(String[] args) {
//        testGetInfo();
//        testCompressVideo();
//        testExportAudio();
//        testImportSubtitle();
//        testUpdateCoverPage();
//        testGetDescribeMaterialList();
//        testFaceFusion();
//        testAudio2zhSubtitle();
//        testTranslate();
//        testMergeSubtitle();
//        testTextToVoice();
    }
}
