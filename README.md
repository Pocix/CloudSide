# CloudSide
基于Spring Cloud 的生态体系开发。

技术栈
>
SpringCloud：Edgware.SR3  
Springboot：1.5.9.RELEASE
- 服务发现：Eureka
- 服务声明：Feign
- 服务网关：Zuul
- 服务追踪：Sleuth Zipkin
- 服务鉴权：Jwt、Shiro
---
- UI：Layui
- 模板引擎：beetl
- 数据库：Mariadb、Druid、Mybatis
- nio ：Netty
- API接口：Swagger2
- 缓存：Redis
- 消息队列：RabbitMQ  



## 说明
这里引用了> bootdo 项目作为业务系统端的基础框架。  
Zuul网关是基于数据库的动态网关。  
Swagger 在网关中动态浏览多服务的API接口。  
框架用到Netty  
&emsp;当初设想是模拟Docker容器的方式，通过新增服务器/容器，传输服务包，自动化部署；  
&emsp;后期觉得Docker着实方便。
