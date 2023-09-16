# 目录
* [引入依赖](#引入依赖)
* [配置参数](#配置参数)
* [编写Mapper和xml文件](#编写Mapper和xml文件)
# 引入依赖
首先我们需要在pom文件中引入mybatis自动配置的依赖
> 注意：mybatis自动配置的依赖一定要写版本号
```xml
<dependencies>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.1</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
# 配置参数
在配置文件中，我们需要告诉`mybatis`所有的`xml文件在哪里`，`实体类在哪里`，以及`是否开启驼峰`。
```yaml
spring:
  # datasource 数据源配置内容
  datasource:
    url: jdbc:mysql://localhost:3306/meetsystem?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: hejianfei1998

# mybatis 配置内容
mybatis:
#  config-location: classpath:mybatis-config.xml # 配置 MyBatis 配置文件路径
  mapper-locations: classpath:mapper/*.xml # 配置 Mapper XML 地址
  type-aliases-package: com.example.mybatis.entity # 配置数据库实体包路径
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰
```
# 编写Mapper和xml文件
完成以上配置后，我们就可以编写`mapper接口类`了，但是需要注意的是，接口类需要用`@Mapper`注解标明或者在`启动类`上使用`@MapperScan(basepackage=" ")`注解表明`mapper`所在包，这样`mapper`才会被`mybatis`扫描到。
编写mapper和xml文件，可以看[这篇文章](https://blog.csdn.net/qq_43705697/article/details/127803814?spm=1001.2014.3001.5502)。