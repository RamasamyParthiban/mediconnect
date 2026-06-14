package com.mediconnect.doctor_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String email = null;

        String authHead = request.getHeader("Authorization");

        if (authHead != null && authHead.startsWith("Bearer ")) {
            token = authHead.substring(7);
            email = jwtUtils.extractEmail(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtUtils.validateToken(token)) {

                Long userID = jwtUtils.extractUserID(token);

                String role = jwtUtils.extractRole(token);

                String principal = email+":"+userID+":"+role;

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        filterChain.doFilter(request, response);

    }
}
