package mini.crm.Controllers.Departments;

import lombok.RequiredArgsConstructor;
import mini.crm.Configurations.JWTAuthorization.Authorization;
import mini.crm.Services.Departments.DepartmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<?> allDepartments() {

        return new ResponseEntity<>(departmentsService.allDepartments(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @PostMapping("/create")
    public ResponseEntity<?> createDepartment(String name) {

        return new ResponseEntity<>(departmentsService.createDepartment(name), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable int id, @RequestParam(required = false) String name) {

        return new ResponseEntity<>(departmentsService.updateDepartment(id, name), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable int id) {

        return new ResponseEntity<>(departmentsService.deleteDepartment(id), HttpStatus.OK);
    }
}