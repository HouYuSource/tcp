
package cn.shaines.tcp.netty.demo1.client;

import cn.shaines.tcp.netty.util.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author pancm
 * @Title: NettyClientHandler
 * @Description: 客户端业务逻辑实现
 * @Version:1.0.0
 * @date 2017年10月8日
 */
@Service("nettyClientHandler")
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyClient nettyClient;

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("建立连接时：" + new Date());
        NettyUtils.sendData(ctx.channel(), "客户端: 连接到服务端了...");
        ctx.fireChannelActive();
    }

    /**
     * 关闭连接时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        nettyClient.doConnect(new Bootstrap(), eventLoop);
        super.channelInactive(ctx);
    }

    /**
     * 心跳请求处理 每4秒发送一次心跳请求;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.WRITER_IDLE.equals(event.state())) { // 如果写通道处于空闲状态,就发送心跳命令
                // 因为设置了心跳检测, 如果不发生心跳检测的话, 服务端发现该客户端超时就会关闭该客户端连接
                NettyUtils.sendData(ctx.channel(), "心跳连接");
            }
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            System.out.println("客户端收到的数据:" + msg);
            doSomeThing(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    boolean flag = true;


    /** 也可以存储对应的管道, 然后进行逻辑处理 */
    public void doSomeThing(ChannelHandlerContext ctx) {
	    if (!flag) {
	        return;
        }
        flag = false;
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                NettyUtils.sendData(ctx, "现在是第:" + i + "次");
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }).start();


    }

}

