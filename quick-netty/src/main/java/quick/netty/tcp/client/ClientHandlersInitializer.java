package quick.netty.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 客户端处理器初始化
 *
 * @author yehao
 * @date 2021/7/26
 */
public class ClientHandlersInitializer extends ChannelInitializer<SocketChannel> {

    private ReconnectHandler reconnectHandler;

    private MessageHandler messageHandler;

    private PingHandler pingHandler;

    public ClientHandlersInitializer(NettyTcpClient nettyTcpClient) {
        this.reconnectHandler = new ReconnectHandler(nettyTcpClient);
        this.messageHandler = new MessageHandler();
        this.pingHandler = new PingHandler();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(this.reconnectHandler);
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(this.pingHandler);
        pipeline.addLast(this.messageHandler);
    }
}
