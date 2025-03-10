package at.backend.tourist.places.core.Config;

import at.backend.tourist.places.modules.Auth.DTOs.LoginResponseDTO;
import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.modules.User.Service.UserService;
import at.backend.tourist.places.core.Utils.Enum.Role;
import at.backend.tourist.places.modules.Auth.JWT.JwtAuthenticationFilter;
import at.backend.tourist.places.modules.Auth.JWT.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        .requestMatchers("/signup", "/login", "/auth/**").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // User
                        .requestMatchers("/v1/api/users/me", "/v1/api/users/lists/**").authenticated()
                        .requestMatchers("/v1/api/users/admin").hasAuthority("ROLE_ADMIN")

                        // Public Access
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // Requires Login
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
                        .successHandler(this::successHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                           jwtAuthFilter.validateTokenFormat(request, response, authException);
                        })
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return request -> {
            OAuth2User oauth2User = delegate.loadUser(request);
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setName(name);
                        newUser.setProvider("google");
                        newUser.setActivated(true);
                        newUser.setRole(Role.VIEWER);
                        return userRepository.save(newUser);
                    });

            Map<String, Object> attributes = oauth2User.getAttributes();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            return new DefaultOAuth2User(authorities, attributes, "email");
        };
    }

    private void successHandler(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        String role = oauth2User.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(Role.VIEWER.toString());

        System.out.println("Roles: " + oauth2User.getAuthorities());

        User user = userRepository.findByEmail(email).orElseThrow( () -> new EntityNotFoundException("User Not Found"));
        LoginResponseDTO responseDTO = jwtService.generateLoginTokens(email, user.getId(), role);

        response.setContentType("application/json");
        response.getWriter().write("{\"tokens\":\"" + responseDTO + "\"}");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Access denied: insufficient authorities\"}");
        };
    }
}
