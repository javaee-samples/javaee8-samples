package org.javaee8.cdi.dynamic.bean;

import static javax.faces.application.ViewVisitOption.RETURN_AS_MINIMAL_IMPLICIT_OUTCOME;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 
 * @author Arjan Tijms
 */
public class MappingInit implements SystemEventListener {
    
 
	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		
		FacesContext facesContext = event.getFacesContext();
        ServletContext sc = (ServletContext) facesContext.getExternalContext().getContext();
        
        if (Boolean.valueOf((String) sc.getAttribute("mappingsAdded"))) {
        	return;
        }

        Map<String, ? extends ServletRegistration> servletRegistrations = (Map<String, ? extends ServletRegistration>) sc.getAttribute("mappings");
        
        if (servletRegistrations == null) {
        	return;
        }

        MappingServletContextListener.addServletMappings(servletRegistrations, facesContext);
    }
		

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof Application;
	}

}
