package top.guoshouzhen.blog.blogmsauth.model.enums;

/**
 * 一共两个库，一个主库一个分库
 * 用户相关的博客数据、评论数据等按dbid保存在两个库中，dbid根据用户ID计算
 * 用户信息、用户权限信息、博客类别信息等数据保存在主库中
 * @author shouzhen.guo
 * @date 2022/3/4 17:42
 */
public enum DataSourceEnum {
    /**
     * 主库，
     */
    MASTER("blog1"),

    /**
     * 分库
     */
    SLAVE("blog2");

    private final String name;
    DataSourceEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
