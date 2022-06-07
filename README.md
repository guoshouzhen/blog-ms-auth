# blog-ms-authcenter

#### 介绍
基于spring boot搭建的微服务，作为博客项目的认证鉴权中心，主要为其他服务提供token颁发、认证及权限验证接口。  
关键词：spring boot，spring cloud alibaba，mybatis，mysql，redis，jwt

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

 #### 接口文档示例
 ##### 失效token接口
 **Path：** /authcenter/api/innerapi/token_remove

 **Method：** POST

 **接口描述：**
 <p>接口逻辑：<br>
 用户中心调用，强制失效一组用户的token，从白名单中移除</p>

 **请求参数**

 **Headers**

 | 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
 | ------------ | ------------ | ------------ | ------------ | ------------ |
 | Content-Type  |  application/json | 是  |   |   |
 
 **Body**

 <table>
   <thead class="ant-table-thead">
     <tr>
       <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
     </tr>
   </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> userIds</span></td><td key=1><span>number []</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">一组用户ID</span></td><td key=5><p key=3><span style="font-weight: '700'">item 类型: </span><span>number</span></p></td></tr><tr key=array-370><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> </span></td><td key=1><span></span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap"></span></td><td key=5></td></tr>
                </tbody>
               </table>

 **返回数据**

 <table>
   <thead class="ant-table-thead">
     <tr>
       <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
     </tr>
   </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> result</span></td><td key=1><span>number</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">操作结果</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span></td><td key=1><span>string</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">错误码</span></td><td key=5></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> message</span></td><td key=1><span>string</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">错误信息</span></td><td key=5></td></tr><tr key=0-3><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> data</span></td><td key=1><span>object</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap"></span></td><td key=5></td></tr>
                </tbody>
               </table>

 ##### 获取token接口
 **Path：** /authcenter/api/innerapi/token_get

 **Method：** POST

 **接口描述：**
 <p>接口逻辑：<br>
 根据传入的用户ID查询用户信息，并生成对应token，放入redis token白名单。<br>
 token中的用户信息包括用户ID，数据库ID</p>

 **请求参数**

 **Headers**

 | 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
 | ------------ | ------------ | ------------ | ------------ | ------------ |
 | Content-Type  |  application/json | 是  |   |   |
 
 **Body**

 <table>
   <thead class="ant-table-thead">
     <tr>
       <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
     </tr>
   </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> userId</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">用户ID</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> dbId</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">对应数据库id</span></td><td key=5></td></tr>
                </tbody>
               </table>

 **返回数据**

 <table>
   <thead class="ant-table-thead">
     <tr>
       <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
     </tr>
   </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> result</span></td><td key=1><span>number</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应结果</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span></td><td key=1><span>string</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">错误码</span></td><td key=5></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> message</span></td><td key=1><span>string</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">错误信息</span></td><td key=5></td></tr><tr key=0-3><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> data</span></td><td key=1><span>object</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap"></span></td><td key=5></td></tr><tr key=0-3-0><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> token</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">认证令牌</span></td><td key=5></td></tr>
                </tbody>
               </table>

 ##### 鉴权接口

 **Path：** /authcenter/api/innerapi/check_auth

 **Method：** POST

 **接口描述：**
 <p>接口逻辑：<br>
 1.检查token是否合法，是否被篡改<br>
 2.检查token是否在白名单中<br>
 3.检查token是否过期，若过期，则需要从白名单中删除<br>
 4.解析token，拿到其中的用户ID，数据库ID。<br>
 5.获取权限，判断是否有相应权限，然后返回结果<br>
 6.将token中用户id对应的数据库id，权限等信息缓存</p>

 **请求参数**

 **Headers**

 | 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
 | ------------ | ------------ | ------------ | ------------ | ------------ |
 | Content-Type  |  application/json | 是  |   |   |
 
 **Body**

 <table>
   <thead class="ant-table-thead">
     <tr>
       <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
     </tr>
   </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> token</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">令牌</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> auths</span></td><td key=1><span>string []</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">需要的权限列表</span></td><td key=5><p key=3><span style="font-weight: '700'">item 类型: </span><span>string</span></p></td></tr><tr key=array-371><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> </span></td><td key=1><span></span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap"></span></td><td key=5></td></tr>
                </tbody>
               </table>

 **返回数据**

 <table>
   <thead class="ant-table-thead">
     <tr>
       <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
     </tr>
   </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> result</span></td><td key=1><span>number</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">操作结果</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span></td><td key=1><span>string</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">错误码</span></td><td key=5></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> message</span></td><td key=1><span>string</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">错误信息</span></td><td key=5></td></tr><tr key=0-3><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> data</span></td><td key=1><span>object</span></td><td key=2>非必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap"></span></td><td key=5></td></tr><tr key=0-3-0><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> loginInfo</span></td><td key=1><span>object</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">登陆信息</span></td><td key=5></td></tr><tr key=0-3-0-0><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> userId</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">用户ID</span></td><td key=5></td></tr><tr key=0-3-0-1><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> dbId</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">数据库ID</span></td><td key=5></td></tr><tr key=0-3-1><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> authorities</span></td><td key=1><span>object []</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">用户权限</span></td><td key=5><p key=3><span style="font-weight: '700'">item 类型: </span><span>object</span></p></td></tr><tr key=0-3-1-0><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> authName</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">权限名称，对应接口入参传入的权限</span></td><td key=5></td></tr><tr key=0-3-1-1><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> isAuthorized</span></td><td key=1><span>boolean</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">是否授权</span></td><td key=5></td></tr>
                </tbody>
               </table>