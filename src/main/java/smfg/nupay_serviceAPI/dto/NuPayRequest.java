package smfg.nupay_serviceAPI.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class NuPayRequest {

    private String customerId;

    private String loanNumber;

    private String customerName;

    private String customerEmail;

    private String customerMobile;

    private BigDecimal amount;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer debitDay;

}
