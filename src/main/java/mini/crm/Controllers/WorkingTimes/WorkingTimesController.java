package mini.crm.Controllers.WorkingTimes;

import lombok.RequiredArgsConstructor;
import mini.crm.Configurations.JWTAuthorization.Authorization;
import mini.crm.Services.WorkingTimes.WorkingTimesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/Working-Times")
@RequiredArgsConstructor
public class WorkingTimesController {

    private final WorkingTimesService workingTimesService;

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/allToday")
    public ResponseEntity<?> allWorkingTimesToday() {

        return new ResponseEntity<>(workingTimesService.allWorkingTimesToday(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/allWeek")
    public ResponseEntity<?> allWorkingTimesThisWeek() {

        return new ResponseEntity<>(workingTimesService.allWorkingTimesThisWeek(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/allMonth")
    public ResponseEntity<?> allWorkingTimesThisMonth() {

        return new ResponseEntity<>(workingTimesService.allWorkingTimesThisMonth(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/allYear")
    public ResponseEntity<?> allWorkingTimesThisYear() {

        return new ResponseEntity<>(workingTimesService.allWorkingTimesThisYear(), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"ADMIN"})
    @GetMapping("/allCustomDate")
    public ResponseEntity<?> allWorkingTimesBetweenDates(@RequestParam LocalDate start, @RequestParam LocalDate end) {

        return new ResponseEntity<>(workingTimesService.allWorkingTimesBetweenDates(start, end), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"WORKLY"})
    @PostMapping("/setArrival")
    public ResponseEntity<?> setArrival(@RequestParam int worklyCode, @RequestParam int worklyPass) {

        return new ResponseEntity<>(workingTimesService.setArrival(worklyCode, worklyPass), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"WORKLY"})
    @PostMapping("/setExit")
    public ResponseEntity<?> setExit(@RequestParam int worklyCode, @RequestParam int worklyPass) {

        return new ResponseEntity<>(workingTimesService.setExit(worklyCode, worklyPass), HttpStatus.OK);
    }
}