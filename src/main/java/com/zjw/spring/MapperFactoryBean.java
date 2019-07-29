package com.zjw.spring;

import com.xiongyx.session.SqlSession;
import com.xiongyx.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Program Name: micro-mybatis-spring
 * <p>
 * Description: FactoryBean 实现将dao/mapper注入到spring中
 * <p>
 *
 * @author zhangjianwei
 * @version 1.0
 * @date 2019/7/19 1:42 PM
 */
public class MapperFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    private SqlSession sqlSession;

    private SqlSessionFactory sqlSessionFactory;

    private Class<T> mapperInterface;

    public MapperFactoryBean() {
    }


    /**
     * 扫描到mapper 接口，生成bd，会将bd替换为MapperFactoryBean，同时使用该构造方法，将原mapper bd放入
     *
     * @param mapperInterface
     */
    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public T getObject() {
        return sqlSessionFactory.getSession().getMapper(mapperInterface);
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }


    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }


    public void afterPropertiesSet() throws Exception {
    }

    public SqlSession getSqlSession() {
        return this.sqlSessionFactory.getSession();
    }
}
