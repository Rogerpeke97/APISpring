package com.apisnake.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apisnake.api.exception.CustomException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;
/*
HttpServletRequest is an interface which exposes getInputStream()  method to read the body. By default, the data from this InputStream can be read only once.
A stream can be defined as a sequence of data. The InputStream is used to read data from a source and the OutputStream is used for writing data to a destination.
A filter is an object used to intercept the HTTP requests and responses of your application. By using filter, we can perform two operations at two instances −

Before sending the request to the controller
Before sending a response to the client.*/

/*java.io.IOException is an exception which programmers use in the code to throw a failure in Input & Output operations.
It is a checked exception. The programmer needs to subclass the IOException and should throw the IOException subclass based on the context*/

/*
class onceperrequestfilter extends genericfilterbean
Filter base class that guarantees to be just executed once per request, on any servlet container. It provides a doFilterInternal method with HttpServletRequest and HttpServletResponse arguments.
Class GenericFilterBean
Simple base implementation of javax.servlet.Filter that treats its config parameters as bean properties. Unknown parameters are ignored. A very handy superclass for any type of filter.
Type conversion is automatic. It is also possible for subclasses to specify required properties.*/

public class JwtTokenFilter extends OncePerRequestFilter {

  private JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(httpServletRequest);
    //CHECKS FOR THE AUTHORIZATION HEADER AND EXECUTES RESOLVETOKEN FROM JWTTOKEN PROVIDER
    // IF THERE IS NO HEADER OR THE HEADER TOKEN IS INVALID
    // YOU WONT BE ABLE TO ACCESS TO /account
    System.out.println(token);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        System.out.println("authenticating in filter TOKEN");
        System.out.println( httpServletRequest.getHeader("Authorization"));
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
      return;
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

}
