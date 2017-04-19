package com.carpenter.ssm.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**   
* @Title: RedisHttpSessionConfig.java 
* @Package com.carpenter.ssm.web.config 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月21日 下午9:05:41 
* @version V1.0   
*/

@EnableRedisHttpSession
public class RedisHttpSessionConfig {
	@Bean
	public JedisConnectionFactory connectionFactory(){
        JedisConnectionFactory connection = new JedisConnectionFactory();
        connection.setHostName("localhost");
        connection.setPort(6379);
        connection.setPassword("password");
        return connection;
	}
	
	@Bean
	public RedisOperationsSessionRepository sessionRepository(){
		return new RedisOperationsSessionRepository(connectionFactory());
	}
	
}
