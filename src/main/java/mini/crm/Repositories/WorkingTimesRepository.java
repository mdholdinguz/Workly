package mini.crm.Repositories;

import mini.crm.Models.Companies;
import mini.crm.Models.Users;
import mini.crm.Models.WorkingTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingTimesRepository extends JpaRepository<WorkingTimes, Integer> {

    Optional<WorkingTimes> findByUserIdAndDate(Users userId, LocalDate date);

    List<WorkingTimes> findAllByCompanyIdAndDate(Companies companyId, LocalDate date);

    List<WorkingTimes> findAllByCompanyIdAndDateBetween(Companies company, LocalDate startOfWeek, LocalDate endOfWeek);
}