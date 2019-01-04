package org.javaee8.cdi.dynamic.bean;

import static javax.faces.application.ViewVisitOption.RETURN_AS_MINIMAL_IMPLICIT_OUTCOME;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class MappingServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc =  sce.getServletContext();

		sc.setAttribute("mappings", sce.getServletContext().getServletRegistrations());
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
			//It 's possible that JSF isn't available at this time depending on JSF implementation and Java server container
			return;
		}
        
        addServletMappings(sc.getServletRegistrations(), facesContext);
        
        //Add a flag to not add the mappings again later in MappingInit
        sc.setAttribute("mappingsAdded", "true");
	}

	public static void addServletMappings(Map<String, ? extends ServletRegistration> servletRegistrations, FacesContext facesContext) {
        servletRegistrations.values().stream().filter(e -> e.getClassName().equals(FacesServlet.class.getName()))
        .findAny()
        .ifPresent(reg -> facesContext.getApplication().getViewHandler().getViews(
        		facesContext, "/", RETURN_AS_MINIMAL_IMPLICIT_OUTCOME).forEach(e -> reg.addMapping(e)));
	}

}
