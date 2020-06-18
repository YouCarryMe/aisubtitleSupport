package sdu.aisubtitle.support;

import sdu.aisubtitle.support.tencentai.FaceFusion;

/**
 * Hello world!
 */
public class App {

    public static void testFaceFusion() {
        FaceFusion.facefusion("D:/Projects/aisubtitleSupport/src/main/resources/imgs/baby.jpg", "D:/Projects/aisubtitleSupport/src/main/resources/imgs/fusion.jpg");
    }

    public static void testMediaProcess() {
        String filePath = "D:/Projects/aisubtitleSupport/src/main/resources/videos/video.mp4";
        System.out.println(MediaProcess.getTimeLen(filePath));
        System.out.println(MediaProcess.getBitrate(filePath));
        System.out.println(MediaProcess.getSize(filePath));
    }

    public static void main(String[] args) {
//        testFaceFusion();
        testMediaProcess();
    }
}
