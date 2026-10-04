package smfg.nupay_serviceAPI.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smfg.nupay_serviceAPI.dto.MandateRequest;
import smfg.nupay_serviceAPI.dto.MandateResponse;
import smfg.nupay_serviceAPI.service.MandateService;

@RestController
@RequestMapping("/api/mandates")
@RequiredArgsConstructor
public class MandateController {

    private  final MandateService mandateService;

    @PostMapping
    public ResponseEntity<MandateResponse> createMandate(@RequestBody MandateRequest request){

       MandateResponse response =mandateService.createMandate(request);

       return ResponseEntity.status(HttpStatus.CREATED)
               .body(response);
    }


}
