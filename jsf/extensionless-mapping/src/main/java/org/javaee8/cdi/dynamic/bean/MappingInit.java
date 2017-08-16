package org.javaee8.cdi.dynamic.bean;

import static javax.faces.application.ViewVisitOption.RETURN_AS_MINIMAL_IMPLICIT_OUTCOME;

import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * @author Arjan Tijms
 */
@WebListener
public class MappingInit implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        sce.getServletContext()
           .getServletRegistrations()
           .values()
           .stream()
           .filter(e -> e.getClassName().equals(FacesServlet.class.getName()))
           .findAny()
           .ifPresent(
               reg -> context.getApplication()
                             .getViewHandler()
                             .getViews(context, "/", RETURN_AS_MINIMAL_IMPLICIT_OUTCOME)
                             .forEach(e -> reg.addMapping(e)));
    }

}
