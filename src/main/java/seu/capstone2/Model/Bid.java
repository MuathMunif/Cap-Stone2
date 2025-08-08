package seu.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Manual FKs
    @NotNull(message = "projectBidId is required")
    @Column(columnDefinition = "int not null")
    private Integer projectBidId;

    @NotNull(message = "contractorCompanyId is required")
    @Column(columnDefinition = "int not null")
    private Integer contractorCompanyId; // Must exist and be type CONTRACTOR

    @NotNull(message = "submittedByUserId is required")
    @Column(columnDefinition = "int not null")
    private Integer submittedByUserId;   // Must exist and be role CONTRACTOR

    @NotNull(message = "amount is required")
    @Positive( message = "amount must be greater than 0")
    @Column(columnDefinition = "double not null")
    private Double amount;

    @NotNull(message = "durationInDays is required")
    @Min(value = 1, message = "durationInDays must be at least 1")
    @Max(value = 3650, message = "durationInDays is too large")
    @Column(columnDefinition = "int not null")
    private Integer durationInDays;

    @NotEmpty(message = "description must be not empty")
    @Size(min = 10, max = 3000, message = "description length must be more than 9")
    @Column(columnDefinition = "varchar(3000) not null")
    private String description;

    // PENDING, ACCEPTED, REJECTED
    @NotEmpty(message = "status must be not empty") //todo check
    @Pattern(regexp = "PENDING|ACCEPTED|REJECTED", message = "Status must be 'PENDING', 'ACCEPTED' or 'REJECTED'")
    @Column(columnDefinition = "varchar(10) not null")
    private String status = "PENDING";

    @NotNull(message = "submissionDate must be not null")
    @Column(columnDefinition = "date")
    private LocalDate submissionDate;
}
