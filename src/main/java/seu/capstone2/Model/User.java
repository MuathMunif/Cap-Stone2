package seu.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message ="The name must be not empty")
    @Size(min = 4, message = "The name length must be more than 3")
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty(message ="The email must be not empty")
    @Email(message = "The email must be a valid email")
    @Size(max = 50)
    @Column(columnDefinition = "varchar(50) not null unique")
    private String email;


    @NotEmpty(message ="The password must be not empty")
    @Size(min = 7, max = 100, message = "password length must be more than 6")
    private String password;


    @NotEmpty(message ="The role must be not empty")
    @Pattern(regexp = "OWNER|CONTRACTOR", message = "The role must be 'OWNER' Or 'CONTRACTOR'")
    private String role;

    @CreationTimestamp
    private LocalDate registeredDate = LocalDate.now();
}
