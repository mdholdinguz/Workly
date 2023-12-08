package mini.crm.Controllers.Users;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mini.crm.Configurations.JWTAuthorization.Authorization;
import mini.crm.Services.Users.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mini.crm.Services.Authentication.LoginService.checkAuthorizationAndReturnResult;
import static mini.crm.Services.Authentication.LoginService.returnForResponseEntities;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class UsersController {

    private final UsersService usersService;

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<?> allCompaniesEmployees() {

        return new ResponseEntity<>(usersService.allCompaniesEmployees(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @PostMapping("/create")
    public ResponseEntity<?> createCompaniesEmployee(HttpServletRequest request,
                                                     @RequestParam Integer departmentId, @RequestParam String username,
                                                     @RequestParam String password, @RequestParam String role,
                                                     @RequestParam Integer worklyCode, @RequestParam Integer worklyPass) {

        ResponseEntity<?> response = checkAuthorizationAndReturnResult(request, List.of("ADMIN"),
                () -> new ResponseEntity<>(usersService.createCompaniesEmployee(departmentId, username, password, role.toUpperCase(), worklyCode, worklyPass), HttpStatus.OK));

        return returnForResponseEntities(response);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateCompaniesEmployee(@PathVariable Integer userId, HttpServletRequest request,
                                                      @RequestParam(required = false) Integer departmentId,
                                                      @RequestParam(required = false) String role,
                                                     @RequestParam(required = false) Integer worklyCode,
                                                     @RequestParam(required = false) Integer worklyPass) {

        ResponseEntity<?> response = checkAuthorizationAndReturnResult(request, List.of("ADMIN"),
                () -> new ResponseEntity<>(usersService.updateCompaniesEmployee(userId, departmentId, role, worklyCode, worklyPass), HttpStatus.OK));

        return returnForResponseEntities(response);
    }

    @Authorization(requiredRoles = {"ADMIN", "EMPLOYEE"})
    @PutMapping("/update/myself")
    public ResponseEntity<?> updateMySelf(HttpServletRequest request, @RequestParam(required = false) String password) {

        ResponseEntity<?> response = checkAuthorizationAndReturnResult(request, List.of("ADMIN", "EMPLOYEE"),
                () -> new ResponseEntity<>(usersService.updateMyself(password), HttpStatus.OK));

        return returnForResponseEntities(response);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteCompaniesEmployee(@PathVariable Integer userId, HttpServletRequest request) {

        ResponseEntity<?> response = checkAuthorizationAndReturnResult(request, List.of("ADMIN"),
                () -> new ResponseEntity<>(usersService.deleteCompaniesEmployee(userId), HttpStatus.OK));

        return returnForResponseEntities(response);
    }
}