# blog-ms-authcenter

#### 介绍
基于spring boot搭建的微服务，作为博客项目的认证鉴权中心，主要为其他服务提供token颁发、认证及权限验证接口。  

关键词：java，spring boot，spring cloud alibaba，mybatis，mysql，redis，jwt

#### 程序结构说明
```
│  .gitignore
│  Dockerfile ------------------------------------------------------dockerfile文件
│  pom.xml ---------------------------------------------------------pom文件
│  README.md -------------------------------------------------------readme文档
│  
└─src
    ├─main
    │  ├─java
    │  │  └─top
    │  │      └─guoshouzhen
    │  │          └─blog
    │  │              └─blogmsauth
    │  │                  │  BlogMsAuthApplication.java ------------------------------主程序类
    │  │                  │  
    │  │                  ├─aop ------------------------------------------------------切面类
    │  │                  │      ControllerLogAspect.java ----------------------------请求日志切面类，监控请求数据
    │  │                  │      DynamicDataSourceAspect.java ------------------------多数据源切面类，支持根据注解动态切换数据库
    │  │                  │      SlowQuerySqlLogAspect.java --------------------------慢sql切面类，记录慢sql日志
    │  │                  │      
    │  │                  ├─config ---------------------------------------------------配置类
    │  │                  │  ├─db
    │  │                  │  │  │  MybatisConfig.java --------------------------------mybatis配置
    │  │                  │  │  │  
    │  │                  │  │  └─datasource -----------------------------------------多数据源配置
    │  │                  │  │          DataSourceConfig.java
    │  │                  │  │          DynamicDataSourceContextHolder.java
    │  │                  │  │          DynamicRoutingDataSource.java
    │  │                  │  │          
    │  │                  │  ├─jackson -----------------------------------------------jackson配置
    │  │                  │  │      MyJackson2ObjectMapperBuilderCustomizer.java -----解决jackson不支持序列化Java8LocalDatetime的问题
    │  │                  │  │      
    │  │                  │  ├─properties
    │  │                  │  │      DruidProperties.java -----------------------------druid数据库连接池配置
    │  │                  │  │      RsaKeyProperties.java ----------------------------rsa加密密钥配置，用于token颁发及验证
    │  │                  │  │      
    │  │                  │  ├─redis
    │  │                  │  │      RedisConfig.java ---------------------------------redis配置
    │  │                  │  │      
    │  │                  │  └─web
    │  │                  │          WebMvcConfiguration.java ------------------------spring boot拦截器配置
    │  │                  │          
    │  │                  ├─controller
    │  │                  │  └─innerapi ----------------------------------------------对内接口
    │  │                  │          AuthController.java -----------------------------认证及鉴权相关接口
    │  │                  │          DemoController.java
    │  │                  │          
    │  │                  ├─exception ------------------------------------------------全局异常处理
    │  │                  │      MyErrorController.java ------------------------------自定义ErrorController实现，拦截未进入控制器就抛出的异常
    │  │                  │      MyExceptionHandler.java -----------------------------全局异常处理器
    │  │                  │      ServiceException.java -------------------------------自定义业务异常，抛出异常时可指定是否追踪堆栈信息
    │  │                  │      
    │  │                  ├─interceptor ----------------------------------------------拦截器
    │  │                  │      CommonInterceptor.java ------------------------------公共拦截器，请求处理前添加日志追踪ID、签名验证等，请求处理完后做一些资源清理操作
    │  │                  │      
    │  │                  ├─manager --------------------------------------------------业务处理层
    │  │                  │  └─business
    │  │                  │          AuthService.java --------------------------------权限相关的业务逻辑处理
    │  │                  │          TokenService.java -------------------------------token相关的业务逻辑处理
    │  │                  │          
    │  │                  ├─mapper ---------------------------------------------------mapper层，由mybatishelper插件自动根据数据表生成，根据需要再做修改
    │  │                  │  └─user
    │  │                  │          RoleMapper.java
    │  │                  │          UserMapper.java
    │  │                  │          UserRoleMapper.java
    │  │                  │          
    │  │                  ├─model ----------------------------------------------------model类
    │  │                  │  ├─annotations -------------------------------------------自定义注解
    │  │                  │  │      DatabaseSource.java
    │  │                  │  │      
    │  │                  │  ├─bo ----------------------------------------------------业务model
    │  │                  │  │  ├─jwt
    │  │                  │  │  │      PlayloadBo.java
    │  │                  │  │  │      
    │  │                  │  │  ├─log
    │  │                  │  │  │      ControllerLogBo.java
    │  │                  │  │  │      
    │  │                  │  │  └─user
    │  │                  │  │          RoleAuthorityBo.java
    │  │                  │  │          UserTokenDetailsBo.java
    │  │                  │  │          
    │  │                  │  ├─constants ---------------------------------------------常量
    │  │                  │  │      TokenConstant.java
    │  │                  │  │      
    │  │                  │  ├─dto ---------------------------------------------------dto类，定义接口入参
    │  │                  │  │      CheckAuthDto.java
    │  │                  │  │      LoginInfoDto.java
    │  │                  │  │      RemoveTokenDto.java
    │  │                  │  │      
    │  │                  │  ├─enums -------------------------------------------------枚举类
    │  │                  │  │      DataSourceEnum.java
    │  │                  │  │      ErrorCodeEnum.java
    │  │                  │  │      ResultEnum.java
    │  │                  │  │      
    │  │                  │  ├─po ----------------------------------------------------数据库实体类
    │  │                  │  │  └─user
    │  │                  │  │          Role.java
    │  │                  │  │          User.java
    │  │                  │  │          UserRole.java
    │  │                  │  │          
    │  │                  │  └─vo ----------------------------------------------------vo类，定义接口返回数据
    │  │                  │          AuthVo.java
    │  │                  │          Result.java
    │  │                  │          TokenVo.java
    │  │                  │          
    │  │                  ├─service --------------------------------------------------service层，由mybatishelper插件自动根据数据表生成，根据需要再做修改
    │  │                  │  └─user
    │  │                  │      │  RoleService.java
    │  │                  │      │  UserRoleService.java
    │  │                  │      │  UserService.java
    │  │                  │      │  
    │  │                  │      └─impl
    │  │                  │              RoleServiceImpl.java
    │  │                  │              UserRoleServiceImpl.java
    │  │                  │              UserServiceImpl.java
    │  │                  │              
    │  │                  └─utils ----------------------------------------------------工具类
    │  │                          AesUtil.java
    │  │                          DateUtil.java
    │  │                          DbUtil.java
    │  │                          FileUtil.java
    │  │                          JacksonUtil.java
    │  │                          JwtUtil.java
    │  │                          RedisUtil.java
    │  │                          RsaUtil.java
    │  │                          SpringUtil.java
    │  │                          StringUtil.java
    │  │                          TraceIdUtil.java
    │  │                          
    │  └─resources -------------------------------------------------------------------配置文件
    │      │  bootstrap-dev.yml ------------------------------------------------------开发环境配置
    │      │  bootstrap-prod.yml -----------------------------------------------------生成环境配置
    │      │  bootstrap.yml ----------------------------------------------------------公共配置（含nacos多环境配置）
    │      │  logback-custom.xml -----------------------------------------------------日志配置
    │      │  
    │      └─mapper ------------------------------------------------------------------mybatis sql配置，由mybatishelper插件自动根据数据表生成，根据需要再做修改
    │          └─user
    │                  RoleMapper.xml
    │                  UserMapper.xml
    │                  UserRoleMapper.xml
    │                  
    └─test ---------------------------------------------------------------------------单元测试
        └─java
            └─top
                └─guoshouzhen
                    └─blog
                        └─blogmsauth
                                BlogMsAuthApplicationTests.java
                                ModuleTests.java
                                RsaTest.java
```
#### 部署说明
* docker容器部署
    * 使用maven插件打包
    * 使用dockerfile-maven build，并push镜像到远程仓库
    * 在服务器上拉取镜像,并运行
    * 运行示例
    ```
  docker run --name blog-auth  \
  -p 8011:8011 \
  --net commonnetwork \
  --ip 172.18.2.1 \
  -v /root/appdata/blog/logs:/root/appdata/blog/logs \
  -v /root/appdata/blog/fileDoc:/root/appdata/blog/fileDoc \
  -v /root/appdata/blog/fileDoc/authcenter/rsaKey/key_rsa.pub:/root/appdata/blog/fileDoc/authcenter/rsaKey/key_rsa.pub \
  -v /root/appdata/blog/fileDoc/authcenter/rsaKey/key_rsa:/root/appdata/blog/fileDoc/authcenter/rsaKey/key_rsa \
  -d registry.cn-hangzhou.aliyuncs.com/blog-regs/blog-ms-auth
  ```