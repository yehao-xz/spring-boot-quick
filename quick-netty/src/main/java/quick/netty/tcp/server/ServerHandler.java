package quick.netty.tcp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * ServerHandler
 *
 * @author yehao
 * @date 2021/7/19
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        ctx.channel().writeAndFlush("hello1");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("server receive:" + msg);
        ctx.writeAndFlush("hello2");
    }
}
