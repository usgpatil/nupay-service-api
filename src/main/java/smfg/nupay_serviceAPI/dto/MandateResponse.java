package smfg.nupay_serviceAPI.dto;

import ch.qos.logback.core.util.Loader;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MandateResponse {

    private Long id;
    private String sourceRequestId;
    private String customerId;
    private String loanNumber;
    private BigDecimal amount;
    private String frequency;
    private String status;
    private String nupayMandateId;
    private String nupayReferenceId;
    private String message;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
