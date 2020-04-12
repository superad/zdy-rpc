package com.mengma.handler;

import com.mengma.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author fgm
 * @description
 * @date 2020-04-11
 ***/
public class RequestEncoder extends MessageToByteEncoder{


    private Class<?> clazz;

    private Serializer serializer;

    public RequestEncoder(Class<?> clazz, Serializer serializer){
        this.clazz = clazz;
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("RequestEncoder....");
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
