package cn.shaines.tcp.netty.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author houyu
 * @createTime 2019/8/2 16:49
 */
public class NettyUtils {

    /** 定义分割符 */
    public static final String DELIMITER_BASED = "DELIMITER_BASED";

    /** 发送数据, 自动添加分割符 */
    public static <T extends Channel> void sendData(T channel, Object data) {
        channel.writeAndFlush(data + DELIMITER_BASED);
    }

    /** 发送数据, 自动添加分割符 */
    public static <T extends ChannelHandlerContext> void sendData(T  ctx, Object data) {
        ctx.writeAndFlush(data + DELIMITER_BASED);
    }

}
