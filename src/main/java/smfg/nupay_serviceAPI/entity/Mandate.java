package smfg.nupay_serviceAPI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "mandates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_source_request_id",
                        columnNames = "source_request_id"
                )
        }
)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mandate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Idempotency key from source system

    @Column(name = "source_request_id", nullable = false,unique = true)
    private String sourceRequestId;

    private String loanNumber;
    private String customerId;
    private String customerName;
    private  String customerEmail;
    private String customerMobile;
    private BigDecimal amount;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer debitDay;

    // Our internal mandate status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MandateStatus status;

    //nupay identifiers
    private String nupayMandateId;

    private String nupayReferenceId;

    private String message;

    //// Complete original request JSON
    @Lob
    @Column(name = "row_Request",columnDefinition = "LONGTEXT")
    private  String rowRequest;

    @Lob
    @Column(name = "row_Response",columnDefinition = "LONGTEXT")
    private  String rowResponse;

    //Audit timeStamp

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected  void onCreate(){

        LocalDateTime now=LocalDateTime.now();

        createdAt=now;
        updatedAt=now;

        if(status==null){

            status=MandateStatus.CREATED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }




}
