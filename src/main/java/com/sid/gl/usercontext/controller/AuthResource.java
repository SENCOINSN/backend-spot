package com.sid.gl.usercontext.controller;

import com.sid.gl.advisors.AbstractController;
import com.sid.gl.advisors.ApiResponse;
import com.sid.gl.catalogcontext.dtos.SaveSongDTO;
import com.sid.gl.usercontext.application.UserService;
import com.sid.gl.usercontext.domain.User;
import com.sid.gl.usercontext.dto.AuthRequestDTO;
import com.sid.gl.usercontext.dto.JwtResponseDTO;
import com.sid.gl.usercontext.dto.ReadUserDTO;
import com.sid.gl.usercontext.dto.UserRequestDTO;
import com.sid.gl.usercontext.helper.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;*/
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthResource extends AbstractController {
    private Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

    private final UserService userService;

    private final Validator validator;

   /* private final ClientRegistration registration;*/


    /*public AuthResource(UserService userService, ClientRegistrationRepository registrations) {
        this.userService = userService;
        this.registration = registrations.findByRegistrationId("okta");
    }*/

    public AuthResource(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @PostMapping(path = "/login")
    public JwtResponseDTO  login(@RequestBody AuthRequestDTO authRequestDTO){
        LOGGER.info("login process auth {} ",authRequestDTO.getUsernameOrEmail());
        return userService.login(authRequestDTO);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        LOGGER.info("process register user {}",userRequestDTO);

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);
        if (!violations.isEmpty()) {
            String violationsJoined = violations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());
            ProblemDetail validationIssue = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "Validation errors for the fields : " + violationsJoined);
            return ResponseEntity.of(validationIssue).build();
        }else{
            return getResponseEntity(userService.register(userRequestDTO));
        }

    }

    @GetMapping("/get-authenticated-user")
    public ResponseEntity<ReadUserDTO> getAuthenticatedUser() {
            //userService.syncWithIdp(user);
            ReadUserDTO userFromAuthentication = userService.getAuthenticatedUserFromSecurityContextV2();
            LOGGER.info("user from authentification {} ",userFromAuthentication);
            return ResponseEntity.ok().body(userFromAuthentication);

    }

   /* @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String issuerUri = registration.getProviderDetails().getIssuerUri();
        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        Object[] params = {issuerUri, registration.getClientId(), originUrl};
        String logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("logoutUrl", logoutUrl));
    }*/
}
