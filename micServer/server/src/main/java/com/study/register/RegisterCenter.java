package com.study.register;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author YCKJ1409
 */
public class RegisterCenter{

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final HashMap<String,Class> SERVICEREGISTRY = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private int port;

    public RegisterCenter(int port){
        this.port = port;
    }

    public void start() throws IOException {
        //服务器监听
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
        System.out.println("start server");
        try {
            while(true){
                executor.execute(new ServiceTask(server.accept()));
            }
        }finally {
            server.close();
        }
    }

    /**
     * 服务注册，socket通讯+反射
     * @param serviceInterface
     * @param impl
     */
    public void register(Class serviceInterface,Class impl){
        SERVICEREGISTRY.put(serviceInterface.getName(),impl);
    }


    private static class ServiceTask implements Runnable {
        Socket client = null;

        public ServiceTask(Socket client) {
            this.client = client;
        }

        public void run() {
            //反射
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            try {
                inputStream = new ObjectInputStream(client.getInputStream());
                //拿到类名
                String serviceName = inputStream.readUTF();
                //拿到方法名
                String methodName = inputStream.readUTF();
                //拿到参数类型
                Class<?> [] paramTypes = (Class<?>[]) inputStream.readObject();
                //拿到参数值
                Object[] arguments = (Object[]) inputStream.readObject();
                Class serviceClass = SERVICEREGISTRY.get(serviceName);
                //使用反射机制进行调用
                Method method = serviceClass.getMethod(methodName,paramTypes);
                //反射调用方法，把结果拿到
                Object object = method.invoke(serviceClass.newInstance(),arguments);
                //把结果返回给客户端
                outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(object);
                outputStream.close();
                inputStream.close();
                client.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
