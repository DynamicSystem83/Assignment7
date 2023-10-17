package edu.jhu.assignment7;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer
{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		System.out.println("<<<<<< SpringApplicationBuilder >>>>>>");
		return application.sources(Assignment7Application.class);
	}

}
