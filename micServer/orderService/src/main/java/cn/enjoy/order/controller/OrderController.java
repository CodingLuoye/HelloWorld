package cn.enjoy.order.controller;

import cn.enjoy.order.pojo.Order;
import cn.enjoy.order.pojo.Product;
import cn.enjoy.order.utils.AbstractLoadBalance;
import cn.enjoy.order.utils.RamdomLoadBalance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author YCKJ1409
 */
@RequestMapping("/order")
@RestController
public class OrderController {

    @Resource
    private RestTemplate restTemplate;

    private AbstractLoadBalance abstractLoadBalance = new RamdomLoadBalance();

    @RequestMapping("/getOrder/{id}")
    public Object getOrder(@PathVariable("id") String id ) {
        Product product = restTemplate.getForObject("http://"+ abstractLoadBalance.choseServiceHost()+"/product/getProduct/1", Product.class);
        return new Order(id,"orderName",product);
    }
}
