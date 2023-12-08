package mini.crm.Repositories;

import mini.crm.Models.Companies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompaniesRepository extends JpaRepository<Companies, Integer> {

    Optional<Companies> findByName(String name);
}