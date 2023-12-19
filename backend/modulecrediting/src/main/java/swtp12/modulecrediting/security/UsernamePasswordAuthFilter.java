package swtp12.modulecrediting.security;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import swtp12.modulecrediting.dto.CredentialsDTO;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter{
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest, 
            HttpServletResponse httpServletResponse, 
            FilterChain filterChain) throws ServletException, IOException {
        
        if ("/v1/signIn".equals(httpServletRequest.getServletPath()) && HttpMethod.POST.matches(httpServletRequest.getMethod())) {
            CredentialsDTO credentialsDto = MAPPER.readValue(httpServletRequest.getInputStream(), CredentialsDTO.class);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(credentialsDto.getUsername(), credentialsDto.getPassword()));
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
