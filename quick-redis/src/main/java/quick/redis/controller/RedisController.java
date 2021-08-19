package quick.redis.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.redis.utils.RedisUtil;

import java.util.*;

/**
 * Redis
 *
 * @author yehao
 * @date 2021/8/10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("redis")
public class RedisController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisUtil redisUtil;

    @GetMapping("/get/{key}")
    public Object get(@PathVariable("key") String key) {
        return redisUtil.get(key);
    }

    @GetMapping("/set")
    public Object set() {
        redisTemplate.opsForValue().set("age", 20);
        return JSONUtil.toJsonStr("ok");
    }

    @GetMapping("/info")
    public Object info() {
        Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute(RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", key.replace("cmdstat_", ""));
            data.put("value", StrUtil.subBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return result;
    }
}
