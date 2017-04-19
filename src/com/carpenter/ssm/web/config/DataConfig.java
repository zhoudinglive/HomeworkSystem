package com.carpenter.ssm.web.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**   
* @Title: DataConfig.java 
* @Package com.carpenter.ssm.web.config 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月6日 下午1:35:00 
* @version V1.0   
*/

@Configuration
@MapperScan("com.carpenter.ssm.dao")
public class DataConfig {
	@Bean
	public ComboPooledDataSource dataSource() throws Exception{
		ComboPooledDataSource cpds = new ComboPooledDataSource(true);  
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setJdbcUrl("jdbc:mysql://localhost");
			cpds.setUser("root");
			cpds.setPassword("password");
			cpds.setMaxPoolSize(10);
			cpds.setMinPoolSize(3);
			cpds.setInitialPoolSize(10);
			cpds.setMaxIdleTime(3600000);
			cpds.setCheckoutTimeout(3000);
			cpds.setAcquireRetryAttempts(5);
			cpds.setAcquireRetryDelay(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cpds;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManger() throws Exception{
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/carpenter/ssm/dao/mappers/*.xml"));
		return sessionFactory;
	}
	
}
