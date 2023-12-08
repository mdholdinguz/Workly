package mini.crm.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "working_times")
public class WorkingTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Companies companyId;

    @Column(name = "date")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "arrival_image_link")
    private String arrivalImageLink;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "exit_image_link")
    private String exitImageLink;
}