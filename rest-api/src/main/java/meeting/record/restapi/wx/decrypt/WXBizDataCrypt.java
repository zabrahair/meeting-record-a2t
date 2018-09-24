package meeting.record.restapi.wx.decrypt;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by deanwang on 2019/09/24.
 * <p>
 * AES-128-CBC 加密方式
 * 注：
 * AES-128-CBC可以自己定义“密钥”和“偏移量“。
 * AES-128是jdk自动生成的“密钥”。
 */
public class WXBizDataCrypt {


    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static WXBizDataCrypt instance = null;

    private WXBizDataCrypt() {

    }

    public static WXBizDataCrypt getInstance() {
        if (instance == null)
            instance = new WXBizDataCrypt();
        return instance;
    }

    /**
     * AES解密
     *
     * @param data           //密文，被加密的数据
     * @param key            //秘钥
     * @param iv             //偏移量
     * @param encodingFormat //解密后的结果需要进行的编码
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedData, String sessionKey, String iv, String encodingFormat) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] _encryptedData = base64Decoder.decodeBuffer(encryptedData);
            byte[] _sessionKey = base64Decoder.decodeBuffer(sessionKey);
            byte[] _iv = base64Decoder.decodeBuffer(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(_sessionKey, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(_iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] original = cipher.doFinal(_encryptedData);
            byte[] bytes = PKCS7Encoder.decode(original);
            String originalString = new String(bytes, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}