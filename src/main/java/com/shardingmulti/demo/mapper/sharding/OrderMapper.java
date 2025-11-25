package com.shardingmulti.demo.mapper.sharding;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.shardingmulti.demo.pojo.Order;
import com.shardingmulti.demo.pojo.OrderSharding;

@Repository
@Mapper
public interface OrderMapper {

    Order selectOneOrder(Long orderid);
}