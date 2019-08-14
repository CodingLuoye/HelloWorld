package cn.enjoy.order.utils;

import java.util.List;

/**
 * @author YCKJ1409
 */
public abstract class AbstractLoadBalance {

    public volatile static List<String> SERVICE_LIST;

    /**
     * 选择服务主机
     * @return
     */
    public abstract String choseServiceHost();

}
