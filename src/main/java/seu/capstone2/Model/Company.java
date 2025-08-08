package seu.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "The company name must be not empty")
    @Size(min = 4, max = 120, message = "The company name length must be between 4 and 120")
    @Column(columnDefinition = "varchar(120) not null")
    private String name;


    @NotEmpty(message = "The company type must be not empty")
    @Pattern(regexp = "OWNER|CONTRACTOR", message = "The company type must be 'OWNER' or 'CONTRACTOR'")
    @Column(columnDefinition = "varchar(15) not null")
    private String type;


    @NotEmpty(message = "The CR number must be not empty")
    @Pattern(regexp = "\\d{10}", message = "The CR number must be exactly 10 digits")
    @Column(columnDefinition = "varchar(10) not null unique")
    private String crNumber;

    @NotEmpty(message = "The city must be not empty")
    @Size(min = 4, max = 50, message = "The city length must be between 4 and 50")
    @Column(columnDefinition = "varchar(50) not null")
    private String city;

    @NotEmpty(message = "The phone must be not empty")
    @Pattern(regexp = "^05\\d{8}$", message = "The phone must match Saudi format e.g. 05XXXXXXXX")
    @Column(columnDefinition = "varchar(10) not null")
    private String phone;

    @NotNull(message = "createdByUserId is required")
    @Column(columnDefinition = "int not null")
    private Integer createdByUserId;

    @CreationTimestamp
    private LocalDate createdAt = LocalDate.now();
}
