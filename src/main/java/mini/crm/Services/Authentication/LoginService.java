package mini.crm.Services.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mini.crm.Models.Companies;
import mini.crm.Models.Users;
import mini.crm.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsersRepository usersRepository;

    public static String secretKeyString;

    public static Integer USER_ID;
    public static String USERNAME;
    public static String ROLE;
    public static Users USER;
    public static Companies COMPANY;

    @Value("${jwt.secretKey}")
    public void setSecretKeyString(String secretKeyString) {

        LoginService.secretKeyString = secretKeyString;
    }

    public static SecretKey getSecretKey() {

        byte[] secretBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(secretBytes);
    }

    public String login(String username, String password) {

        Users user = usersRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Username Not Found"));

        if (Objects.equals(user.getPassword(), password)) {

            Claims claims = Jwts.claims();

            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            claims.put("role", user.getRole());
            claims.put("user", user);
            claims.put("company", user.getCompanyWorkingId());

            // Set expiration time to 1 week
            Date expiration = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(expiration)
                    .signWith(getSecretKey())
                    .compact();
        }
        else {

            throw new IllegalArgumentException("Password Didn't Match!");
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseToken(HttpServletRequest request) throws Exception {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.toLowerCase().startsWith("bearer")) {

            String token = authorizationHeader.substring("Bearer".length()).trim();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();

            if (expiration != null && expiration.before(new Date())) {

                throw new Exception("Token is expired");
            }

            Map<String, Object> result = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();

            LinkedHashMap<String, Object> userMap = (LinkedHashMap<String, Object>) claims.get("user");
            LinkedHashMap<String, Object> companyMap = (LinkedHashMap<String, Object>) claims.get("company");

            Users user = mapper.convertValue(userMap, Users.class);
            Companies company = mapper.convertValue(companyMap, Companies.class);

            result.put("id", claims.get("id"));
            result.put("username", claims.get("username"));
            result.put("role", claims.get("role"));
            result.put("user", user);
            result.put("company", company);

            USER_ID = (Integer) claims.get("id");
            USERNAME = (String) claims.get("username");
            ROLE = (String) claims.get("role");
            USER = user;
            COMPANY = company;

            return result;
        }

        return null;
    }

    public static boolean isAuthorized(HttpServletRequest request, List<String> requiredRoles) throws Exception {

        Map<String, Object> tokenData = parseToken(request);

        if (tokenData != null) {

            String role = (String) tokenData.get("role");

            return !requiredRoles.contains(role);
        }

        return true;
    }

    public static ResponseEntity<?> checkAuthorizationAndReturnResult(HttpServletRequest request,
                                                                      List<String> requiredRoles,
                                                                      Supplier<Object> resultSupplier) {

        try {

            if (isAuthorized(request, requiredRoles)) {

                return new ResponseEntity<>("Only Admin Can Access This Page!", HttpStatus.UNAUTHORIZED);
            }
            else {

                Object result = resultSupplier.get();

                if (result == null) {

                    return new ResponseEntity<>("Nothing To Show", HttpStatus.OK);
                }
                else {

                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            }
        }
        catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {

            return new ResponseEntity<>("JWT EXPIRED: " + e, HttpStatus.UNAUTHORIZED);
        }
    }

    public static ResponseEntity<?> returnForResponseEntities(ResponseEntity<?> response) {

        if (response.getBody() instanceof ResponseEntity) {

            return (ResponseEntity<?>) response.getBody();
        }
        else {

            return response;
        }
    }
}