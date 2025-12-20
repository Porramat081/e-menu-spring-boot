package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.dto.UserDto;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.User;
import com.porramat081.e_menu.request.LoginRequest;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.response.JwtResponse;
import com.porramat081.e_menu.response.UserDataResponse;
import com.porramat081.e_menu.security.jwt.CookieFunction;
import com.porramat081.e_menu.security.jwt.JwtUtils;
import com.porramat081.e_menu.security.user.WebUserDetail;
import com.porramat081.e_menu.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUserService userService;

    private final CookieFunction cookieFunction;

    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse> getAdmin(HttpServletResponse response){
        try{
            User user = this.userService.getAuthenticatedUser();
            if(user == null){
                throw  new JwtException("Session expired , please login again");
            }
            UserDto userDto = this.userService.convertToDto(user);
            if(userDto != null){
                UserDataResponse userDataResponse = new UserDataResponse(userDto , user.getRoles());
                return ResponseEntity.ok(new ApiResponse("Get user complete" , userDataResponse));
            }
            throw  new JwtException("something wrong");
        }catch (ResourceNotFoundException e){
            cookieFunction.deleteCookie(response);
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch(JwtException e){
            cookieFunction.deleteCookie(response);
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request , HttpServletResponse response){
        try{
            cookieFunction.deleteCookie(response);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),request.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            WebUserDetail userDetail = (WebUserDetail) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetail.getId(),jwt);
            cookieFunction.setCookie(response,jwtResponse.getToken());
            return ResponseEntity.ok(new ApiResponse("Login Successfully",jwtResponse.getId()));
        }catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid email or password "+e.getMessage(),null));
        }
    }
}
