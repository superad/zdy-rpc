package com.mengma.handler;

import com.mengma.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author fgm
 * @description 返回编码
 * @date 2020-04-11
 ***/
public class ResponseEncoder extends MessageToByteEncoder{


    private Class<?> clazz;

    private Serializer serializer;

    public ResponseEncoder(Class<?> clazz, Serializer serializer){
        this.clazz = clazz;
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("ResponseEncoder....");
        if(msg==null){
           return;
       }
       if(clazz==null||!clazz.isInstance(msg)){
           return;
       }
       byte[] bytes = serializer.serialize(msg);
       out.writeInt(bytes.length);
       out.writeBytes(bytes);
    }


}
