package ru.job4j.accidents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Класс конфигурации подключения к базе данных c помощью Hibernate
 */
@Configuration
@EnableTransactionManagement
public class HbmConfig {

    /**
     * Инстанцирует объект LocalSessionFactoryBean
     * @param dialect значение свойства hibernate.dialect из файла
     * application.properties
     * @param ds DataSource
     * @return LocalSessionFactoryBean
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory(
                 @Value("${hibernate.dialect}") String dialect, DataSource ds) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("ru.job4j.accidents.model");
        sessionFactory.setDataSource(ds);
        Properties cfg = new Properties();
        cfg.setProperty("hibernate.dialect", dialect);
        cfg.setProperty("hibernate.show_sql", "true");
        sessionFactory.setHibernateProperties(cfg);
        return sessionFactory;
    }
}
