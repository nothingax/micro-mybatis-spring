# micro-mybatis-spring*
简易版Mybatis-Spring适配包
micro-mybatis-spring是toy-Mybatis与spring5.0适配包


* [toy-mybatis](https://github.com/1399852153/toy-framework)
* [集成micro-mybatis-spring 与toy-mybatis 的demo](https://github.com/nothingax/micro-mybatis-spring-demo)


## 2019-07-28 22:24:23
新增MapperFactoryBean
实现以统一的方式获取mapper代理对象,使用一factoryBean的方式获取mapper代理,同时管理session的开启，不需要手动开启session。



## 2019-07-29 16:13:05
实现 mapper 接口扫描，使用该配置后不必注册每个mapper接口，配置MapperScannerConfigurer，并设置包名，即可扫描包下所有的mapper接口
使用方式：配置MapperScannerConfigurer bean，设置属性即可

