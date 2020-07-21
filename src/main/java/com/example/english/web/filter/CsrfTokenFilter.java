//package com.example.english.web.filter;
//
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.security.web.header.Header;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Objects;
//
//public class CsrfTokenFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//
//        if (csrfToken != null) {
//            Cookie cookie = WebUtils.getCookie(request, "X-CSRF-TOKEN");
//            String token = csrfToken.getToken();
//
//            if (cookie == null || !token.equals(cookie.getValue())) {
//                cookie = new Cookie("X-XSRF-TOKEN", token);
//                    response.addHeader("csrf-token2", token);
//                    response.addHeader("X-XSRF-TOKEN", token);
//                    response.addHeader("_csrf", token);
//                    response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
//                response.addCookie(cookie);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}