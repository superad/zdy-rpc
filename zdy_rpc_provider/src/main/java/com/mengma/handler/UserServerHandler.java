package com.mengma.handler;

import com.mengma.config.ApplicationContextConfig;
import com.mengma.pojo.RPCRequest;
import com.mengma.pojo.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.UUID;

public class UserServerHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 判断是否符合约定，符合则调用本地方法，返回数据
        if(!(msg instanceof RPCRequest)){
            return;
        }
        System.out.println("channelRead.....");
        RPCResponse response=new RPCResponse();
        response.setResponseId(UUID.randomUUID().toString());
        try{
            RPCRequest request=(RPCRequest)msg;
            System.out.println("requestId is : "+request.getRequestId());
            String className=request.getClassName();
            String methodName=request.getMethodName();
            Class<?>[] parameterTypes=request.getParameterTypes();
            Object[] params=request.getParameters();

            //找到对应bean 调用对应方法
            Class clazz=Class.forName(className);
            Object targetBean= ApplicationContextConfig.getContext().getBean(clazz);
            Method method= targetBean.getClass().getMethod(methodName,parameterTypes);

            Object respData=method.invoke(targetBean,params);
            response.setObject(respData);
            response.setCode(RPCResponse.SUCCESS);
        }catch (Exception ex){
            ex.printStackTrace();
            response.setCode(RPCResponse.FAILED);
            response.setMsg(ex.getMessage());
        }finally {
            ctx.writeAndFlush(response);
        }

    }


}
