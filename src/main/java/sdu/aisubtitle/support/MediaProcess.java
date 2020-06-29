package sdu.aisubtitle.support;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import sdu.aisubtitle.support.voicechanger.SoundEnum;

import java.io.*;
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
     * @param videoPath  视频路径（会在视频同级目录生成一个封面图片，名称和视频名一样，格式是jpg）
     * @param option     选项：为“time”时是从视频中抽取一帧作为封面，为“picture”时是使用用户给定的图片路径作为封面
     * @param params     当option为“time”时，输入格式为“00:00:00”，表示截取某一帧；当option为“picture”时，是图片路径
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

    /**
     * 变声器（输入和输出只支持MP3格式）
     *
     * @param voicePath  原始音频路径
     * @param outputPath 输出音频路径
     * @param type       声音类别（1：萝莉；2：大叔；3：肥仔；4：熊孩子；5：困兽；6：重机械；7：感冒；8：空灵）
     */
    public static void voiceChanger(final String voicePath, final String outputPath, final int type) {
        SoundEnum soundEnum;
        switch (type) {
            case 1:
                soundEnum = SoundEnum.LUOLI;
                break;
            case 2:
                soundEnum = SoundEnum.DASHU;
                break;
            case 3:
                soundEnum = SoundEnum.FEIZAI;
                break;
            case 4:
                soundEnum = SoundEnum.XIONGHAIZI;
                break;
            case 5:
                soundEnum = SoundEnum.KUNSHOU;
                break;
            case 6:
                soundEnum = SoundEnum.ZHONGJIXIE;
                break;
            case 7:
                soundEnum = SoundEnum.GANMAO;
                break;
            case 8:
                soundEnum = SoundEnum.KONGLING;
                break;
            default:
                soundEnum = SoundEnum.LUOLI;
                break;
        }
        byte[] pcmBytes = soundEnum.run(voicePath);
        byte[] wavHeader = SoundEnum.pcm2wav(pcmBytes);
        try {
            String[] div = outputPath.split("\\.");
            String wavPath = "";
            for (int i = 0; i < div.length - 1; i++)
                wavPath += div[i] + ".";
            wavPath += "wav";
            OutputStream wavOutput = new FileOutputStream(wavPath);
            try {
                wavOutput.write(wavHeader);
                wavOutput.write(pcmBytes);
                wavOutput.flush();
                wavHeader.clone();
            } catch (IOException e) {
                e.printStackTrace();
            }
            wav2mp3(wavPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * wav格式音频转换为mp3格式。会在同目录下生成名字相同的“.mp3”后缀的音频文件
     *
     * @param audioPath wav音频文件的路径
     * @return
     */
    public static Boolean wav2mp3(String audioPath) {
        List<String> globals = new ArrayList<>();
        List<String> input1Opts = new ArrayList<>();
        Map<String, List<String>> inputs = new HashMap<>();
        inputs.put(audioPath, input1Opts);
        List<String> outputOpts = new ArrayList<>(Arrays.asList("-acodec", "libmp3lame", "-y"));
        Map<String, List<String>> outputs = new HashMap<>();
        String[] div = audioPath.split("\\.");
        String outputPath = "";
        for (int i = 0; i < div.length - 1; i++)
            outputPath += div[i] + ".";
        outputPath += "mp3";
        outputs.put(outputPath, outputOpts);
        FFmpegJ ff = new FFmpegJ(globals, inputs, outputs);
        System.out.println(ff.cmd());
        return ff.run();
    }

    /**
     * 替换视频中声音（当然前题是保证时长一致，这里主要使用原视频变声之后的音频文件替换原始视频中的声音）
     *
     * @param videoPath  视频路径
     * @param audioPath  变声后的音频文件
     * @param outputPath 输出路径
     * @return
     */
    public static Boolean replaceAudio(final String videoPath, final String audioPath, final String outputPath) {
        List<String> globals = new ArrayList<>();
        List<String> input1Opts = new ArrayList<>();
        List<String> input2Opts = new ArrayList<>();
        Map<String, List<String>> inputs = new HashMap<>();
        inputs.put(videoPath, input1Opts);
        inputs.put(audioPath, input2Opts);
        List<String> outputOpts = new ArrayList<>(Arrays.asList("-c:v", "copy", "-c:a", "aac", "-strict", "experimental", "-map", "0:v:0", "-map", "1:a:0", "-y"));
        Map<String, List<String>> outputs = new HashMap<>();
        outputs.put(outputPath, outputOpts);
        FFmpegJ ff = new FFmpegJ(globals, inputs, outputs);
        System.out.println(ff.cmd());
        return ff.run();
    }

    /**
     * json格式的字幕转srt格式并保存
     *
     * @param subtitle   表示字幕的json数组
     * @param outputPath 输出路径
     */
    public static void subtitleJson2srt(final JSONArray subtitle, final String outputPath) {
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < subtitle.size(); i++) {
            JSONObject temp = subtitle.getJSONObject(i);
            content.append("" + (i + 1) + "\n");
            String begin = temp.getString("begin"), end = temp.getString("end");
            content.append(begin + " --> " + end + "\n");
            JSONArray texts = temp.getJSONArray("texts");
            for (int j = 0; j < texts.size(); j++) {
                content.append(texts.getString(j) + "\n");
            }
            content.append("\n");
        }
        File file = new File(outputPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content.toString());
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * srt格式字幕转json格式并返回数据
     *
     * @param inputPath srt字幕路径
     * @throws IOException
     */
    public static String subtitleSrt2json(final String inputPath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
        String indexline, timeline, textline_zh, textline_en, kongline;
        JSONArray subtitle = new JSONArray();
        while ((indexline = reader.readLine()) != null && (timeline = reader.readLine()) != null &&
                (textline_zh = reader.readLine()) != null && (textline_en = reader.readLine()) != null && (kongline = reader.readLine()) != null) {
            String rebegin = "(.*?) -->";
            String reend = "(.*?)(--> )(.*)";
            List<String> list = new ArrayList<String>();
            List<String> extvounoLists = new ArrayList<String>();
            Pattern pabegin = Pattern.compile(rebegin);
            Pattern paend = Pattern.compile(reend);
            Matcher mbegin = pabegin.matcher(timeline);
            Matcher mend = paend.matcher(timeline);
            JSONObject subs = new JSONObject();
            while (mend.find()) {
                int i = 1;
                list.add(mend.group(i));
                subs.put("end", mend.group(i + 2));
                i++;
            }
            while (mbegin.find()) {
                int i = 1;
                list.add(mbegin.group(i));
                subs.put("begin", mbegin.group(i));
                i++;
            }
            JSONArray texts = new JSONArray();
            texts.add(textline_zh);
            texts.add(textline_en);
            subs.put("texts", texts);
            subtitle.add(subs);
        }
        return JSONObject.toJSONString(subtitle);
    }

    /**
     * 根据时间戳生成图片
     *
     * @param videoPath 视频路径
     * @param pagePath  图片路径
     * @param time      时间
     * @return
     */
    public static Boolean generateCoverPage(final String videoPath, final String pagePath, final String time) {
        List<String> globals = new ArrayList<>();
        List<String> input1Opts = new ArrayList<>(Arrays.asList("-ss", time));
        Map<String, List<String>> inputs = new HashMap<>();
        inputs.put(videoPath, input1Opts);
        List<String> outputOpts = new ArrayList<>(Arrays.asList("-vframes", "1", "-q:v", "2", "-y"));
        Map<String, List<String>> outputs = new HashMap<>();
        outputs.put(pagePath, outputOpts);
        FFmpegJ ff = new FFmpegJ(globals, inputs, outputs);
        System.out.println(ff.cmd());
        return ff.run();
    }

}
