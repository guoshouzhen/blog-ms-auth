package top.guoshouzhen.blog.blogmsauth.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES 工具类 TODO 重新实现
 * @author guoshouzhen
 * @date 2021/12/9 23:48
 */
@Slf4j
public class AesUtil {
    /**
     * 算法/模式/补码方式
     */
    private final static String KEY_ALGORITHMS = "AES/ECB/PKCS5Padding";
    private final static String CHARSET = "utf-8";
    /**
     * 密钥长度限制
     */
    private final static int SECRET_SIZE = 16;

    /**
     * 默认密钥，此处使用AES-128-ECB加密模式，key需要为16位
     */
    private static final String SECRET = "45041f697b78804a";

    /**
     * AES加密
     *
     * @param sSrc 待加密字符串
     * @param sKey 密钥
     * @return java.lang.String
     * @author guoshouzhen
     * @date 2021/12/11 0:10
     */
    public static String encrypt(String sSrc, String sKey) {
        if (sKey == null) {
            sKey = SECRET;
        }
        try {
            // 判断Key是否为16位
            if (sKey.length() != SECRET_SIZE) {
                throw new Exception("密钥长度不是16位");
            }
            byte[] raw = sKey.getBytes(CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            log.error("AES加密异常：", ex);
            return null;
        }
    }

    public static String encrypt(String data) {
        return encrypt(data, SECRET);
    }

    /**
     * AES解密
     *
     * @param sSrc 待解密字符串
     * @param sKey 密钥
     * @return java.lang.String
     * @author guoshouzhen
     * @date 2021/12/11 0:17
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                throw new Exception("Key为空null");
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                throw new Exception("Key长度不是16位");
            }
            byte[] raw = sKey.getBytes(CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, CHARSET);
        } catch (Exception ex) {
            log.error("AES解密异常：", ex);
            return null;
        }
    }

    public static String decrypt(String sSrc) {
        return decrypt(sSrc, SECRET);
    }
}