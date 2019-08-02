package cn.shaines.tcp.websocket.controller;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;

/**
 * 特别需要注意, 这个类是多实例, 所以存储多个客户端需要使用类静态变量存储
 * @author houyu
 * @createTime 2019/8/1 14:39
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketController {

    /** 记录当前在线连接数。 */
    private static LongAdder onlineCount = new LongAdder();

    /** concurrent包的线程安全Set，用来存放每个客户端对应的WebsockerController对象。 */
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();

    /** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     // 加入set中
        addOnlineCount();           // 在线数加1
        sendGroupMessage("欢迎光临! 有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);       // 从set中删除
        subOnlineCount();                   // 在线数减1
        sendGroupMessage("有连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        sendGroupMessage(message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendGroupMessage(String message) {
        for (WebSocketController item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    public static int getOnlineCount() {
        return onlineCount.intValue();
    }

    public static void addOnlineCount() {
        onlineCount.increment();
    }

    public static void subOnlineCount() {
        onlineCount.decrement();
    }

}
