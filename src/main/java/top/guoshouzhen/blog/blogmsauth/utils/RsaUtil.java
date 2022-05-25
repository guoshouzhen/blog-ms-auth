package top.guoshouzhen.blog.blogmsauth.utils;


import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description RSA加密相关
 * @date 2021/12/23 17:16
 */
public class RsaUtil {
    private static final int DEFAULT_KEY_SIZE = 2048;
    private static final String ALG = "RSA";
    private static final String SECRET = "Sh9NH4OIAR9YlyIU";

    /**
     * 从文件中读取公钥
     *
     * @param fileName 文件名（公钥保存路径）
     * @return java.security.PublicKey
     * @author shouzhen.guo
     * @date 2021/12/23 17:24
     */
    public static PublicKey getPublicKey(String fileName) throws Exception {
        byte[] bytes = FileUtil.readFile(fileName);
        return getPublicKey(bytes);
    }

    /**
     * 从文件中读取私钥
     *
     * @param fileName 文件名（公钥保存路径）
     * @return java.security.PrivateKey
     * @author shouzhen.guo
     * @date 2021/12/23 17:26
     */
    public static PrivateKey getPrivateKey(String fileName) throws Exception {
        byte[] bytes = FileUtil.readFile(fileName);
        return getPrivateKey(bytes);
    }

    /**
     * 获取公钥
     *
     * @param bytes bytes
     * @return java.security.PublicKey
     * @author shouzhen.guo
     * @date 2021/12/23 17:38
     */
    private static PublicKey getPublicKey(byte[] bytes) throws Exception {
        bytes = Base64.getDecoder().decode(bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory fc = KeyFactory.getInstance(ALG);
        return fc.generatePublic(spec);
    }

    /**
     * 获取私钥
     *
     * @param bytes bytes
     * @return java.security.PrivateKey
     * @author shouzhen.guo
     * @date 2021/12/23 17:41
     */
    private static PrivateKey getPrivateKey(byte[] bytes) throws Exception {
        bytes = Base64.getDecoder().decode(bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory fc = KeyFactory.getInstance(ALG);
        return fc.generatePrivate(spec);
    }

    /**
     * 根据密文，生存rsa公钥和私钥,并写入指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     * @author shouzhen.guo
     * @date 2021/12/23 17:44
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG);
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(DEFAULT_KEY_SIZE, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
        FileUtil.writeFile(publicKeyFilename, publicKeyBytes);
        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
        FileUtil.writeFile(privateKeyFilename, privateKeyBytes);
    }

    /**
     * 生成RSA密钥，重载
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @author shouzhen.guo
     * @date 2021/12/24 12:33
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename) throws Exception {
        generateKey(publicKeyFilename, privateKeyFilename, SECRET);
    }
}
