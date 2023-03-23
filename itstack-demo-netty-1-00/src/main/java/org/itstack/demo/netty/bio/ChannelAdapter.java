package org.itstack.demo.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 微信公众号：bugstack虫洞栈 | 专注原创技术专题案例，以最易学习编程的方式分享知识，让萌新、小白、大牛都能有所收获。目前已完成的专题有；Netty4.x从入门到实战、用Java实现JVM、基于JavaAgent的全链路监控等，其他更多专题还在排兵布阵中。
 * 论坛：http://bugstack.cn
 * Create by 付政委 on @2019
 *
 * ChannelAdapter继承了Thread类，一方面用于处理每一个Socket连接，
 * 另一方面定义了抽象的公共方法channelActive、channelRead（相当于定义了一个规范的处理流程/模版），
 * 这样服务端和客户端的消息处理器通过继承该适配器都可以在不同的阶段做一些相应的扩展处理。
 */
public abstract class ChannelAdapter extends Thread {

    private Socket socket;
    private ChannelHandler channelHandler;
    private Charset charset;

    public ChannelAdapter(Socket socket, Charset charset) {
        this.socket = socket;
        this.charset = charset;
        while (!socket.isConnected()) {
            break;
        }
        channelHandler = new ChannelHandler(this.socket, charset);
        channelActive(channelHandler);
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), charset));
            String str = null;
            while ((str = input.readLine()) != null) {
                channelRead(channelHandler, str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 链接通知抽象类
    public abstract void channelActive(ChannelHandler ctx);

    // 读取消息抽象类
    public abstract void channelRead(ChannelHandler ctx, Object msg);

}


