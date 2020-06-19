package sdu.aisubtitle.support;

import sdu.aisubtitle.support.tencentai.FaceFusion;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {

    public static void testGetInfo() {
        String filePath = "videos/video.mp4";
        System.out.println(MediaProcess.getTimeLen(filePath));
        System.out.println(MediaProcess.getBitrate(filePath));
        System.out.println(MediaProcess.getSize(filePath));
        System.out.println(MediaProcess.getFormat(filePath));
    }

    public static void testCompressVideo() {
        String filePath = "videos/video.mp4";
        String outputPath = "videos/video_comporessed.mp4";
        try {
            System.out.println(MediaProcess.compressVideo(filePath, outputPath, 600));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testExportAudio() {
        String filePath = "videos/video.mp4";
        String outputPath = "audios/audio.aac";
        try {
            System.out.println(MediaProcess.exportAudio(filePath, outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testImportSubtitle() {
        String filePath = "videos/video.mp4";
        String subtitlePath = "subtitles/subtitles.srt";
        String outputPath = "videos/video_subtitle.mp4";
        try {
            System.out.println(MediaProcess.importSubtitle(filePath, subtitlePath, outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testFaceFusion() {
//        String imgPath = "imgs/baby.jpg";
        String imgPath = "imgs/Aragaki.jpg";
//        String outputPath = "imgs/fusion.jpg";
        String outputPath = "imgs/fusion1.jpg";
        TencentAI.facefusion(imgPath, outputPath);
    }

    public static void testAudio2zhSubtitle() {
        String pyFilePath = "python/audio2zhSubtitle.py";
        String audioPath = "audios/audio.aac";
        String zhSubtitlePath = "subtitles/zhSubtitle.srt";
        TencentAI.audio2zhSubtitle(pyFilePath, audioPath, zhSubtitlePath);
    }

    public static void testTranslate() {
        String pyFilePath = "python/demo_translate.py";
        String subtitlePath = "subtitles/zhSubtitle.srt";
        String transSubtitlePath = "subtitles/enSubtitle.srt";
        String source = "zh";
        String target = "en";
        TencentAI.translate(pyFilePath, subtitlePath, transSubtitlePath, source, target);
    }

    public static void testMergeSubtitle() {
        String pyFilePath = "python/demo_merge.py";
        String zhSubtitlePath = "subtitles/zhSubtitle.srt";
        String enSubtitlePath = "subtitles/enSubtitle.srt";
        String mergedSubtitlePath = "subtitles/mergedSubtitle.srt";
        TencentAI.mergeSubtitle(pyFilePath, zhSubtitlePath, enSubtitlePath, mergedSubtitlePath);
    }

    public static void main(String[] args) {
//        testGetInfo();
//        testCompressVideo();
//        testExportAudio();
//        testImportSubtitle();
//        testFaceFusion();
//        testAudio2zhSubtitle();
//        testTranslate();
//        testMergeSubtitle();
    }
}
