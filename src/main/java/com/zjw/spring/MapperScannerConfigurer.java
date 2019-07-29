package com.zjw.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Program Name: micro-mybatis-spring
 * <p>
 * Description: 作用：扫描工程内mapper所在的包，统一加载所有mapper
 * MapperScannerConfigurer 使用是需要实例化此类的对象，设置basePackage 进行初始化
 * <p>
 *
 * @author zhangjianwei
 * @version 1.0
 * @date 2019/7/28 10:38 PM
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    /**
     * SessionFactory的名称，而非ref
     */
    private String sqlSessionFactoryBeanName;
    /**
     * mapper接口所在包
     */
    private String basePackage;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 根据指定的classpath 从spring上下文中扫描候选bd
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
