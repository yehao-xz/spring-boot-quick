package quick.event.event;

import org.springframework.context.ApplicationEvent;

/**
 * 事件
 *
 * @author yehao
 * @date 2021/8/10
 */
public class TestEvent extends ApplicationEvent {

    public TestEvent(String source) {
        super(source);
    }
}
