package sdu.aisubtitle.support.tencentai;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.facefusion.v20181201.FacefusionClient;
import com.tencentcloudapi.facefusion.v20181201.models.FaceFusionRequest;
import com.tencentcloudapi.facefusion.v20181201.models.FaceFusionResponse;

import java.io.*;
import java.util.Base64;

public class FaceFusion {

    /**
     * 将图片转换成base64编码
     *
     * @param imgPath 需要被编码的图片路径
     * @return 图片的base64编码
     * @author PY
     */
    public static String imgToBase64(String imgPath) {
        InputStream in = null;
        byte[] data = null;
        String encode = null;
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            encode = encoder.encodeToString(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return encode;
    }

    /**
     * 将图片的base64编码生成jpg文件
     *
     * @param imgData 图片的base64编码
     * @param imgPath 输出图片的路径
     * @return 解码是否成功
     * @author PY
     */
    public static boolean base64ToImg(String imgData, String imgPath) {
        if (imgData == null)
            return false;
        Base64.Decoder decoder = Base64.getDecoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgPath);
            byte[] b = decoder.decode(imgData);
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 人脸融合
     *
     * @param imgPath    用户的人脸图片
     * @param outputPath 融合后的人脸图片
     * @author PY
     */
    public static void facefusion(String imgPath, String outputPath) {

        try {

            Credential cred = new Credential(TencentaiInfo.secretId, TencentaiInfo.secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("facefusion.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            FacefusionClient client = new FacefusionClient(cred, "ap-beijing", clientProfile);

            String format = "{\"ProjectId\":\"303269\",\"ModelId\":\"qc_303269_330150_8\",\"Image\":\"%s\",\"RspImgType\":\"base64\"}";
            String params = String.format(format, imgToBase64(imgPath));
            FaceFusionRequest req = FaceFusionRequest.fromJsonString(params, FaceFusionRequest.class);

            FaceFusionResponse resp = client.FaceFusion(req);

            JSONObject jsonObject = JSONObject.parseObject(FaceFusionRequest.toJsonString(resp));
            base64ToImg(jsonObject.get("Image").toString(), outputPath);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

}
