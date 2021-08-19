package quick.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import quick.event.event.TestEvent;

/**
 * 事件监听
 *
 * @author yehao
 * @date 2021/8/10
 */
@Slf4j
@Component
public class TestListener {

    @Async
    @Order
    @EventListener(TestEvent.class)
    public void test(TestEvent testEvent) {
        log.info("TestListener ...{}", testEvent.getSource());
    }
}
