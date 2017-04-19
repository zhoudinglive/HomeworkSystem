package com.carpenter.ssm.web.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**   
* @Title: HsWebAppInitializer.java 
* @Package com.carpenter.ssm.web.config 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月3日 下午6:31:29 
* @version V1.0   
*/

public class HsWebAppInitializer 
		extends AbstractAnnotationConfigDispatcherServletInitializer{
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class, DataConfig.class, SecurityConfig.class, RedisHttpSessionConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" }; 
	}
	
}
