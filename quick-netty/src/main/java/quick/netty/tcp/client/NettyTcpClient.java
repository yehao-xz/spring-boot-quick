package quick.netty.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * TCP连接的客户端
 *
 * @author yehao
 * @date 2021/7/26
 */
public class NettyTcpClient {

    private String host;
    private int port;
    private Bootstrap bootstrap;

    /**
     * 将<code>Channel</code>保存起来, 可用于在其他非handler的地方发送数据
     */
    private Channel channel;

    public NettyTcpClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        // bootstrap 可重用, 只需在TcpClient实例化的时候初始化即可.
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandlersInitializer(NettyTcpClient.this));
    }

    /**
     * 向远程TCP服务器请求连接
     */
    public void connect() {
        synchronized (bootstrap) {
            ChannelFuture future = bootstrap.connect(host, port);
            future.addListener((ChannelFutureListener) future1 -> {
                if (!future1.isSuccess()) {
                    future1.channel().pipeline().fireChannelInactive();
                }
            });
            this.channel = future.channel();
        }
    }
}
