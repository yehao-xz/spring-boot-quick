package quick.netty.tcp.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;
import quick.netty.utils.RetryUtil;

import java.util.concurrent.TimeUnit;

/**
 * 注解@Sharable 用来说明ChannelHandler可以在多个channel直接共享使用 每次失败重连都是一个新的管道Channel 多个管道要共享这个ReconnectHandler
 *
 * @author yehao
 * @date 2021/7/26
 */
@Slf4j
@ChannelHandler.Sharable
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private int retries = 0;
    private NettyTcpClient nettyTcpClient;

    public ReconnectHandler(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("成功连接到TCP服务端");
        retries = 0;
        ctx.fireChannelActive();//作用：触发事件告知Inbound ChannelHandler：ChannelHandlerContext的Channel现在处于活动状态，调用ChannelInboundHandler的channelActive
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (retries == 0) {
            log.error("TCP服务端连接失败");
            ctx.close();
        }
        // 重试
        int retryTime = RetryUtil.getNextTime(++retries);
        log.info("尝试去重试 重试次数{} 等待时间{}分钟", retries, retryTime);
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> {
            log.info("Reconnecting ...");
            nettyTcpClient.connect();
        }, 1000 * 60 * retryTime, TimeUnit.MILLISECONDS);

        //现在处于不活动状态，调用ChannelInboundHandler的channelInactive
        ctx.fireChannelInactive();
    }
}
