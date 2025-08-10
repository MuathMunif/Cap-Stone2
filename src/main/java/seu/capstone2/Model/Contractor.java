package seu.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "The contractor name must be not empty")
    @Size(min = 4, max = 120)
    @Column(columnDefinition = "varchar(120) not null")
    private String name;

    @NotEmpty(message = "The CR number must be not empty")
    @Pattern(regexp = "\\d{10}", message = "The CR number must be exactly 10 digits")
    @Column(columnDefinition = "varchar(10) not null unique")
    private String crNumber;

    @NotEmpty(message = "The city must be not empty")
    @Size(min = 2, max = 50)
    @Column(columnDefinition = "varchar(50) not null")
    private String city;

    @NotEmpty(message = "The phone must be not empty")
    @Pattern(regexp = "^05\\d{8}$", message = "The phone must match Saudi format e.g. 05XXXXXXXX")
    @Column(columnDefinition = "varchar(10) not null")
    private String phone;

    @NotEmpty(message = "The email must be not empty")
    @Email(message = "Invalid email")
    @Column(columnDefinition = "varchar(120) not null")
    private String email;

    @CreationTimestamp
    private LocalDate createdAt = LocalDate.now();
}
