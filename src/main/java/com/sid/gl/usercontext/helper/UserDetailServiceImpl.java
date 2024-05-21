package com.sid.gl.usercontext.helper;

import com.sid.gl.usercontext.domain.User;
import com.sid.gl.usercontext.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.debug("load by username ... {}",usernameOrEmail);
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
        if(user==null){
            logger.error("username not found for load .... info");
            throw new RuntimeException("Could not found user !!!");
        }
        logger.info("user authenticated successfully !!!");
        return new CustomUserDetails(user);
    }
}
