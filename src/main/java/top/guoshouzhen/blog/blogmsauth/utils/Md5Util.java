package top.guoshouzhen.blog.blogmsauth.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5加密/验证工具类
 * 使用小写的MD5
 *
 * @author guoshouzhen
 * @date 2021/12/9 23:22
 */
@Slf4j
public class Md5Util {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String KEY_ALGORITHMS = "MD5";

    /**
     * 对字符串 MD5 无盐值加密
     *
     * @param plainText 要加密的字符串
     * @return java.lang.String MD5加密后生成32位(小写字母 + 数字)字符串
     * @author guoshouzhen
     * @date 2021/12/9 23:23
     */
    public static String md5Lower(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance(KEY_ALGORITHMS);

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception ex) {
            log.error("MD5加密异常：", ex);
            return null;
        }
    }

    /**
     * 对字符串 MD5 无盐值加密
     *
     * @param plainText 要加密的字符串
     * @return java.lang.String MD5加密后生成32位(大写字母 + 数字)字符串
     * @author guoshouzhen
     * @date 2021/12/9 23:26
     */
    public static String md5Upper(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance(KEY_ALGORITHMS);

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : mdResult) {
                // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception ex) {
            log.error("MD5加密异常：", ex);
            return null;
        }
    }

//    // 测试
//    public static void main(String[] args) {
//        String plainText = "guoshouzhen";
//        String saltValue = "2333";
//
//        System.out.println(md5Lower(plainText));
//        System.out.println(md5Upper(plainText));
//
//    }
}