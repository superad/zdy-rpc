package com.mengma.client;

import com.mengma.handler.RequestEncoder;
import com.mengma.handler.ResponseDecoder;
import com.mengma.pojo.RPCRequest;
import com.mengma.pojo.RPCResponse;
import com.mengma.serializer.JSONSerializer;
import com.mengma.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

    //创建线程池对象
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static UserClientHandler userClientHandler;

    private static Serializer serializer;

    //1.创建一个代理对象 providerName：UserService#sayHello are you ok?
    public Object createProxy(final Class<?> serviceClass){
        //借助JDK动态代理生成代理对象
        Object proxy= Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //（1）调用初始化netty客户端的方法
                if(userClientHandler == null){
                    initClient();
                }
                RPCRequest request=new RPCRequest();
                request.setRequestId(UUID.randomUUID().toString());
                request.setClassName(serviceClass.getName());
                request.setMethodName(method.getName());
                request.setParameters(args);
                request.setParameterTypes(method.getParameterTypes());
                // 设置参数
                userClientHandler.setData(request);
                // 去服务端请求数据
                return executor.submit(userClientHandler).get();
            }
        });
        return proxy;


    }



    //2.初始化netty客户端
    public static  void initClient() throws InterruptedException {
         userClientHandler = new UserClientHandler();
         serializer = new JSONSerializer();

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ResponseDecoder(RPCResponse.class,serializer));
                        pipeline.addLast(new RequestEncoder(RPCRequest.class,serializer));
                        pipeline.addLast(userClientHandler);
                    }
                });
        bootstrap.connect("127.0.0.1",8990).sync();

    }


}
