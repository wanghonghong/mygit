package com.jm.application.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;


/**
 * <p>Druid数据源注入</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
@Configuration
@PropertySource({"classpath:/config/database.properties"})
public class DataSourceConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired @Qualifier("primaryDS")
    private DataSource primaryDS;

    @Bean(name = "primaryDS") @Qualifier("primaryDS")
    @Primary
    @ConfigurationProperties(prefix="multi.primary.datasource")
    public DataSource primaryDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        try {
            druidDataSource.setFilters("stat");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return druidDataSource;
    }

    @Bean(name = "entityManagerPrimary")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryPrimary")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
        return builder.dataSource(primaryDS)
                .properties(getVendorProperties(primaryDS))
                .build();
    }

    @Bean(name = "entityManagerSecondary")
    public EntityManager entityManagerSecondary(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
