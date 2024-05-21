package com.sid.gl.usercontext.helper;

import com.sid.gl.security.JwtService;
import com.sid.gl.usercontext.domain.User;
import com.sid.gl.usercontext.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class UserInterceptor implements HandlerInterceptor {
    private Logger LOGGER = LoggerFactory.getLogger(UserInterceptor.class);

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token;
        String username;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
            LOGGER.info("extract username for claim {}",username);
            UserContext.setCurrentUser(username);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
