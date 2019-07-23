package cn.enjoy.order.listener;

import cn.enjoy.order.utils.AbstractLoadBalance;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YCKJ1409
 */
public class InitListener implements ServletContextListener {

    private  static final String BASE_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";

    private ZooKeeper zooKeeper;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            /**
             * 连接zk,获取链表信息
             */
             zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,(watchedEvent)->{
                 /**
                  * 当有节点变更的时候，通知到订单服务
                  */
                if(watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged  && watchedEvent.getPath().equals(BASE_SERVICES+SERVICE_NAME)) {
                    updateServiceList();
                }
            });

            updateServiceList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateServiceList() {
       try{
           List<String> children = zooKeeper.getChildren(BASE_SERVICES  + SERVICE_NAME, true);
           List<String> newServerList = new ArrayList<String>();
           for(String subNode:children) {
               byte[] data = zooKeeper.getData(BASE_SERVICES  + SERVICE_NAME + "/" + subNode, false, null);
               String host = new String(data, "utf-8");
               System.out.println("host:"+host);
               newServerList.add(host);
           }
           AbstractLoadBalance.SERVICE_LIST = newServerList;
       }catch (Exception e) {
           e.printStackTrace();
       }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
