package com.apisnake.api.security;

import java.net.http.HttpRequest;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Bean
   public ModelMapper modelMapper() {
      ModelMapper modelMapper = new ModelMapper();
      return modelMapper;
   }




  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.csrf().disable();
    http.authorizeRequests()
        .antMatchers("/signin").permitAll()
        .antMatchers("/signup").permitAll()
        .antMatchers(HttpMethod.OPTIONS,"/account").permitAll()//allow CORS option calls
        .antMatchers(HttpMethod.POST, "/account").authenticated();
/*You need to configure the server to not require authorization for OPTIONS requests (that is, the server the request is being sent to — not the one serving your frontend code).

That’s because what’s happening is this:

Your code’s telling your browser it wants to send a request with the Authorization header.
Your browser says, OK, requests with the Authorization header require me to do a CORS preflight OPTIONS to make sure the server allows requests with the Authorization header.
Your browser sends the OPTIONS request to the server without the Authorization header, because the whole purpose of the OPTIONS check is to see if it’s OK to include that header.
Your server sees the OPTIONS request but instead of responding to it in a way that indicates it allows the Authorization header in requests, it rejects it with a 401 since it lacks the header.
Your browser expects a 200 or 204 response for the CORS preflight but instead gets that 401 response. So your browser stops right there and never tries the POST request from your code*/
    http.exceptionHandling().accessDeniedPage("/login");
    http.antMatcher("/account").apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    //APPLIES FILTER TO ACCOUNT, IF YOU ARE AUTHENTICATED YOU WILL RECEIVE THE MESSAGE
    //ELSE YOU WILL GET A CORS ERROR
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


}
