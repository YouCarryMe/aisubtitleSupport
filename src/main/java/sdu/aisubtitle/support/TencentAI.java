package sdu.aisubtitle.support;

import sdu.aisubtitle.support.tencentai.FaceFusion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TencentAI {

    private static String pythonExe = "python";

    /**
     * 根据不同的系统，更换python执行器
     *
     * @param str
     * @author PY
     */
    public void setPythonExe(String str) {
        pythonExe = str;
    }

    /**
     * 人脸融合
     *
     * @param imgPath    用户人脸
     * @param outputPath 融合后的人脸
     */
    public static void facefusion(String imgPath, String outputPath) {
        FaceFusion.facefusion(imgPath, outputPath);
    }

    /**
     * 音频转为字幕
     *
     * @param pyFilePath     python文件路径
     * @param audioPath      输入音频文件的路径
     * @param zhSubtitlePath 输入字幕文件的路径
     * @author PY
     */
    public static void audio2zhSubtitle(String pyFilePath, String audioPath, String zhSubtitlePath) {
        List<String> commList = new ArrayList<>(Arrays.asList(pythonExe, pyFilePath, audioPath, zhSubtitlePath));
        ExecuteCommand.exec(commList);
    }

    /**
     * 翻译字幕
     *
     * @param pyFilePath        python文件路径
     * @param subtitlePath      输入的字幕文件路径
     * @param transSubtitlePath 翻译后字幕文件的路径
     * @param source            源语言
     * @param target            目标语言
     * @author PY
     */
    public static void translate(String pyFilePath, String subtitlePath, String transSubtitlePath, String source, String target) {
        List<String> commList = new ArrayList<>(Arrays.asList(pythonExe, pyFilePath, subtitlePath, transSubtitlePath, source, target));
        ExecuteCommand.exec(commList);
    }

    /**
     * 合并字幕文件
     *
     * @param pyFilePath         python文件路径
     * @param zhSubtitlePath     中文字幕文件路径
     * @param enSubtitlePath     英文字幕文件路径
     * @param mergedSubtitlePath 合成后的字幕文件路径
     * @author PY
     */
    public static void mergeSubtitle(String pyFilePath, String zhSubtitlePath, String enSubtitlePath, String mergedSubtitlePath) {
        List<String> commList = new ArrayList<>(Arrays.asList(pythonExe, pyFilePath, zhSubtitlePath, enSubtitlePath, mergedSubtitlePath));
        ExecuteCommand.exec(commList);
    }

}
