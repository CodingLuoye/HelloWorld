package cn.enjoy.order.utils;

import org.springframework.util.CollectionUtils;

import java.util.Random;

/**
 * 实现一个随机的算法，从列表里面获取一个值
 * @author YCKJ1409
 */
public class RamdomLoadBalance extends AbstractLoadBalance {
    @Override
    public String choseServiceHost() {
        String result = "";
        if(!CollectionUtils.isEmpty(SERVICE_LIST)) {
            int index = new Random().nextInt(SERVICE_LIST.size());
            result = SERVICE_LIST.get(index);
        }
        return result ;
    }
}
