/*
 * package com.product.config;
 * 
 * import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException;
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletResponse; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import java.io.IOException; import java.util.List;
 * 
 * @Component public class JwtFilter extends OncePerRequestFilter {
 * 
 * @Autowired private JwtUtil jwtUtil;
 * 
 * @Override protected void doFilterInternal( HttpServletRequest request,
 * HttpServletResponse response, FilterChain chain) throws ServletException,
 * IOException {
 * 
 * try { String authHeader = request.getHeader("Authorization");
 * 
 * // ✅ Only process if Bearer token present if (authHeader != null &&
 * authHeader.length() > 7 && authHeader.startsWith("Bearer ")) {
 * 
 * String token = authHeader.substring(7).trim();
 * 
 * // ✅ Only validate non-empty tokens if (token != null && !token.isEmpty() &&
 * jwtUtil.validateToken(token)) {
 * 
 * String username = jwtUtil.extractUsername(token); String role =
 * jwtUtil.extractRole(token);
 * 
 * // ✅ Null check before setting authentication if (username != null &&
 * !username.isEmpty()) { String safeRole = (role != null) ? role : "ADMIN";
 * 
 * UsernamePasswordAuthenticationToken auth = new
 * UsernamePasswordAuthenticationToken( username, null, List.of(new
 * SimpleGrantedAuthority("ROLE_" + safeRole)) );
 * 
 * SecurityContextHolder.getContext().setAuthentication(auth); } } } } catch
 * (Exception e) { // ✅ Never crash — just clear context and continue
 * SecurityContextHolder.clearContext(); }
 * 
 * // ✅ Always continue filter chain chain.doFilter(request, response); }
 * 
 * @Override protected boolean shouldNotFilter(HttpServletRequest request)
 * throws ServletException { String path = request.getServletPath(); // Do not
 * check for JWT on these paths return path.startsWith("/api/auth/login") ||
 * path.startsWith("/api/auth/register"); } }
 */
package com.product.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        // ✅ Just pass through — no JWT validation for now
        chain.doFilter(request, response);
    }
}
