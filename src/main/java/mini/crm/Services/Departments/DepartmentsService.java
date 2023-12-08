package mini.crm.Services.Departments;

import lombok.RequiredArgsConstructor;
import mini.crm.Models.Departments;
import mini.crm.Models.Users;
import mini.crm.Repositories.DepartmentsRepository;
import mini.crm.Repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static mini.crm.Services.Authentication.LoginService.COMPANY;

@Service
@RequiredArgsConstructor
public class DepartmentsService {

    private final DepartmentsRepository departmentsRepository;
    private final UsersRepository usersRepository;

    public List<Departments> allDepartments() {

        return departmentsRepository.findAllByCompanyId(COMPANY);
    }

    public String createDepartment(String name) {

        Optional<Departments> existingDepartment = departmentsRepository.findByNameAndCompanyId(name, COMPANY);

        if (existingDepartment.isPresent()) {

            throw new IllegalArgumentException("Department " + name + " Already Exists!");
        }

        Departments department = new Departments();

        department.setCompanyId(COMPANY);
        department.setName(name);

        departmentsRepository.save(department);

        return "You Successfully Created " + name;
    }

    public String updateDepartment(Integer departmentId, String name) {

        Departments department = departmentsRepository.findByIdAndCompanyId(departmentId, COMPANY).orElseThrow(() -> new IllegalArgumentException("Department Not Found!"));

        if (name != null) {

            department.setName(name);
        }

        departmentsRepository.save(department);

        return "You Successfully Updated Department!";
    }

    public String deleteDepartment(Integer departmentId) {

        Departments department = departmentsRepository.findByIdAndCompanyId(departmentId, COMPANY).orElseThrow(() -> new IllegalArgumentException("Department Not Found!"));

        List<Users> usersList = usersRepository.findAllByCompanyWorkingIdAndDepartmentId(COMPANY, department);

        usersList.forEach(user -> user.setDepartmentId(null));

        usersRepository.saveAll(usersList);

        departmentsRepository.delete(department);

        return "You have successfully deleted department " + department.getName();
    }
}