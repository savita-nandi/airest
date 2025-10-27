package com.tigerdatademo.tigerdatatest;


import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@ComponentScan(basePackages = {"com.tigerdatademo.tigerdatatest.model","com.tigerdatademo.tigerdatatest.rag"})
@EnableJpaRepositories(basePackages = "com.tigerdatademo.tigerdatatest.model")
@PropertySource("classpath:application.properties")
public class AppConfig {
	
		
	@Value("${spring.datasource.password}")	
	private String DbPwdValue;
	
	@Value("${spring.datasource.url}")	
	private String DbUrl;
	
	@Value("${spring.datasource.username}")	
	private String DbUsername;
	
    @Value("${spring.ai.tavily.api-key}")
    private String tavilyApiKey;
    
    @Value("${spring.ai.tavily.url}")
    private String tavilyUrl;
	
	@Bean(name="entityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
		
		/*Below line fixes the Error:EntityManagerFactory interface seems to conflict with 
		 * Spring's EntityManagerFactoryInfo*/
		lcemfb.setEntityManagerFactoryInterface(EntityManagerFactory.class);
		lcemfb.setDataSource(getDataSource());		
		
		lcemfb.setPackagesToScan(new String[] { "com.tigerdatademo.tigerdatatest.model"});
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		lcemfb.setJpaVendorAdapter(vendorAdapter);
		lcemfb.setJpaProperties(getHibernateProperties());
		lcemfb.afterPropertiesSet();
		return lcemfb.getObject();
	}
	
	@Bean(name="dataSource")
	public DataSource getDataSource() {
		
		DriverManagerDataSource dmds = new DriverManagerDataSource();			
		dmds.setUrl(DbUrl);
		dmds.setUsername(DbUsername);
		dmds.setPassword(DbPwdValue);
		dmds.setDriverClassName("org.postgresql.Driver");		
		return dmds;
		
	}
	
	
	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		/*
		 * properties.put("hibernate.dialect",
		 * environment.getProperty("spring.jpa.properties.hibernate.dialect"));
		 * properties.put("hibernate.show_sql",
		 * environment.getProperty("spring.jpa.show-sql"));
		 * properties.put("hibernate.format_sql",
		 * environment.getProperty("spring.jpa.properties.hibernate.format_sql"));
		 * properties.setProperty("hibernate.naming.physical-strategy",
		 * environment.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
		 */
		
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.format_sql", "true");
		properties.setProperty("hibernate.naming.physical-strategy",
				"org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
		
		return properties;
	}
	
	
	@Bean(name="transactionManager")
	public JpaTransactionManager getJpaTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(getEntityManagerFactory());
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	
}
