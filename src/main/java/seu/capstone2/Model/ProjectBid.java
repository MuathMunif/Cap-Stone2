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
    @Size(min = 5, max = 150)
    @Column(columnDefinition = "varchar(150) not null")
    private String title;

    @NotEmpty(message = "The description must be not empty")
    @Size(min = 20, max = 5000)
    @Column(columnDefinition = "varchar(5000) not null")
    private String description;

    @NotEmpty(message = "The location must be not empty")
    @Size(min = 2, max = 120)
    @Column(columnDefinition = "varchar(120) not null")
    private String location;

    @NotNull(message = "The budget must be not null")
    @Positive
    @Column(columnDefinition = "double not null")
    private Double budget;

    @NotNull(message = "The deadline must be not null")
    @Future
    @Column(columnDefinition = "DATE not null")
    private LocalDate deadline;


    @Pattern(regexp = "OPEN|CLOSED|AWARDED")
    @Column(columnDefinition = "varchar(10) not null")
    private String status = "OPEN";

    @NotNull(message = "companyId is required")
    @Column(columnDefinition = "int not null")
    private Integer companyId;

    @CreationTimestamp
    private LocalDate createdAt = LocalDate.now();
}
