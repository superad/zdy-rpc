package com.mengma.handler;

import com.mengma.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author fgm
 * @description  返回解析
 * @date 2020-04-12
 ***/
public class ResponseDecoder extends MessageToMessageDecoder {

    private Class<?> clazz;
    private Serializer serializer;

    public ResponseDecoder(Class<?> clazz, Serializer serializer){
        this.clazz = clazz;
        this.serializer = serializer;
    }

    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        System.out.println("ResponseDecoder....");
        ByteBuf byteBuf=(ByteBuf)msg;
        if(null==msg){
            return;
        }
        int size=byteBuf.readInt();
        byte[] data=new byte[size];
        byteBuf.readBytes(data);
        Object object=  serializer.deserialize(clazz,data);
        out.add(object);
    }



}
