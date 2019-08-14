package com.styde.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author YCKJ1409
 *
 */
public class RpcClientFrame {
    /**
     * 远程服务的代理对象，参数为客户端需要调用的服务
     * @param serviceInterface
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface) throws Exception{
        //默认端口8888
        InetSocketAddress serviceAddr = new InetSocketAddress("127.0.0.1",8888);
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),new Class[]{serviceInterface},
                new DynProxy(serviceInterface, serviceAddr));
    }

    /**
     * 动态代理，实现了对远程服务的访问
     */
    private static class DynProxy implements InvocationHandler {
        /**
         * 接口
         */
        private final Class<?> serviceInterface;

        private final InetSocketAddress addr;

        public DynProxy(Class<?> serviceInterface,InetSocketAddress addr){
            this.serviceInterface = serviceInterface;
            this.addr= addr;
        }

        /**
         * 动态代理类，增强实现了对远程服务的访问
         */
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Socket socket = null;
            ObjectInputStream objectInputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try{
                socket = new Socket();
                socket.connect(addr);
                //往远端发送数据，按照顺序发送数据：类名，方法名，参数类型，参数值
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                //发送 类名 使用utf-8避免乱码
                objectOutputStream.writeUTF(serviceInterface.getName());
                //发送 方法名 使用utf-8避免乱码
                objectOutputStream.writeUTF(method.getName());
                // 发送参数类型
                objectOutputStream.writeObject(method.getParameterTypes());
                //参数值
                objectOutputStream.writeObject(args);
                objectOutputStream.flush();

                objectInputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("远程调用成功："+serviceInterface.getName());
                return objectInputStream.readObject();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                socket.close();
                objectInputStream.close();
                objectOutputStream.close();
            }
            return null;
        }
    }
}
