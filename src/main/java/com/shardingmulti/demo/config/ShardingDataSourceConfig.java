package com.shardingmulti.demo.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageHelper;
import com.shardingmulti.demo.algorithm.DatabasePreciseShardingAlgorithm;
import com.shardingmulti.demo.algorithm.OrderTablePreciseShardingAlgorithm;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * shardingjdbc的数据源，创建时要使用ShardingDataSourceFactory
 *
 * 它负责连接3个库：两个分表库:saleorder01,saleorder02, 一个非分表库:orderdb
 *
 * 注意shardingjdbc所管理的数据源中，只能有一个非分表的库，而且要设置为默认库，
 *
 * 否则不能正常访问
 *
 * @author zhanzhan
 * @date 2025/11/25 20:06
 */
@Configuration
@MapperScan(basePackages = "com.shardingmulti.demo.mapper.sharding", sqlSessionFactoryRef = "shardingSqlSessionFactory")
public class ShardingDataSourceConfig {

    //分表算法
    @Resource
    private OrderTablePreciseShardingAlgorithm orderTablePreciseShardingAlgorithm;

    //分库算法
    @Resource
    private DatabasePreciseShardingAlgorithm databasePreciseShardingAlgorithm;

    //第一个订单库
    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.saleorder01")
    public DataSource saleorder01() {
        return DruidDataSourceBuilder.create()
            .build();
    }

    //第二个订单库
    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "spring.datasource.druid.saleorder02")
    public DataSource saleorder02() {
        return DruidDataSourceBuilder.create()
            .build();
    }

    //第三个库，订单统计库，做为默认
    @Bean(name = "orderdb")
    @ConfigurationProperties(prefix = "spring.datasource.druid.orderdb")
    public DataSource orderdb() {
        return DruidDataSourceBuilder.create()
            .build();
    }

    //shardingjdbc的数据源，创建时要使用ShardingDataSourceFactory
    // 它负责连接3个库：两个分表库:saleorder01,saleorder02, 一个非分表库:orderdb
    // 注意shardingjdbc所管理的数据源中，只能有一个非分表的库，而且要设置为默认库，
    // 否则不能正常访问

    //创建数据源，需要把分库的库都传递进去
    @Bean("dataSource")
    public DataSource dataSource(@Qualifier("db1") DataSource saleorder01,
                                 @Qualifier("db2") DataSource saleorder02,
                                 @Qualifier("orderdb") DataSource orderdb) throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
        dataSourceMap.put("orderdb", orderdb);  //可以去掉
        dataSourceMap.put("db1", saleorder01);
        dataSourceMap.put("db2", saleorder02);
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDataSourceName("orderdb"); //可以去掉
        //如果有多个数据表需要分表，依次添加到这里
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        // shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        Properties p = new Properties();
        p.setProperty("sql.show", Boolean.TRUE.toString());
        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, p);
        return dataSource;
    }

    // 创建SessionFactory
    @Bean(name = "shardingSqlSessionFactory")
    public SqlSessionFactory shardingSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
    throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath:mapper/sharding/*.xml"));
        return bean.getObject();
    }

    // 创建事务管理器
    @Bean("shardingTransactionManger")
    public DataSourceTransactionManager shardingTransactionManger(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 创建SqlSessionTemplate
    @Bean(name = "shardingSqlSessionTemplate")
    public SqlSessionTemplate shardingSqlSessionTemplate(
        @Qualifier("shardingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //订单表的分表规则配置
    private TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order",
            "db1.t_order_$->{1..2},db2.t_order_$->{3..4}");
        //result.setDatabaseShardingStrategyConfig(getDatabaseStrategyConfiguration());
        result.setDatabaseShardingStrategyConfig(
            new StandardShardingStrategyConfiguration("orderId", databasePreciseShardingAlgorithm));
        //result.setTableShardingStrategyConfig(getStrategyConfiguration());
        result.setTableShardingStrategyConfig(
            new StandardShardingStrategyConfiguration("orderId", orderTablePreciseShardingAlgorithm));
        return result;
    }

    //分页
    @Bean(name = "pageHelper")
    public PageHelper getPageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "true");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}