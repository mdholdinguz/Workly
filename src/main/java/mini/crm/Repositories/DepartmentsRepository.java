package mini.crm.Repositories;

import mini.crm.Models.Companies;
import mini.crm.Models.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Integer> {

    Optional<Departments> findByIdAndCompanyId(Integer id, Companies companyId);
    Optional<Departments> findByNameAndCompanyId(String name, Companies companyId);
    List<Departments> findAllByCompanyId(Companies companyId);
}