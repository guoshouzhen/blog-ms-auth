package top.guoshouzhen.blog.blogmsauth.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 日期转换工具类
 * @author guoshouzhen
 * @date 2021/12/23 21:48
 */
public class DateUtil {
    /**
     * 将传入的LocalDateTime对象转为Date
     * @author guoshouzhen
     * @date 2021/12/23 21:50
     * @param localDateTime localDateTime
     * @return java.util.Date
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());

    }
}
