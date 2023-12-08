package mini.crm.Controllers.Companies;

import lombok.RequiredArgsConstructor;
import mini.crm.Configurations.JWTAuthorization.Authorization;
import mini.crm.Services.Companies.CompaniesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompaniesController {

    private final CompaniesService companiesService;

    @Authorization(requiredRoles = {"SUPERADMIN"})
    @GetMapping("/all")
    public ResponseEntity<?> companyInfo() {

        return new ResponseEntity<>(companiesService.allCompanies(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"SUPERADMIN", "ADMIN", "EMPLOYEE"})
    @GetMapping("/myCompany/info")
    public ResponseEntity<?> myCompanyInfo() {

        return new ResponseEntity<>(companiesService.myCompanyInfo(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"SUPERADMIN"})
    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestParam String name, @RequestParam String adminsUsername,
                                           @RequestParam String adminsPassword, @RequestParam String worklysUsername,
                                           @RequestParam String worklysPassword) {

        return new ResponseEntity<>(companiesService.createCompany(name, adminsUsername, adminsPassword, worklysUsername, worklysPassword), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"SUPERADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable int id) {

        return new ResponseEntity<>(companiesService.deleteCompany(id), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @DeleteMapping("/delete/myCompany")
    public ResponseEntity<?> deleteMyCompany() {

        return new ResponseEntity<>(companiesService.deleteMyCompany(), HttpStatus.OK);
    }
}