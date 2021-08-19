package quick.netty.tcp.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 客户端连接到服务器端后，会循环执行一个任务：随机等待几秒，然后ping一下Server端，即发送一个心跳包。
 *
 * @author yehao
 * @date 2021/7/19
 */
@Slf4j
@ChannelHandler.Sharable
public class PingHandler extends ChannelInboundHandlerAdapter {

    public static final String HEART_BEAT = "heart beat!";

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.ALL_IDLE) {
//                // write heartbeat to server
//                ctx.writeAndFlush(HEART_BEAT);
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ping(ctx.channel());
    }

    private void ping(Channel channel) {
        int second = 3;
        log.info("next heart beat will send after {}s.", second);
        ScheduledFuture<?> future = channel.eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                if (channel.isActive()) {
                    log.info("sending heart beat to the server...");
                    channel.writeAndFlush(HEART_BEAT);
                } else {
                    log.info("The connection had broken, cancel the task that will send a heart beat.");
                    channel.closeFuture();
                    channel.pipeline().fireChannelInactive();
                }
            }
        }, second, TimeUnit.SECONDS);

        future.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isSuccess()) {
                    ping(channel);
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当Channel已经断开的情况下, 仍然发送数据, 会抛异常, 该方法会被调用.
        cause.printStackTrace();
        ctx.close();
    }
}