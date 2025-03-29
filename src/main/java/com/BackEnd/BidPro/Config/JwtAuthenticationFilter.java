package com.BackEnd.BidPro.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
 @Component
 @RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


     private final JwtService jwtService;
     private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String contentType = request.getHeader("Content-Type");
        final String jwt;
        final String userEmail;
        System.out.println("Authorization Header: " + authHeader);
        if(authHeader ==null || !authHeader.startsWith("Bearer ")) {
           // System.out.println("No token found or incorrect format.");
            filterChain.doFilter(request, response);
           // System.out.println(" from here hellllo n");
            return;         // انا هنا بتأكد عموما من صحة  ال token  هل هى valid ولا لا ؟
        }
        jwt = authHeader.substring(7);
        userEmail=jwtService.extractUsername(jwt);


//        System.out.println("Extracted Token: " + jwt); // ✅ Debugging Log
//        System.out.println("Extracted Username: " + userEmail); // ✅ Debugging Log
//        System.out.println("Content-Type: " + contentType);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()== null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)) {
             //   System.out.println("Token is valid. Authenticating user...");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()

                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request, response);

    }
}
