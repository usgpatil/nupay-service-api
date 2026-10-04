package smfg.nupay_serviceAPI.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MandateRequest {


    @NotBlank
    private String sourceRequestID;

    @NotBlank
    private String customerId;

    @NotBlank
    private String loanNumber;

    @NotBlank
    private String customerName;

    @Email
    private String customerEmail;

    @NotBlank
    @Pattern(regexp = "^[1-9]{10}$")
    private String customerMobile;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotBlank
    private String frequency;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Min(1)
    @Max(31)
    private Integer debitDay;




}
