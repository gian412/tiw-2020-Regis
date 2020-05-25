package it.polimi.tiw.crowdsourcing.filters;

import it.polimi.tiw.crowdsourcing.beans.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckWorker implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("Worker Filter executing"); // TODO: remove after test

        HttpServletRequest request = (HttpServletRequest) servletRequest; // Cast request
        HttpServletResponse response = (HttpServletResponse) servletResponse; // Cast response

        String loginPath = request.getServletContext().getContextPath() + "/Login.htm"; // Prepare login path

        HttpSession session = request.getSession(); // Get session from the request
        User user = null;
        user = (User) session.getAttribute("user"); // Get the user from the session
        if (!(user.getRole().equals("worker"))) { // If the user isn't a Worker...
            response.sendRedirect(loginPath); // ... redirect to login page
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse); // Pass along the filter chain

    }

    @Override
    public void destroy() {

    }
}
