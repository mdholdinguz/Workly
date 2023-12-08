package mini.crm.Services.Companies;

import lombok.RequiredArgsConstructor;
import mini.crm.Models.Companies;
import mini.crm.Models.Users;
import mini.crm.Repositories.CompaniesRepository;
import mini.crm.Repositories.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static mini.crm.Services.Authentication.LoginService.COMPANY;

@Service
@RequiredArgsConstructor
public class CompaniesService {

    private final UsersRepository usersRepository;
    private final CompaniesRepository companiesRepository;

    public List<Companies> allCompanies() {

        return companiesRepository.findAll(Sort.by("name"));
    }

    public Companies myCompanyInfo() {

        if (COMPANY == null) {

            throw new IllegalArgumentException("You Are Not In A Company!");
        }

        return companiesRepository.findById(COMPANY.getId()).orElse(null);
    }

    public String createCompany(String name, String adminsUsername, String adminsPassword, String worklysUsername, String worklysPassword) {

        Optional<Companies> existingCompany = companiesRepository.findByName(name);

        if (existingCompany.isPresent()) {

            throw new IllegalArgumentException("Company " + name + " Already Exists!");
        }

        Optional<Users> existingUser = usersRepository.findByUsername(adminsUsername);

        if (existingUser.isPresent()) {

            throw new IllegalArgumentException(adminsUsername + " Already Exists!");
        }

        Optional<Users> existingWorklyUsername = usersRepository.findByUsername(worklysUsername);

        if (existingWorklyUsername.isPresent()) {

            throw new IllegalArgumentException(worklysUsername + " Already Exists!");
        }

        Companies company = new Companies();

        company.setName(name);
        company.setCreationDate(LocalDateTime.now());

        companiesRepository.save(company);

        Users user = new Users();

        user.setUsername(adminsUsername);
        user.setPassword(adminsPassword);
        user.setRole("ADMIN");
        user.setCompanyWorkingId(company);

        usersRepository.save(user);

        user = new Users();

        user.setUsername(worklysUsername);
        user.setPassword(worklysPassword);
        user.setRole("WORKLY");
        user.setCompanyWorkingId(company);

        usersRepository.save(user);

        return "You Successfully Created " + name + ".\n\nLogin: " + adminsUsername +
                "\nPassword: " + adminsPassword +
                "\nWorkly account: " + worklysUsername + "\nPassword: " + worklysPassword;
    }

    public String deleteCompany(int companyId) {

        Companies company = companiesRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("Company Not Found"));

        companiesRepository.delete(company);

        return "You Successfully Deleted " + company.getName() + "!";
    }

    public String deleteMyCompany() {

        if (COMPANY == null) {

            throw new IllegalArgumentException("You Don't Have Company!");
        }

        companiesRepository.delete(COMPANY);

        return "You Successfully Deleted Your Company!";
    }
}