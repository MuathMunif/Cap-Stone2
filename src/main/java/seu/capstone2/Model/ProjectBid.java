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
public class ProjectBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "The title must be not empty")
    @Size(min = 5, max = 150, message = "The title length must be between 5 and 150")
    @Column(columnDefinition = "varchar(150) not null")
    private String title;

    @NotEmpty(message = "The description must be not empty")
    @Size(min = 20, max = 5000, message = "The description length must be between 20 and 5000")
    @Column(columnDefinition = "varchar(5000) not null")
    private String description;

    @NotEmpty(message = "The location must be not empty")
    @Size(min = 2, max = 120, message = "The location length must be between 2 and 120")
    @Column(columnDefinition = "varchar(120) not null")
    private String location;

    @NotNull(message = "The budget is required")
    @Positive( message = "The budget must be greater than 0")
    @Column(columnDefinition = "double not null")
    private Double budget;

    @NotNull(message = "The deadline is required")
    @Future(message = "The deadline must be a future date")
    @Column(columnDefinition = "DATE not null")
    private LocalDate deadline;


    @NotEmpty(message = "The status must be not empty")
    @Pattern(regexp = "OPEN|CLOSED|AWARDED", message = "Status must be 'OPEN', 'CLOSED' or 'AWARDED'")
    @Column(columnDefinition = "varchar(10) not null")
    private String status = "OPEN";


    @NotNull(message = "companyId is required")
    @Column(columnDefinition = "int not null")
    private Integer companyId;

    @NotNull(message = "createdByUserId is required")
    @Column(columnDefinition = "int not null")
    private Integer createdByUserId;

    @CreationTimestamp
    private LocalDate createdAt = LocalDate.now();
}
