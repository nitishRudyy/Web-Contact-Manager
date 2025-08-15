package com.scm.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;



@Configuration
public class SecurityConfig { 




    //user create and login using java code with in memeory service
   
    // @Bean
    // public UserDetailsService userDetailsService(){

    //    UserDetails user1 = User
    //    .withDefaultPasswordEncoder()
    //    .username("admin")
    //    .password("admin")
    //    .roles("ADMIN","USER")
    //    .build();

    //    var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);
    //     return inMemoryUserDetailsManager;
    // }
 

    @Autowired
    private SecurityCustomUserDetailService userDetailsService;
     
    @Autowired
    private OAuthAuthenticationSuccessHandler Handler;

    @Autowired
    private authFailureHandler authFailureHandler;

   //Configuring authentication provider to use custom user details service and password encoder 
   @Bean 
   public AuthenticationProvider authenticationProvider(){
       DaoAuthenticationProvider daoauthenticationProvider = new DaoAuthenticationProvider();
       // user detail service ka object
        daoauthenticationProvider.setUserDetailsService(userDetailsService);
        // password encoder ka object
        daoauthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoauthenticationProvider;
    
   }
     

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
         

        //configuration
        
        //url configure kiya hai ki kon se public rhenge air kon se private rhenge
       httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register", "/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });


        //form login default
        //agar hume kuch change krna hua  ham yaha ayenge: form login se related
        httpSecurity.formLogin(formlogin -> {
            formlogin.loginPage("/login");
            formlogin.loginProcessingUrl( "/authenticate");
            formlogin.defaultSuccessUrl("/user/profile");
           //   formlogin.failureUrl("/login?error=true");

            formlogin.usernameParameter("email");
            formlogin.passwordParameter("password");
           
            // formlogin.failureHandler((AuthenticationFailureHandler) new AuthenticationFailureHandler() {
                
                

            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
            // });




            // formlogin.successHandler(new AuthenticationSuccessHandler() {

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
                
            // });

            formlogin.failureHandler(authFailureHandler);


        });
        
        httpSecurity.csrf(AbstractHttpConfigurer::disable); 
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        //oauth config
        httpSecurity.oauth2Login(oauth2login ->{
            oauth2login.loginPage("/login");
            oauth2login.successHandler(Handler);                                           
            //oauth2login.failureUrl("/login?error=true");
        });

        return httpSecurity.build();
     }
     
     @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }

}
