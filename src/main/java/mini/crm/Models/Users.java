package mini.crm.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "company_working_id")
    private Companies companyWorkingId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments departmentId;

    private String username;
    private String password;
    private String role;

    @Column(name = "workly_code")
    private Integer worklyCode;

    @Column(name = "workly_pass")
    private Integer worklyPass;
}