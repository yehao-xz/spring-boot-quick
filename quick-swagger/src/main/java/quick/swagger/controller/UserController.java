package quick.swagger.controller;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quick.swagger.entity.User;

import java.util.List;

/**
 * 用户管理
 *
 * @author yehao
 * @date 2021/8/10
 */
@RestController
@RequestMapping("user")
public class UserController {

    @ApiOperation("用户列表") // 单个接口的描述
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", required = true, value = "ID", defaultValue = "0"),// 每个参数的类型，名称，数据类型，是否校验，描述，默认值(这些在界面上有展示)
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "姓名", defaultValue = ""),
            @ApiImplicitParam(paramType = "query", name = "age", dataType = "Integer", required = true, value = "年龄", defaultValue = "0"),
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"), // 响应对应编码的描述
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @GetMapping("list")
    public List<User> getAddressInfo(@RequestParam(value = "id") Long id,
                                     @RequestParam(value = "name") String name,
                                     @RequestParam(value = "age") Integer age) {
        User user = new User(id, name, age);
        return ListUtil.of(user);

    }

    @ApiOperation("获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "ID", defaultValue = "0"),// 每个参数的类型，名称，数据类型，是否校验，描述，默认值(这些在界面上有展示)
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @GetMapping("info")
    public User getAddressList(@RequestParam(value = "id") Long id) {
        User user = new User(id, "admin", 25);
        return user;
    }
}
