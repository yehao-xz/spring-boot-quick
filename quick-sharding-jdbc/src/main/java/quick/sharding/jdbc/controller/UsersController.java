package quick.sharding.jdbc.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.sharding.jdbc.mapper.UsersMapper;
import quick.sharding.jdbc.entity.Users;

import java.util.List;

/**
 * UsersController
 *
 * @author yehao
 * @date 2021/8/19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersMapper usersMapper;

    @GetMapping("/getAll")
    public String getAll() {
        List<Users> users = usersMapper.selectList(new QueryWrapper<>());
        return JSONUtil.toJsonStr(users);
    }

    @GetMapping("/save")
    public String save(String name) {
        Users users = new Users();
        users.setName(name);
        return JSONUtil.toJsonStr(usersMapper.insert(users));
    }

    @GetMapping("/masterGetAll")
    public String masterGetAll() {
        HintManager.getInstance().setMasterRouteOnly();
        List<Users> users = usersMapper.selectList(new QueryWrapper<>());
        return JSONUtil.toJsonStr(users);
    }
}
