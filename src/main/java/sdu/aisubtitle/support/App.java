package sdu.aisubtitle.support;

import sdu.aisubtitle.support.tencentai.FaceFusion;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {

    public static void testFaceFusion() {
        FaceFusion.facefusion("target/classes/imgs/baby.jpg", "target/classes/imgs/fusion.jpg");
    }

    public static void testGetInfo() {
        String filePath = "target/classes/videos/video.mp4";
        System.out.println(MediaProcess.getTimeLen(filePath));
        System.out.println(MediaProcess.getBitrate(filePath));
        System.out.println(MediaProcess.getSize(filePath));
        System.out.println(MediaProcess.getFormat(filePath));
    }

    public static void testCompressVideo() {
        String filePath = "target/classes/videos/video.mp4";
        String outputPath = "target/classes/videos/video_comporessed.mp4";
        try {
            System.out.println(MediaProcess.compressVideo(filePath, outputPath, 600));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testExportAudio() {
        String filePath = "target/classes/videos/video.mp4";
        String outputPath = "target/classes/audios/audio.aac";
        try {
            System.out.println(MediaProcess.exportAudio(filePath, outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testImportSubtitle() {
        String filePath = "target/classes/videos/video.mp4";
        String subtitlePath = "target/classes/subtitles/subtitles.srt";
        String outputPath = "target/classes/videos/video_subtitle.mp4";
        try {
            System.out.println(MediaProcess.importSubtitle(filePath, subtitlePath, outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        testGetInfo();
//        testCompressVideo();
//        testExportAudio();
        testImportSubtitle();
    }
}
