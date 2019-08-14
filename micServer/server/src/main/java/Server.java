import com.study.register.RegisterCenter;
import com.study.service.TechInterface;
import com.study.service.impl.TechImpl;

/**
 * @author YCKJ1409
 */
public class Server {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                try{
                    //起一个服务中心
                    RegisterCenter registerCenter = new RegisterCenter(8888);
                    registerCenter.register(TechInterface.class, TechImpl.class);
                    registerCenter.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
