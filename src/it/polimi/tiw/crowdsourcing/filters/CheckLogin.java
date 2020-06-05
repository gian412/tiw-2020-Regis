package it.polimi.tiw.crowdsourcing.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckLogin implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("Login Filter executing"); // TODO: remove after test
        HttpServletRequest request = (HttpServletRequest) servletRequest; // Cast request
        HttpServletResponse response = (HttpServletResponse) servletResponse; // Cast response

        String loginPath = request.getServletContext().getContextPath() + "/login.html"; // Prepare login path

        HttpSession session = request.getSession(); // Get session from the request
        if ( session.isNew() || session.getAttribute("user")==null ) { // If the request is new OR the saved user is null...
            response.sendRedirect(loginPath); // ... redirect to login page
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse); // Pass along the filter chain

    }

    @Override
    public void destroy() {

    }
}
