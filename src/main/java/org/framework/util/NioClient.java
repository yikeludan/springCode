package org.framework.util;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NioClient {
    public void start() throws Exception {
        /**
         * 链接服务器端
         */
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("139.196.89.179",9999));

        //向服务器端发送数据
        //从命令行获取数据，获取键盘的输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            //获取这一行数据
            String request =  scanner.nextLine();
            //如果有数据，则发送，且数据不为空
            if(request != null && request.length() > 0){
                socketChannel.write(Charset.forName("UTF-8").encode(request));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        NioClient nioClient = new NioClient();
        nioClient.start();
    }
}
