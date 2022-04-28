package com.example.demo.filters;

import com.example.demo.util.JwtUtil;
import com.example.demo.wrappers.MutableHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        String requestPath = new URL(request.getRequestURL().toString()).getPath();
        Boolean userIsSigningUp = requestPath.equals("/user") && request.getMethod().equals("POST");
        Boolean userIsSigningIn = requestPath.equals("/sign-in");
        Boolean userIsSigningOut = requestPath.equals("/token") && request.getMethod().equals("DELETE");
        Boolean userIsAuthenticating = requestPath.equals("/token") && request.getMethod().equals("POST");

        if (userIsSigningIn || userIsSigningUp || userIsAuthenticating || userIsSigningOut) {
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("authorization");
        System.out.println(authorizationHeader);

        String id = null;
        String jwt = null;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(401);
            return;
        }

        try {
            jwt = authorizationHeader.substring(7);
            id = jwtUtil.extractId(jwt);
        } catch (Exception e) {
            if (e.getMessage().startsWith("JWT expired")) {
                response.sendError(403);
            } else {
                response.sendError(500);
            }
            return;
        }

        if (id == null) {
            response.sendError(403);
            return;
        }

        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
        mutableRequest.putHeader("id", id);
        chain.doFilter(mutableRequest, response);

    }

}
