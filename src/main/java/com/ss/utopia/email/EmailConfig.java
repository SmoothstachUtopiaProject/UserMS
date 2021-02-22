package com.ss.utopia.email;

import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class EmailConfig {
	
	public FreeMarkerConfigurationFactoryBean factoryBean() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("classpath:/templates");
		return bean;
	}
	

}
