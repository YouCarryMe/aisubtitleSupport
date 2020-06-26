package sdu.aisubtitle.support;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
        String audioPath = "audios/en.mp3";
        String zhSubtitlePath = "subtitles/enSubtitle.srt";
        TencentAI.audio2zhSubtitle(pyFilePath, audioPath, zhSubtitlePath, "en");
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

    /**
     * 测试变声器
     */
    public static void testVoiceChanger() {
        String voicePath = "audios/audio.mp3";
        String outputPath = "audios/audio_luoli.mp3";
        int type = 1;
        MediaProcess.voiceChanger(voicePath, outputPath, type);
    }

    /**
     * 测试替换音频
     */
    public static void testReplaceAudio() {
        String videoPath = "videos/video.mp4";
        String audioPath = "audios/audio_luoli.mp3";
        String outputPath = "videos/video_luoli.mp4";
        MediaProcess.replaceAudio(videoPath, audioPath, outputPath);
    }

    /**
     * 测试从json生成srt
     */
    public static void testSubtitleJson2srt() {
        JSONArray subtitle = new JSONArray();
        JSONObject sub1 = new JSONObject();
        sub1.put("begin", "00:00:00,460");
        sub1.put("end", "00:00:03,210");
        JSONArray texts1 = new JSONArray();
        texts1.add("说一下。");
        texts1.add("Tell me.");
        sub1.put("texts", texts1);
        subtitle.add(sub1);
        JSONObject sub2 = new JSONObject();
        sub2.put("begin", "00:00:03,960");
        sub2.put("end", "00:00:07,230");
        JSONArray texts2 = new JSONArray();
        texts2.add("嗯，你们干什么。");
        texts2.add("Well, what are you doing.");
        sub2.put("texts", texts2);
        subtitle.add(sub2);
        System.out.println(subtitle);
        MediaProcess.subtitleJson2srt(subtitle, "subtitles/json2srt.srt");
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
//        testVoiceChanger();
//        testReplaceAudio();
        testSubtitleJson2srt();
    }
}
