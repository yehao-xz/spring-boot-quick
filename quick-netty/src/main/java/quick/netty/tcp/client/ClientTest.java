package quick.netty.tcp.client;

/**
 * @author yehao
 * @date 2021/7/26
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {
        NettyTcpClient tcpClient = new NettyTcpClient("localhost", 10028);
        tcpClient.connect();
    }
}
