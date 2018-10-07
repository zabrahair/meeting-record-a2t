package meeting.record.restapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import meeting.record.restapi.wx.decrypt.WXBizDataCrypt;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

@RestController
public class WeChatUserInfoController {

//    @RequestMapping(value="/", method = RequestMethod.GET)
    @PostMapping(value="/wechat/getUserInfo")
    public @ResponseBody
    String index(@RequestParam("wxcode") String wxcode, @RequestParam("wxiv")String wxiv, @RequestParam("wxdata") String wxdata) {
        System.out.println("wxcode: " +  wxcode);
        System.out.println("wxiv: " +  wxiv);
        System.out.println("wxdata: " +  wxdata);
        String data = wxdata;
        String key = "wx11edd7f03d34623d";
        String iv = wxiv;
        String encodingFormat = "UTF-8";
        String resultObject = "{}";
//        String tempCode = "071YVi0Y1KEvtT0hsRZX1rzu0Y1YVi09";
        String tempCode = wxcode;
        System.out.println("tempCode: " + tempCode);
        JSONObject sessionKeyObject = getSessionKey(tempCode);
        System.out.println("object: " + sessionKeyObject.toJSONString());

        String openId = sessionKeyObject.getString("openid");
        String sessionKey = sessionKeyObject.getString("session_key");
        Integer expiresIn = sessionKeyObject.getInteger("expires_in");

        System.out.println("openId: " + openId);
        System.out.println("sessionKey: " + sessionKey);
        System.out.println("expiresIn: " + expiresIn.toString());
        key = sessionKey;
        try {
            // method 1
//            resultObject =  getUserInfo(data, key, iv);
//            System.out.println("PhoneNumber:" + resultObject);

            // method 2
            String deString = WXBizDataCrypt.getInstance().decrypt(data, key, iv, "UTF-8");
            System.out.println("PhoneNumber:" + deString);
            resultObject = deString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;
    }

    public JSONObject getSessionKey(String tempCode){
        //微信端登录code
        String wxCode = tempCode;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> requestUrlParam = new HashMap<String, String>(  );
        requestUrlParam.put( "appid","wx11edd7f03d34623d" );//小程序appId
        requestUrlParam.put( "secret","e3f206db167487e048f32586e5337ba7" );
        requestUrlParam.put( "js_code",wxCode );//小程序端返回的code
        requestUrlParam.put( "grant_type","authorization_code" );//默认参数

        //发送post请求读取调用微信接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject( sendPost( requestUrl,requestUrlParam ));
        return jsonObject;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String getUserInfo(String encryptedData, String sessionKey, String iv) throws IOException {
        // 被加密的数据
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] dataByte = base64Decoder.decodeBuffer(encryptedData);
        // 加密秘钥
        byte[] keyByte = base64Decoder.decodeBuffer(sessionKey);
        // 偏移量
        byte[] ivByte = base64Decoder.decodeBuffer(iv);
        try {

            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "utf-8");
                System.out.println("result:" + result);
                return result;
//                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
