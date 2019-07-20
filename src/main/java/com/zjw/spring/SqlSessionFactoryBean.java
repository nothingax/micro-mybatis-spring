package com.zjw.spring;

import com.xiongyx.builder.XMLConfigBuilder;
import com.xiongyx.datasource.DataSource;
import com.xiongyx.environment.Environment;
import com.xiongyx.model.Configuration;
import com.xiongyx.session.SqlSessionFactory;
import com.xiongyx.session.SqlSessionFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;

/**
 * Program Name: micro-mybatis-spring
 * <p>
 * Description:
 * <p>
 *
 * @author zhangjianwei
 * @version 1.0
 * @date 2019/7/19 1:02 AM
 */
@Slf4j
public class SqlSessionFactoryBean implements InitializingBean, FactoryBean {
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    private SqlSessionFactory sqlSessionFactory;
    private DataSource dataSource;
    private Resource configLocation;
    private Configuration configuration;
    private Resource[] mapperLocations;
    //
    // public abstract class DaoSupport implements InitializingBean {
    //     protected final Log logger = LogFactory.getLog(this.getClass());
    //
    //     public DaoSupport() {
    //     }
    //
    //
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init");
        notNull(dataSource, "Property 'dataSource' is required");
        notNull(sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
        state((configuration == null && configLocation == null) || !(configuration != null && configLocation != null),
                "Property 'configuration' and 'configLocation' can not specified with together");
        this.sqlSessionFactory = buildSqlSessionFactory();
    }


    /**
     * 构造 SqlSessionFactory
     *
     * @return
     */
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        Configuration configuration = null;
        XMLConfigBuilder xmlConfigBuilder = null;

        // 根据xml配置，解析到Configuration
        if (this.configuration != null) {
            configuration = this.configuration;
        } else if (this.configLocation != null) {
            xmlConfigBuilder = new XMLConfigBuilder(new InputStreamReader(this.configLocation.getInputStream()),
                    null/*TODO properties 可以加入*/);
        } else {
            log.info("Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");
            throw new RuntimeException("无Configuration");
            // configuration = new Configuration();
        }

        if (xmlConfigBuilder != null) {
            try {
                configuration = xmlConfigBuilder.parse();
                log.info("Parsed configuration file: '" + this.configLocation + "'");
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            } finally {
                // ErrorContext.instance().reset();
            }
        }

        // 设置数据源
        configuration.setEnvironment(new Environment(this.dataSource));

        // TODO 事务管理接入

        // TODO 修改batis
        return this.sqlSessionFactoryBuilder.privateBuild(configuration);
    }

    public Object getObject() throws Exception {
        if (sqlSessionFactory == null) {
            afterPropertiesSet();
        }

        return this.sqlSessionFactory;
        // return new Person().setName("23");
    }

    public Class<?> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    public boolean isSingleton() {
        return true;
    }


    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }
}
