package quick.dynamic.datasource.controller;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.dynamic.datasource.entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * UserController
 *
 * @author yehao
 * @date 2021/8/19
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final quick.dynamic.datasource.mapper.UserMapper userMapper;

    @GetMapping("/getAll")
    public String getAll() {
        //test1   李四
        List<User> findAllUser = userMapper.findAllUser();
        return findAllUser.get(0).getName();
    }

    @GetMapping("/getById")
    public String getById() {
        //test   张三
        return userMapper.findById(1L).getName();
    }

    @PostMapping("/add")
    public String add() {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 500; i++) {
            executorService.submit(() -> {
                userMapper.ageAddOne(1L);
            });
        }
        return JSONUtil.toJsonStr(userMapper.findById(1L));
    }
}
