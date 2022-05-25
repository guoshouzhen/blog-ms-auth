package top.guoshouzhen.blog.blogmsauth.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件操作的工具类
 * @author shouzhen.guo
 * @date 2022/1/19 15:28
 */
@Slf4j
public class FileUtil {
    /**
     * 读取文件文本内容
     * @author guoshouzhen
     * @date 2021/11/21 15:52
     * @param fileName 文件名
     * @return java.lang.String
     */
    public static String readFileText(String fileName) throws Exception {
        File file = new File(fileName);
        Path path = file.toPath();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
        } catch (Exception ex) {
            log.error("读取文件内容失败，异常信息：" + ex);
            throw ex;
        }
        return sb.toString();
    }

    /**
     * 读取文件内容到字节流
     * @author shouzhen.guo
     * @date 2021/12/23 17:22
     * @param fileName 文件名
     * @return byte[]
     */
    public static byte[] readFile(String fileName) throws Exception{
        return Files.readAllBytes(new File(fileName).toPath());
    }

    /**
     * 将字节流写入指定文件
     * @author shouzhen.guo
     * @date 2021/12/23 17:43
     * @param destPath 目标文件
     * @param bytes 写入数据流
     */
    public static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }
}
