package mini.crm.Repositories;

import mini.crm.Models.Companies;
import mini.crm.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByIdAndCompanyWorkingId(Integer id, Companies companyWorkingId);
    Optional<Users> findByUsername(String username);

    List<Users> findAllByCompanyWorkingId(Companies companyWorkingId);

    Users findByWorklyCodeAndCompanyWorkingId(Integer code, Companies companyId);
}