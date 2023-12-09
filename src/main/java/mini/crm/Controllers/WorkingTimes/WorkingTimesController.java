package mini.crm.Controllers.WorkingTimes;

import lombok.RequiredArgsConstructor;
import mini.crm.Configurations.Images.FileUploadUtil;
import mini.crm.Configurations.JWTAuthorization.Authorization;
import mini.crm.Services.WorkingTimes.WorkingTimesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/Working-Times")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
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
    public ResponseEntity<?> setArrival(@RequestParam int worklyCode, @RequestParam int worklyPass,
                                        @RequestParam("image") MultipartFile multipartFile) {

        String fileName = handleFileUpload(worklyCode, "arrival", multipartFile);

        return new ResponseEntity<>(workingTimesService.setArrival(worklyCode, worklyPass, fileName), HttpStatus.OK);
    }

    @Authorization(requiredRoles = {"WORKLY"})
    @PostMapping("/setExit")
    public ResponseEntity<?> setExit(@RequestParam int worklyCode, @RequestParam int worklyPass,
                                     @RequestParam("image") MultipartFile multipartFile) {

        String fileName = handleFileUpload(worklyCode, "exit", multipartFile);

        return new ResponseEntity<>(workingTimesService.setExit(worklyCode, worklyPass, fileName), HttpStatus.OK);
    }

    private String handleFileUpload(int worklyCode, String type, MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {

            throw new IllegalArgumentException("Upload A File!");
        }

        String originalFileName = multipartFile.getOriginalFilename();

        assert originalFileName != null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        if (!fileExtension.matches(".png|.heic|.jpg|.jpeg")) {

            throw new IllegalArgumentException("Invalid file type! Please upload a .png, .heic, .jpg, or .jpeg file.");
        }

        String fileName = worklyCode + "_" + LocalDate.now() + "_" + type + fileExtension;
        String uploadDir = "/var/www/images";

        try {

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        catch (IOException e) {

            e.printStackTrace();
        }

        return fileName;
    }
}