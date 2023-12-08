package mini.crm.Services.WorkingTimes;

import lombok.RequiredArgsConstructor;
import mini.crm.Models.Users;
import mini.crm.Models.WorkingTimes;
import mini.crm.Repositories.UsersRepository;
import mini.crm.Repositories.WorkingTimesRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static mini.crm.Services.Authentication.LoginService.COMPANY;

@Service
@RequiredArgsConstructor
public class WorkingTimesService {

    private final WorkingTimesRepository workingTimesRepository;
    private final UsersRepository usersRepository;

    public List<WorkingTimes> allWorkingTimesToday() {

        return workingTimesRepository.findAllByCompanyIdAndDate(COMPANY, LocalDate.now());
    }

    public List<WorkingTimes> allWorkingTimesThisWeek() {

        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);

        return workingTimesRepository.findAllByCompanyIdAndDateBetween(COMPANY, startOfWeek, endOfWeek);
    }

    public List<WorkingTimes> allWorkingTimesThisMonth() {

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return workingTimesRepository.findAllByCompanyIdAndDateBetween(COMPANY, startOfMonth, endOfMonth);
    }

    public List<WorkingTimes> allWorkingTimesThisYear() {

        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate endOfYear = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());

        return workingTimesRepository.findAllByCompanyIdAndDateBetween(COMPANY, startOfYear, endOfYear);
    }

    public List<WorkingTimes> allWorkingTimesBetweenDates(LocalDate startDate, LocalDate endDate) {

        return workingTimesRepository.findAllByCompanyIdAndDateBetween(COMPANY, startDate, endDate);
    }

    private Users checkWorklyCodeAndPass(int worklyCode, int worklyPass) {

        Users user = usersRepository.findByWorklyCodeAndCompanyWorkingId(worklyCode, COMPANY);

        if (user == null) {

            throw new IllegalArgumentException("User Not Found!");
        }

        if (!user.getWorklyPass().equals(worklyPass)) {

            throw new IllegalArgumentException("Passwords Didn't Match!");
        }

        return user;
    }

    public String setArrival(int worklyCode, int worklyPassword, String imageLink) {

        Users user = checkWorklyCodeAndPass(worklyCode, worklyPassword);

        LocalDate today = LocalDate.now();

        WorkingTimes workingTime = workingTimesRepository.findByUserIdAndDate(user, today).orElse(new WorkingTimes());

        if (workingTime.getArrivalTime() == null) {

            workingTime.setDate(today);
            workingTime.setArrivalTime(LocalDateTime.now());
            workingTime.setCompanyId(user.getCompanyWorkingId());
            workingTime.setUserId(user);
            workingTime.setArrivalImageLink(imageLink);

            workingTimesRepository.save(workingTime);
        }
        else {

            throw new IllegalArgumentException("You Already Arrived Today!");
        }

        return "You Successfully Arrived. Your Arrival Time: " + workingTime.getArrivalTime();
    }

    public String setExit(int worklyCode, int worklyPassword, String imageLink) {

        Users user = checkWorklyCodeAndPass(worklyCode, worklyPassword);

        LocalDate today = LocalDate.now();

        WorkingTimes workingTime = workingTimesRepository.findByUserIdAndDate(user, today).orElseThrow(() -> new IllegalArgumentException("You Didn't Arrive Yet!"));

        if (workingTime.getExitTime() == null) {

            workingTime.setExitTime(LocalDateTime.now());
            workingTime.setExitImageLink(imageLink);

            workingTimesRepository.save(workingTime);

            return "You Successfully Exited! Your Exit Time: " + workingTime.getExitTime();
        }
        else {

            throw new IllegalArgumentException("You Already Exited Today!");
        }
    }
}