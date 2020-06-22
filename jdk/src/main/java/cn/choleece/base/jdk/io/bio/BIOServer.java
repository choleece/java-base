package cn.choleece.base.jdk.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author choleece
 * @Description: BIO 服务端
 * @Date 2020-06-20 22:40
 **/
public class BIOServer {

    static byte[] msg = new byte[1024];

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            System.out.println("wait for connect...");
            // 这里会将线程阻塞，从而放弃CPU
            Socket accept = serverSocket.accept();
            System.out.println("connect success...");

            System.out.println("wait for data input....");
            // 这里的read也是一个阻塞的过程，会等待输入
            accept.getInputStream().read(msg);
            System.out.println("data success....");

            System.out.println(new String(msg));
        }
    }
}
