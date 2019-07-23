package cn.enjoy.product.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * @author YCKJ1409
 */
public class ServiceRegister {

    private  static final String BASE_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";

    public static  void register(String address,int port) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,(watchedEvent)->{});
            Stat exists = zooKeeper.exists(BASE_SERVICES + SERVICE_NAME, false);
            //判断服务根节点是否存在
            if(exists==null) {
                zooKeeper.create(BASE_SERVICES + SERVICE_NAME,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            String serverPath = address+":"+port;
            //注册的类型EPHEMERAL_SEQUENTIAL  零时，并且带序号的
            zooKeeper.create(BASE_SERVICES + SERVICE_NAME+"/child",serverPath.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("产品服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
