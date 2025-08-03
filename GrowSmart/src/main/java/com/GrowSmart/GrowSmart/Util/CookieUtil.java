package com.GrowSmart.GrowSmart.Util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    // Set Secure HttpOnly Cookie
    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
//        response.addCookie(cookie);
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append(name).append("=").append(value).append("; ");
        cookieBuilder.append("Max-Age=").append(expiry).append("; ");
        cookieBuilder.append("Path=/; ");
        cookieBuilder.append("Secure; "); // comment this out if testing over HTTP
        cookieBuilder.append("HttpOnly; ");
        cookieBuilder.append("SameSite=None"); // required for cross-site cookies

        response.addHeader("Set-Cookie", cookieBuilder.toString());
    }

    // Get Cookie Value
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Delete Cookie
    public static void deleteCookie(HttpServletResponse response, String name) {
        setCookie(response, name, "", 0);
    }
}
