package smfg.nupay_serviceAPI.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smfg.nupay_serviceAPI.dto.NuPayRequest;
import smfg.nupay_serviceAPI.dto.NuPayResponse;

@FeignClient(
        name = "dummy-nupay-service",
        url = "${nupay.base-url}"
)
public interface NuPayClient {

    @PostMapping("/api/nupay/mandates")
      public  NuPayResponse createMandate(@RequestBody NuPayRequest request);



}
