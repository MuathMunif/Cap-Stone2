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
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "The projectBidId must be not null")
    @Column(columnDefinition = "int not null")
    private Integer projectBidId;

    @NotNull(message = "The contractorId must be not null")
    @Column(columnDefinition = "int not null")
    private Integer contractorId;

    @NotNull(message = "The amount must be not null")
    @Positive(message = "The amount must be valid number")
    @Column(columnDefinition = "double not null")
    private Double amount;

    @NotNull(message = "The durationInDays must be not null")
    @Min(1) @Max(3650)
    @Column(columnDefinition = "int not null")
    private Integer durationInDays;

    @NotEmpty(message = "The description must be not empty")
    @Size(min = 10, max = 3000)
    @Column(columnDefinition = "varchar(3000) not null")
    private String description;


    @Pattern(regexp = "PENDING|ACCEPTED|REJECTED")
    @Column(columnDefinition = "varchar(10) not null")
    private String status = "PENDING";

    @CreationTimestamp
    @Column(columnDefinition = "DATE")
    private LocalDate submissionDate;
}
