package com.shardingmulti.demo.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shardingmulti.demo.mapper.goodsdb.GoodsMapper;
import com.shardingmulti.demo.mapper.sharding.OrderMapper;
import com.shardingmulti.demo.mapper.sharding.OrderShardingMapper;
import com.shardingmulti.demo.pojo.Goods;

import javax.annotation.Resource;

import com.shardingmulti.demo.pojo.Order;
import com.shardingmulti.demo.pojo.OrderSharding;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 实现到各个数据源的访问
 *
 * https://www.cnblogs.com/architectforest/p/13537436.html
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private OrderShardingMapper orderShardingMapper;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 访问goodsdb
     * http://127.0.0.1:8080/home/goodsinfo?goodsid=1
     *
     * @param goodsId
     * @return
     */
    //商品详情 参数:商品id
    @GetMapping("/goodsinfo")
    @ResponseBody
    @DS("goodsdb")
    public Goods goodsInfo(@RequestParam(value="goodsid",required = true,defaultValue = "0") Long goodsId) {
        Goods goods = goodsMapper.selectOneGoods(goodsId);
        return goods;
    }

    /**
     * 访问orderdb
     * http://127.0.0.1:8080/home/orderinfo?orderid=1
     *
     * @param orderId
     * @return
     */
    //订单统计库，参数：订单id
    @GetMapping("/orderinfo")
    @ResponseBody
    public Order orderInfo(@RequestParam(value="orderid",required = true,defaultValue = "0") Long orderId) {
        Order order = orderMapper.selectOneOrder(orderId);
        return order;
    }

    /**
     * 访问分表库:
     * http://127.0.0.1:8080/home/orderlist
     *
     * @return
     */
    //两个分表库中的订单列表
    @GetMapping("/orderlist")
    public String list(Model model, @RequestParam(value="currentPage",required = false,defaultValue = "1") Integer currentPage){

       PageHelper.startPage(currentPage, 5);

        List<OrderSharding> orderList = orderShardingMapper.selectAllOrder();
        /*
        for (int i = 0; i < orderList.size(); i++) {
            OrderSharding s = (OrderSharding)orderList.get(i);
            System.out.println("----------:"+s.getOrderId()+"  "+s.getGoodsName());
        }
        */
        model.addAttribute("orderlist",orderList);
        PageInfo<OrderSharding> pageInfo = new PageInfo<>(orderList);
        model.addAttribute("pageInfo", pageInfo);
        System.out.println("------------------------size:"+orderList.size());
        return "order/list";
    }

    /**
     * 从druid的监控页面查看创建的连接:
     * http://127.0.0.1:8080/druid
     * 可以看到连接有共4个数据源：
     */
}

