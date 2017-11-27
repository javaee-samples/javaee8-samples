package org.javaee8.servlet.push;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.PushBuilder;
import javax.ws.rs.core.MediaType;

@WebServlet({"/servlet"})
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(MediaType.TEXT_HTML_TYPE.withCharset("utf-8").toString());
        response.setStatus(200);

        PushBuilder builder = request.newPushBuilder();

        // If server push isn't supported, return that in the result.
        if (builder == null) {
            response.getWriter()
                    .append("<p>The image below was sent normally using HTTP 1.1.</p>");
        } else {
            response.getWriter()
                    .append("<p>The image below was pushed using HTTP 2.0.</p>");
            builder.path("/images/payara-logo.jpg")
                    .push();
        }
        response.getWriter().append("<img src=\"/images/payara-logo.jpg\" />");
    }

}
