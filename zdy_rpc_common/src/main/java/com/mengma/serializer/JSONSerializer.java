package com.mengma.serializer;

import com.alibaba.fastjson.JSON;
import com.mengma.pojo.RPCRequest;

import java.io.IOException;

/**
 * @author fgm
 * @description  Json序列化
 * @date 2020-04-11
 ***/
public class JSONSerializer implements Serializer {
    public byte[] serialize(Object object) throws IOException {
        if(null==object){
            return null;
        }
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
       if(null==bytes){
           return null;
       }
       return JSON.parseObject(bytes,clazz);
    }

    public static void main(String[] args) throws IOException {
        JSONSerializer serializer=new JSONSerializer();
        RPCRequest request=new RPCRequest();
        request.setMethodName("hello");
        byte[] bytes=  serializer.serialize(request);
        RPCRequest aa= serializer.deserialize(RPCRequest.class,bytes);
        System.out.println(aa.getMethodName());

    }
}
