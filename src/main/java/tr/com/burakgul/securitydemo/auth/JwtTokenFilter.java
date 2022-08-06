package tr.com.burakgul.securitydemo.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tr.com.burakgul.securitydemo.service.UserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final TokenManager tokenManager;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenCore = request.getHeader("Authorization");

        /**
         * Authorization: Bearer askdlşkajlsjdljasd.asdjlasdjasjdkas.asjkldhakjshdkjahs
         */

        String token = null;
        String username = null;

        if (tokenCore != null && tokenCore.contains("Bearer") && tokenCore.split(" ").length > 1) {
            token = tokenCore.split(" ")[1];
            try {
                username = this.tokenManager.getUsernameFromToken(token);
            } catch (Exception e) {
                this.LOGGER.error(e.getMessage());
            }
        }

        if (token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (this.tokenManager.hasTokenValid(token)) {
                UserDetails user = this.userDetailService.loadUserByUsername(username);
                if (Objects.nonNull(user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
