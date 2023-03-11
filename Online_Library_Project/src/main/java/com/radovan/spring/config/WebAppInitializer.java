package com.radovan.spring.config;



import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		
		AnnotationConfigWebApplicationContext webContext = 
				new AnnotationConfigWebApplicationContext();
		webContext.register(SpringMvcConfiguration.class);
		ServletRegistration.Dynamic initializer = 
				servletContext.addServlet("Spring Initializer", new DispatcherServlet(webContext));
		
		initializer.setLoadOnStartup(1);
		initializer.addMapping("/");
		
	}
	
	

}
