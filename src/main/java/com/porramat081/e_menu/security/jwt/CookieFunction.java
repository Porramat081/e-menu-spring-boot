package com.porramat081.e_menu.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieFunction {
    @Value("${cookie.name}")
    private String cookieName;
    @Value("${auth.token.expirationInMils}")
    private int expiredTokenTime;
    public void setCookie(HttpServletResponse response , String token){
        Cookie sessionCookie = new Cookie(cookieName,token);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(expiredTokenTime/1000);
        sessionCookie.setAttribute("SameSite", "None");
        sessionCookie.setSecure(true);

        response.addCookie(sessionCookie);
    }
    public void deleteCookie(HttpServletResponse response){
        System.out.println("delete cookie");
        Cookie removedCookie = new Cookie(cookieName,null);
        removedCookie.setHttpOnly(true);
        removedCookie.setSecure(true);
        removedCookie.setPath("/");
        removedCookie.setMaxAge(0);
        removedCookie.setAttribute("SameSite", "None");
        removedCookie.setSecure(true);

        response.addCookie(removedCookie);
    }
}
