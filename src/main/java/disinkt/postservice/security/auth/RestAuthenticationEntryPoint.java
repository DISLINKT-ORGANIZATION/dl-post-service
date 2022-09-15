package disinkt.postservice.security.auth;

import disinkt.postservice.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final TokenUtils tokenUtils;

    @Autowired
    public RestAuthenticationEntryPoint(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String authToken = tokenUtils.getToken(request);

        if (authToken != null) {
            String username = tokenUtils.getUsernameFromToken(authToken);
            if (username != null) {
                RestTemplate restTemplate = new RestTemplate();
                String fooResourceUrl = "http://localhost:8081/auth-service/authentication/users/check-username/" + username;
                ResponseEntity<Boolean> restTemplateResponse = restTemplate.getForEntity(fooResourceUrl, Boolean.class);
                if (Boolean.FALSE.equals(restTemplateResponse.getBody())) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }
}
