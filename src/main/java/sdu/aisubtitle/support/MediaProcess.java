package sdu.aisubtitle.support;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaProcess {

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
     * 根据时间字符串获得时长 格式：00:00:00,000
     *
     * @param time 时间字符串
     * @return 时长（秒为单位）
     * @author PY
     */
    private static double getTimelen(String time) {
        double sec = 0;
        String strs[] = time.split(":");
        if (strs[0].compareTo("0") > 0) {
            sec += Double.valueOf(strs[0]) * 60 * 60;
        }
        if (strs[1].compareTo("0") > 0) {
            sec += Double.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            sec += Double.valueOf(strs[2]);
        }
        return sec;
    }

    /**
     * 获得视频或者音频文件的时长
     *
     * @param filePath 文件路径
     * @return 时长（秒为单位）
     * @author PY
     */
    public static double getTimeLen(String filePath) {
        List<String> commList = new ArrayList<>(Arrays.asList("ffmpeg", "-i", filePath));
        String res = ExecuteCommand.exec(commList);

        String regexDuration = "Duration: (.*?),";
        Pattern pattern = Pattern.compile(regexDuration);
        Matcher m = pattern.matcher(res);
        double timeLen = 0.0;
        if (m.find()) {
            timeLen = getTimelen(m.group(1));
        }
        return timeLen;
    }

    /**
     * 获得视频或音频的比特率
     *
     * @param filePath 文件路径
     * @return 比特率（kb/s为单位）
     * @author PY
     */
    public static int getBitrate(String filePath) {
        List<String> commList = new ArrayList<>(Arrays.asList("ffmpeg", "-i", filePath));
        String res = ExecuteCommand.exec(commList);

        String regexBitrate = ", bitrate: (\\d*) kb\\/s";
        Pattern pattern = Pattern.compile(regexBitrate);
        Matcher m = pattern.matcher(res);
        int bitrate = 0;
        if (m.find()) {
            bitrate = Integer.valueOf(m.group(1));
        }
        return bitrate;
    }

    /**
     * 获得文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（Byte为单位）
     * @author PY
     */
    public static long getSize(String filePath) {
        File f = new File(filePath);
        long size = 0;
        if (f.exists() && f.isFile()) {
            size = f.length();
        }
        return size;
    }

    /**
     * 获得文件的格式
     *
     * @param filePath 文件路径
     * @return 文件格式
     * @author PY
     */
    public static String getFormat(String filePath) {
        String[] temp = filePath.split("\\.");
        String format = temp[temp.length - 1];
        return format;
    }

    /**
     * 压缩视频
     *
     * @param videoPath           被压缩视频的路径
     * @param compressedVideoPath 压缩后视频的路径
     * @param b                   压缩后的比特率
     * @return 是否成功
     * @throws IOException
     * @throws InterruptedException
     * @author PY
     */
    public static Boolean compressVideo(final String videoPath, final String compressedVideoPath, final int b) {
        List<String> globals = new ArrayList<>();
        List<String> input1Opts = new ArrayList<>();
        Map<String, List<String>> inputs = new HashMap<>();
        inputs.put(videoPath, input1Opts);
        List<String> outputOpts = new ArrayList<>(Arrays.asList("-b", "" + b + "k", "-y"));
        Map<String, List<String>> outputs = new HashMap<>();
        outputs.put(compressedVideoPath, outputOpts);
        FFmpegJ ff = new FFmpegJ(globals, inputs, outputs);
        System.out.println(ff.cmd());
        return ff.run();
    }

    /**
     * 导出音频
     *
     * @param videoPath 视频路径
     * @param audioPath 音频路径
     * @return 是否成功
     * @throws IOException
     * @throws InterruptedException
     * @author PY
     */
    public static Boolean exportAudio(final String videoPath, final String audioPath) {
        List<String> globals = new ArrayList<>();
        List<String> input1Opts = new ArrayList<>();
        Map<String, List<String>> inputs = new HashMap<>();
        inputs.put(videoPath, input1Opts);
        List<String> outputOpts = new ArrayList<>(Arrays.asList("-f", "mp3", "-y"));
        Map<String, List<String>> outputs = new HashMap<>();
        outputs.put(audioPath, outputOpts);
        FFmpegJ ff = new FFmpegJ(globals, inputs, outputs);
        System.out.println(ff.cmd());
        return ff.run();
    }

    /**
     * 导入字幕
     *
     * @param videoPath             视频路径
     * @param subtitlePath          字幕路径
     * @param videoWithSubtitlePath 导入路径后的视频路径
     * @return 是否成功
     * @throws IOException
     * @throws InterruptedException
     * @author PY
     */
    public static Boolean importSubtitle(final String videoPath, final String subtitlePath, final String videoWithSubtitlePath) {
        List<String> globals = new ArrayList<>();
        List<String> input1Opts = new ArrayList<>();
        Map<String, List<String>> inputs = new HashMap<>();
        inputs.put(videoPath, input1Opts);
        List<String> outputOpts = new ArrayList<>(Arrays.asList("-vf", "subtitles=" + subtitlePath, "-y"));
        Map<String, List<String>> outputs = new HashMap<>();
        outputs.put(videoWithSubtitlePath, outputOpts);
        FFmpegJ ff = new FFmpegJ(globals, inputs, outputs);
        System.out.println(ff.cmd());
        return ff.run();
    }

    /**
     * 自定义视频封面
     *
     * @param pyFilePath python文件路径
     * @param videoPath 视频路径（会在视频同级目录生成一个封面图片，名称和视频名一样，格式是jpg）
     * @param option 选项：为“time”时是从视频中抽取一帧作为封面，为“picture”时是使用用户给定的图片路径作为封面
     * @param params 当option为“time”时，输入格式为“00:00:00”，表示截取某一帧；当option为“picture”时，是图片路径
     * @param outputPath 自定义完封面的视频路径
     * @return
     */
    public static Boolean updateCoverPage(final String pyFilePath, final String videoPath, final String option, final String params, final String outputPath) {
        List<String> commList = new ArrayList<>(Arrays.asList(pythonExe, pyFilePath, videoPath, option, params, outputPath));
        String res = ExecuteCommand.exec(commList);
        String regexExitCode = "exitCode = (\\d+);";
        Pattern pattern = Pattern.compile(regexExitCode);
        Matcher m = pattern.matcher(res);
        int exitCode = 1;
        if (m.find()) {
            exitCode = Integer.valueOf(m.group(1));
        }
        return exitCode == 0 ? true : false;
    }

}
