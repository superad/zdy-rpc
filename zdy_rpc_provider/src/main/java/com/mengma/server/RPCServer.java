package com.mengma.server;

import com.mengma.handler.RequestDecoder;
import com.mengma.handler.ResponseEncoder;
import com.mengma.handler.UserServerHandler;
import com.mengma.pojo.RPCRequest;
import com.mengma.pojo.RPCResponse;
import com.mengma.serializer.JSONSerializer;
import com.mengma.serializer.Serializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author fgm
 * @description  RPC Service
 * @date 2020-04-11
 ***/
@Component
public class RPCServer {

    @PostConstruct
    public void init(){
        try {
            startServer("127.0.0.1",8990);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void startServer(String hostName,int port) throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        final Serializer serializer = new JSONSerializer();

        io.netty.bootstrap.ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ResponseEncoder(RPCResponse.class,serializer));
                    pipeline.addLast(new RequestDecoder(RPCRequest.class,serializer));
                    pipeline.addLast(new UserServerHandler());

                }
            });
        serverBootstrap.bind(hostName,port).sync();

        System.out.println("RPC server start at "+port);

    }
}
