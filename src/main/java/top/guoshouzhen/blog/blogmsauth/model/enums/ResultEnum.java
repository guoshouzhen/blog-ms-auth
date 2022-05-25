package top.guoshouzhen.blog.blogmsauth.model.enums;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 接口返回结果状态枚举定义
 * @date 2022/1/20 10:25
 */
public enum ResultEnum {
    /**
     * 操作成功枚举
     */
    SUCCESS(1, "200", "操作成功"),
    /**
     * 操作失败枚举
     */
    FAILED(0, "500", "操作失败");

    private final int status;
    private final String code;
    private final String message;

    ResultEnum(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
