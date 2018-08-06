package com.andy.yy.app.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Enumeration;

/**
 * @author richard
 * @since 2018/2/3 23:21
 */
public class SystemPropertiesHelper implements javax.servlet.ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		Enumeration<String> params = context.getInitParameterNames();
		String prefix = "SystemProperty:";
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String value = context.getInitParameter(param);
			if (param != null && param.startsWith(prefix)) {
				System.setProperty(param.substring(prefix.length()), value);
			}
		}
	}
}