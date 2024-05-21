package com.sid.gl.usercontext.application;

import com.sid.gl.security.JwtService;
import com.sid.gl.usercontext.domain.Profil;
import com.sid.gl.usercontext.domain.User;
import com.sid.gl.usercontext.dto.AuthRequestDTO;
import com.sid.gl.usercontext.dto.JwtResponseDTO;
import com.sid.gl.usercontext.dto.ReadUserDTO;
import com.sid.gl.usercontext.dto.UserRequestDTO;
import com.sid.gl.usercontext.helper.UserContext;
import com.sid.gl.usercontext.mapper.UserMapper;
import com.sid.gl.usercontext.repository.ProfileRepository;
import com.sid.gl.usercontext.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;*/
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ProfileRepository profileRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder, ProfileRepository profileRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.profileRepository = profileRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private void updateUser(User user) {
        LOGGER.info("update user ..{} ",user);
        Optional<User> userToUpdateOpt = userRepository.findOneByEmail(user.getEmail());
        if (userToUpdateOpt.isPresent()) {
            User userToUpdate = userToUpdateOpt.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setImageUrl(user.getImageUrl());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setFirstName(user.getFirstName());
            userRepository.save(userToUpdate);
        }
    }


    public ReadUserDTO register(UserRequestDTO userRequestDTO){
       LOGGER.info("process register user {} ",userRequestDTO);

      Optional<User> opUser = userRepository.findOneByEmail(userRequestDTO.email());
      if(opUser.isPresent()){
          LOGGER.info("User already exist for register");
          throw new RuntimeException("user for register already exists");
      }
      String passH = bCryptPasswordEncoder.encode(userRequestDTO.password());
      User user = userMapper.userRequestDTOToUser(userRequestDTO);
      user.setPassword(passH);

      Profil profil =  getOrcreateProfileIfNotExist("USER");
      user.setProfile(profil);
        User userSaved = userRepository.save(user);
        return userMapper.readUserDTOToUser(userSaved);

    }


    public JwtResponseDTO login(AuthRequestDTO authRequestDTO){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsernameOrEmail(),
                            authRequestDTO.getPassword())
            );

            if(authentication.isAuthenticated()){
                LOGGER.info("successfully authenticated");
                User user = userRepository.findByUsernameOrEmail(authRequestDTO.getUsernameOrEmail(), authRequestDTO.getUsernameOrEmail());
                String tokenAccess = jwtService.GenerateToken(user.getUsername());
                //RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
                JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(tokenAccess,null,user);
                return jwtResponseDTO;
            }

        }catch (BadCredentialsException e){
            LOGGER.error("Failed Authentication");
            throw new BadCredentialsException("Echec de connexion, les infos d'authentifications sont incorrectes !!");
        }
        return null;
    }

    private Profil getOrcreateProfileIfNotExist(String profileName){
        Profil profile = profileRepository.findByProfileName(profileName);
        if(profile ==null){
            Profil profil = new Profil();
            profil.setProfileName(profileName);
            return profileRepository.save(profil);
        }
        return profile;
    }






    /*public void syncWithIdp(User user) {
        *//*Map<String, Object> attributes = oAuth2User.getAttributes();
        User user = mapOauth2AttributesToUser(attributes);*//*
        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            if (attributes.get("updated_at") != null) {
                Instant dbLastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
                Instant idpModifiedDate;
                if(attributes.get("updated_at") instanceof Instant) {
                    idpModifiedDate = (Instant) attributes.get("updated_at");
                } else {
                    idpModifiedDate = Instant.ofEpochSecond((Integer) attributes.get("updated_at"));
                }
                if(idpModifiedDate.isAfter(dbLastModifiedDate)) {
                    updateUser(user);
                }
            }
        } else {
            userRepository.saveAndFlush(user);
        }
    }*/

    public ReadUserDTO getAuthenticatedUserFromSecurityContext(HttpServletRequest request) {
       String  userName = getUsername(request);
       User user = userRepository.findByUsername(userName);

       LOGGER.info("current user authenticated --{}",user);
       return userMapper.readUserDTOToUser(user);
    }


    public ReadUserDTO getAuthenticatedUserFromSecurityContextV2() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        User user = userRepository.findByUsername(userName);

        LOGGER.info("current user authenticated --{}",user);
        return userMapper.readUserDTOToUser(user);
    }

    private String getUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token;
        String username;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
            LOGGER.info("extract username for claim {}", username);
            return username;
        }
      return null;
    }


    private User mapOauth2AttributesToUser(Map<String, Object> attributes) {
        User user = new User();
        String sub = String.valueOf(attributes.get("sub"));

        String username = null;

        if (attributes.get("preferred_username") != null) {
            username = ((String) attributes.get("preferred_username")).toLowerCase();
        }

        if (attributes.get("given_name") != null) {
            user.setFirstName((String) attributes.get("given_name"));
        } else if (attributes.get("name") != null) {
            user.setFirstName((String) attributes.get("name"));
        }

        if (attributes.get("family_name") != null) {
            user.setLastName((String) attributes.get("family_name"));
        }

        if (attributes.get("email") != null) {
            user.setEmail((String) attributes.get("email"));
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            user.setEmail(username);
        } else {
            user.setEmail(sub);
        }

        if (attributes.get("picture") != null) {
            user.setImageUrl((String) attributes.get("picture"));
        }

        return user;
    }

    public Optional<ReadUserDTO> getByEmail(String email) {
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        return oneByEmail.map(userMapper::readUserDTOToUser);
    }

   public boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

}
