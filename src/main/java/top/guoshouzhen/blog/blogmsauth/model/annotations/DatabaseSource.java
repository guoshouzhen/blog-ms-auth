package top.guoshouzhen.blog.blogmsauth.model.annotations;


import top.guoshouzhen.blog.blogmsauth.model.enums.DataSourceEnum;

import java.lang.annotation.*;

/**
 * 自定义注解，用于切换数据源
 *
 * @author guoshouzhen
 * @date 2021/11/21 13:35
 */
@Target({ElementType.METHOD, ElementType.TYPE}) // 指定下面注解可以用于哪些程序单元
@Retention(RetentionPolicy.RUNTIME) //表示以下注解可以保留多长事件，RUNTIME表示编译器将注解留在class文件中，程序运行时，JVM可以获取注解信息，程序也可以通过反射获取注解信息
@Documented // 表示修饰的类被Javadoc工具提取为文档
@Inherited // 指定了以下注解具有继承性
public @interface DatabaseSource {
    DataSourceEnum value() default DataSourceEnum.MASTER;
}
