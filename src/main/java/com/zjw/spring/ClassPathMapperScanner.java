package com.zjw.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;

/**
 * Program Name: micro-mybatis-spring
 * <p>
 * Description: 重写扫描逻辑
 * <p>
 *
 * @author zhangjianwei
 * @version 1.0
 * @date 2019/7/29 12:40 AM
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    private String sqlSessionFactoryBeanName;
    private MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean<Object>();


    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public ClassPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public ClassPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public ClassPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }


    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }


    public void registerFilters() {

        // if (this.markerInterface != null) {
        //     this.addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
        //         protected boolean matchClassName(String className) {
        //             return false;
        //         }
        //     });
        //     acceptAllInterfaces = false;
        // }


        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }


    /**
     * 重写父类的的dosan
     *
     * @param basePackages
     * @return
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No MyBatis mapper was found in '" +
                    Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }


    /**
     * 核心：放置的bd是factoryBean，而非真正的mapper bean
     *
     * @param beanDefinitions
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {


        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            logger.info("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" +
                    definition.getBeanClassName() + "' mapperInterface");

            // 核心：放置的bd是factoryBean，而非真正的mapper bean
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(this.mapperFactoryBean.getClass());

            boolean flag = false;
            if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
                definition.getPropertyValues().add("sqlSessionFactory",
                        new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
                flag = true;
            } else {
                throw new RuntimeException("注册mapperFactoryBean bd失败");
            }

            if (!flag) {
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
            }

        }
    }


    /**
     * 重写父父类 ，最后异步判断是不是候选bean
     *
     * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {

        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.info("不是候选bean" + beanName);
            return false;
        }

    }
}
