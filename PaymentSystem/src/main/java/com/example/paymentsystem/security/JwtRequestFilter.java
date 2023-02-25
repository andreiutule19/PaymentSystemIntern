package com.example.paymentsystem.security;

import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //asta ii null
        final String requestHeader = request.getHeader("Authorization");

        if(!request.getRequestURL().toString().endsWith("/login")){
            String username = null;
            String token = null;

            if(requestHeader != null){
                logger.warn("JWT e null");
            }
            if(requestHeader != null && requestHeader.startsWith("Bearer ")){
                token = requestHeader.substring(7);
                try{
                    username= jwtTokenUtil.getUsernameFromToken(token);
                }
                catch (IllegalArgumentException e){
                    System.out.println("Unable to get Jwt");
                }
                catch (ExpiredJwtException e){
                    System.out.println("Expired Jwt");
                }
            }
            else{
                logger.warn("JWT token dont begin with Bearer ");
            }

            User user = userRepository.findUserByUsername(username);

            if(token == null) response.sendRedirect("/users/login");

            else if(!jwtTokenUtil.validateToken(token,user)){
                response.sendRedirect("/users/login");
            }

        }

        filterChain.doFilter(request,response);

    }
}
