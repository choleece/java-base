package cn.choleece.base.jdk.io.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author choleece
 * @Description: BIO client test
 * @Date 2020-06-20 22:44
 **/
public class BIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);

        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();

        socket.getOutputStream().write(msg.getBytes());

        socket.close();
    }

}
