package top.guoshouzhen.blog.blogmsauth.model.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import top.guoshouzhen.blog.blogmsauth.model.enums.ResultEnum;

/**
 * 定义接口返回值格式
 *
 * @author guoshouzhen
 * @date 2021/12/19 20:48
 */
@Data
// @JsonPropertyOrder(alphabetic = true) 按字典序排序
@JsonPropertyOrder({"result", "code", "message", "data"})
public class Result {
    private int result;
    private String code;
    private String message;
    private Object data;

    private Result() {
    }

    public <T> Result(int result, String code, String message, T data) {
        this.result = result;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return success(null);
    }

    public static <T> Result success(T data) {
        return new Result(ResultEnum.SUCCESS.getStatus(), ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    public static  Result fail() {
        return fail(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMessage(), null);
    }

    public static Result fail(String message) {
        return fail(ResultEnum.FAILED.getCode(), message, null);
    }

    public static Result fail(String code, String message) {
        return fail(code, message, null);
    }

    public static <T> Result fail(String code, String message, T data) {
        return new Result(ResultEnum.FAILED.getStatus(), code, message, data);
    }
}
