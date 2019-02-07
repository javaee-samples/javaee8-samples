package org.javaee8.servlet.mapping;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arjan Tijms
 */
@WebServlet({"/path/*", "*.ext", "", "/", "/exact"})
public class Servlet extends HttpServlet {
     
    private static final long serialVersionUID = 1L;
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
        HttpServletMapping mapping = request.getHttpServletMapping();
        
        System.out.println(mapping);
        System.out.println(mapping.getMappingMatch());
         
        response.getWriter()
                .append("Mapping match:")
                .append(mapping.getMappingMatch().name())
                .append("\n")
                .append("Match value:'")
                .append(mapping.getMatchValue())
                .append("'")
                .append("\n")
                .append("Pattern:'")
                .append(mapping.getPattern())
                .append("'");
    }
 
}
