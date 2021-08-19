package quick.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.event.event.TestEvent;
import quick.event.utils.ApplicationContextHolder;

/**
 * 测试
 *
 * @author yehao
 * @date 2021/8/10
 */
@RestController
@RequestMapping("event")
public class EventController {

    @GetMapping("/publish/{msg}")
    public String publish(@PathVariable String msg) {
        ApplicationContextHolder.publishEvent(new TestEvent(msg));
        return "publish event, msg: " + msg;
    }
}
