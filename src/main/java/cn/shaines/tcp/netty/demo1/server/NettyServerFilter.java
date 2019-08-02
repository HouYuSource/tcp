package cn.shaines.tcp.netty.demo1.server;

import cn.shaines.tcp.netty.util.NettyUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author pancm
 * @Title: HelloServerInitializer
 * @Description: Netty 服务端过滤器
 * @Version:1.0.0
 * @date 2017年10月8日
 */
@Component

public class NettyServerFilter extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();

        /** 添加监听空闲时间心跳操作 */
        // 入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式
        ph.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        /** 处理粘包问题 */
        // ph.addLast(new LineBasedFrameDecoder(2048));    // 字节解码器 ,其中2048是规定一行数据最大的字节数。  用于解决拆包问题
        // ph.addLast(new FixedLengthFrameDecoder(100));   // 定长数据帧的解码器 ，每帧数据100个字节就切分一次。  用于解决粘包问题
        ph.addLast(new DelimiterBasedFrameDecoder(1024 * 10, Unpooled.copiedBuffer(NettyUtils.DELIMITER_BASED.getBytes()))); //固定字符切分解码器 ,会以"DELIMITER_BASED"为分隔符。  注意此方法要放到StringDecoder()上面
        /** 编码与解码 */
        // 解码和编码，应和客户端一致
        ph.addLast("decoder", new StringDecoder());
        ph.addLast("encoder", new StringEncoder());
        /** 添加业务处理类 */
        ph.addLast("nettyServerHandler", nettyServerHandler);
    }
}
