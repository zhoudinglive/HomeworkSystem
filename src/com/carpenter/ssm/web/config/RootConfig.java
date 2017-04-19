package com.carpenter.ssm.web.config;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**   
* @Title: RootConfig.java 
* @Package com.carpenter.ssm.web.config 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月3日 下午6:35:28 
* @version V1.0   
*/

@Configuration
@ComponentScan(basePackages={"com.carpenter.ssm.dao", 
					"com.carpenter.ssm.pojo", 
					"com.carpenter.ssm.service",
					"com.carpenter.ssm.service.impl"},
	excludeFilters={
		@Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class)})
public class RootConfig {

}
