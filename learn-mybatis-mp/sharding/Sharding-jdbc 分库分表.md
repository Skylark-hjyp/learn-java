# 分库分表
当数据量变大的时候，一个表存不下，所以需要将同一张表存到多个数据库的多个表中，俗称分库分表。最常使用的分库分表中间件是`sharding-jdbc`,该中间件通过记录的某一列计算所属的数据库和表，从而知道需要在哪个数据库的哪张表进行操作。
# 入门示例
## 引入依赖
```xml
<dependencies>
    <!-- 实现对数据库连接池的自动化配置 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

    <dependency> <!-- 本示例，我们使用 MySQL -->
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>8.0.12</scope>
    </dependency>

    <!-- 实现对 MyBatis 的自动化配置 -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.1</version>
    </dependency>

    <!-- 实现对 Sharding-JDBC 的自动化配置 -->
    <dependency>
        <groupId>org.apache.shardingsphere</groupId>
        <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
        <version>4.0.0-RC2</version>
    </dependency>

    <dependency>
        <groupId>com.sun.xml.ws</groupId>
        <artifactId>jaxws-rt</artifactId>
        <version>2.3.0</version>
    </dependency>

</dependencies>
```
## 配置分库分表映射关系
```yaml
spring:
  # ShardingSphere 配置项
  shardingsphere:
    datasource:
      # 所有数据源的名字
      names: ds-orders-0, ds-orders-1
      # 订单 orders 数据源配置 00
      ds-orders-0:
        type: com.zaxxer.hikari.HikariDataSource # 使用 Hikari 数据库连接池
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/orders_0?useSSL=false&useUnicode=true&characterEncoding=UTF-8
        username: root
        password: hejianfei1998
      # 订单 orders 数据源配置 01
      ds-orders-1:
        type: com.zaxxer.hikari.HikariDataSource # 使用 Hikari 数据库连接池
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/orders_1?useSSL=false&useUnicode=true&characterEncoding=UTF-8
        username: root
        password: hejianfei1998
    # 分片规则
    sharding:
      tables:
        # orders 表配置
        orders:
          actualDataNodes: ds-orders-0.orders_$->{[0,2,4,6]}, ds-orders-1.orders_$->{[1,3,5,7]} # 映射到 ds-orders-0 和 ds-orders-1 数据源的 orders 表们
          key-generator: # 主键生成策略
            column: id
            type: SNOWFLAKE
          database-strategy:
            inline:
              algorithm-expression: ds-orders-$->{user_id % 2}
              sharding-column: user_id  # 拆分数据库所依据的列
          table-strategy:
            inline:
              algorithm-expression: orders_$->{user_id % 8}
              sharding-column: user_id  # 拆分表所依据的列
        # order_config 表配置
        order_config:
          actualDataNodes: ds-orders-0.order_config # 仅映射到 ds-orders-0 数据源的 order_config 表
    # 拓展属性配置
    props:
      sql:
        show: true # 打印 SQL

# mybatis 配置内容
mybatis:
#  config-location: classpath:mybatis-config.xml # 配置 MyBatis 配置文件路径
  mapper-locations: classpath:mapper/*.xml # 配置 Mapper XML 地址
  type-aliases-package: com.example.mybatis.sharding.entity # 配置数据库实体包路径
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰
```
## 编写mapper接口和xml文件
```java
@Mapper
@Repository
public interface OrderMapper {
    Order selectById(@Param("id") Integer id);

    List<Order> selectListByUserId(@Param("userId") Integer userId);

    void insert(Order order);
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatis.sharding.mapper.OrderMapper">

    <sql id="FIELDS">
        id, user_id
    </sql>

    <select id="selectById" parameterType="java.lang.Integer" resultType="com.example.mybatis.sharding.entity.Order">
        SELECT
        <include refid="FIELDS" />
        FROM orders
        WHERE id = #{id}
    </select>

    <select id="selectListByUserId" parameterType="java.lang.Integer" resultType="com.example.mybatis.sharding.entity.Order">
        SELECT
        <include refid="FIELDS" />
        FROM orders
        WHERE user_id = #{userId}
    </select>

    <insert id="insert" parameterType="com.example.mybatis.sharding.entity.Order" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO orders (
            user_id
        ) VALUES (
                     #{userId}
                 )
    </insert>

</mapper>
```
> 在前面我们讲了配置`多数据源`，配置多数据源是根据`@DS()注解`在建立连接时获取对应的数据库连接，但是不能`根据数据动态指定所要操作的数据库`，也不能`指定所操作的表`。但是通过`sharding-jdbc`中间件我们既可以指定`数据库`，也可以`指定表`。
>
> `sharding-jdbc`是改写了`JDBC`，这样`mybatis`调用`JDBC接口`时，实际调用了`sharding-jdbc改写过的接口`，然后该中间件将逻辑表转换为真实表。

## 参考链接

[芋道 Spring Boot 分库分表入门](https://www.iocoder.cn/Spring-Boot/sharding-datasource/?github#)

