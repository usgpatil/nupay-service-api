package smfg.nupay_serviceAPI.dto;

import lombok.Data;

@Data
public class NuPayResponse {

    private String status;

    private String message;

    private String mandateId;

    private String referenceId;

    private  String redirectUrl;


}
