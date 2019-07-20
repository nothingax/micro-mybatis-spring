# micro-mybatis-spring


适配包解决的问题/作用：使用baits在spring中，在spring容器的管理下，使用更加方便，
创建sqlsessionFactory的工作减少
所有首先需要熟悉原生batis框架是如何使用的。

基于此，适配包能做的工作有：管理SqlSessionFactory，创建SqlSession。
几乎没有

MyBatis相信很多人都会使用，但是当MyBatis整合到了Spring中，我们发现在Spring中使用更加方便了。
例如获取Dao的实例，在Spring的我们只需要使用注入的方式就可以了使用Dao了，
完全不需要调用SqlSession的getMapper方法去获取Dao的实例，
，对于插入操作也不需要我们commit。


toy : connection 是druid query的执行时由connection执行的



Statement  java.sql.Statement.execute(java.lang.String, int)

Statement 是rt sql api 实现有jdbc
执行

