package smfg.nupay_serviceAPI.service;

import smfg.nupay_serviceAPI.dto.MandateRequest;
import smfg.nupay_serviceAPI.dto.MandateResponse;

public interface MandateService {

   MandateResponse createMandate(MandateRequest request);


}
