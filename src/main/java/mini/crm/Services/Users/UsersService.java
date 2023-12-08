package mini.crm.Services.Users;

import lombok.RequiredArgsConstructor;
import mini.crm.Models.Departments;
import mini.crm.Models.Users;
import mini.crm.Repositories.DepartmentsRepository;
import mini.crm.Repositories.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static mini.crm.Services.Authentication.LoginService.COMPANY;
import static mini.crm.Services.Authentication.LoginService.USER_ID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final DepartmentsRepository departmentsRepository;

    public List<Users> allCompaniesEmployees() {

        return usersRepository.findAllByCompanyWorkingId(COMPANY);
    }

    public String createCompaniesEmployee(Integer departmentId, String username, String password, String role, Integer worklyCode, Integer worklyPass) {

        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("EMPLOYEE")) {

            throw new IllegalArgumentException("Role Should Be Or Admin Or Employee!");
        }

        Users user = usersRepository.findByWorklyCodeAndCompanyWorkingId(worklyCode, COMPANY);

        if (user != null) {

            throw new IllegalArgumentException("This Workly Code Already Exists!");
        }

        Departments department = departmentsRepository.findByIdAndCompanyId(departmentId, COMPANY).orElseThrow(() -> new IllegalArgumentException("Department Not Found!"));

        try {

            user = new Users();

            user.setDepartmentId(department);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role.toUpperCase());
            user.setCompanyWorkingId(COMPANY);
            user.setWorklyCode(worklyCode);
            user.setWorklyPass(worklyPass);

            usersRepository.save(user);

            return "You Successfully Created " + username;
        }
        catch (DataIntegrityViolationException e) {

            throw new IllegalArgumentException("Username Already Exists!");
        }
    }

    public String updateCompaniesEmployee(int userId, Integer departmentId, String role, Integer worklyCode, Integer worklyPass) {

        Users user = usersRepository.findByIdAndCompanyWorkingId(userId, COMPANY).orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if (role != null) {

            user.setRole(role.toUpperCase());
        }

        if (departmentId != null) {

            Departments department = departmentsRepository.findByIdAndCompanyId(departmentId, COMPANY).orElseThrow(() -> new IllegalArgumentException("Department Not Found!"));

            user.setDepartmentId(department);
        }

        if (worklyCode != null) {

            Users ifExists = usersRepository.findByWorklyCodeAndCompanyWorkingId(worklyCode, COMPANY);

            if (ifExists != null) {

                throw new IllegalArgumentException("This Workly Code Already Exists!");
            }

            user.setWorklyCode(worklyCode);
        }

        if (worklyPass != null) {

            user.setWorklyPass(worklyPass);
        }

        usersRepository.save(user);

        return "You Successfully Updated " + user.getUsername();
    }

    public String updateMyself(String password) {

        Users user = usersRepository.findById(USER_ID).orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if (password != null) {

            user.setPassword(password);
        }

        usersRepository.save(user);

        return "You Successfully Updated Yourself";
    }

    public String deleteCompaniesEmployee(int userId) {

        Users user = usersRepository.findByIdAndCompanyWorkingId(userId, COMPANY).orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        usersRepository.delete(user);

        return "You Successfully Deleted " + user.getUsername();
    }
}