package com;

import com.study.service.TechInterface;
import com.styde.rpc.RpcClientFrame;

/**
 * @author YCKJ1409
 */
public class Client {

    public static void main(String[] args) throws Exception {
        //动态代理获取我们的对象
        TechInterface techInterface = RpcClientFrame.getRemoteProxyObj(TechInterface.class);
        System.out.println(techInterface.coding("Jack Chen"));
    }
}
