package top.guoshouzhen.blog.blogmsauth.controller.innerapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.guoshouzhen.blog.blogmsauth.model.vo.Result;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/6/1 16:14
 */
@RestController
@RequestMapping("api/innerapi")
public class DemoController {
    @PostMapping("/demo")
    public Result getToken() {
        return Result.success();
    }
}
