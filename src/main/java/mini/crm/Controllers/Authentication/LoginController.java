package mini.crm.Controllers.Authentication;

import lombok.RequiredArgsConstructor;
import mini.crm.Services.Authentication.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(maxAge = 3600)
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        return new ResponseEntity<>(loginService.login(username, password), HttpStatus.OK);
    }
}