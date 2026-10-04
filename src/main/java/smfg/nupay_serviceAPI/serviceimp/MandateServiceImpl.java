package smfg.nupay_serviceAPI.serviceimp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;
import smfg.nupay_serviceAPI.client.NuPayClient;
import smfg.nupay_serviceAPI.dto.MandateRequest;
import smfg.nupay_serviceAPI.dto.MandateResponse;
import smfg.nupay_serviceAPI.dto.NuPayRequest;
import smfg.nupay_serviceAPI.dto.NuPayResponse;
import smfg.nupay_serviceAPI.entity.Mandate;
import smfg.nupay_serviceAPI.entity.MandateStatus;
import smfg.nupay_serviceAPI.repository.MandateRepository;
import smfg.nupay_serviceAPI.service.MandateService;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MandateServiceImpl implements MandateService {

    private final MandateRepository mandateRepository;

    private final ObjectMapper objectMapper;
    private  final NuPayClient nuPayClient;


    @Override
    public MandateResponse createMandate(MandateRequest request) {

        // 1 check duplicate request

        if (mandateRepository.existsBySourceRequestId(request.getSourceRequestID())){

            throw new RuntimeException(

                    "Duplicate sourceRequestID found: "+
                            request.getSourceRequestID()
            );
        }

        // 2. covert original request to json

        String rowRequest;

        try {
            rowRequest=objectMapper.writeValueAsString(request);
        }catch (JsonParseException e){
            throw new RuntimeException(
                    "unable to convert request to json ", e);
        }

        // 3. create mandate entity

        Mandate madate=Mandate.builder()
                .sourceRequestId(request.getSourceRequestID())
                .customerId(request.getCustomerId())
                .loanNumber(request.getLoanNumber())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerMobile(request.getCustomerMobile())
                .amount(request.getAmount())
                .frequency(request.getFrequency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .debitDay(request.getDebitDay())
                .rowRequest(rowRequest)
                .build();

        //4 save the request before the calling nupay;
               Mandate saveMandate=mandateRepository.save(madate);

        // 5. Prepare NuPay request
        NuPayRequest nuPayRequest = NuPayRequest.builder()
                .customerId(request.getCustomerId())
                .loanNumber(request.getLoanNumber())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerMobile(request.getCustomerMobile())
                .amount(request.getAmount())
                .frequency(request.getFrequency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .debitDay(request.getDebitDay())
                .build();

        try {
            // update status before calling nupay

            saveMandate.setStatus(MandateStatus.NUPAY_REQUEST_SENT);
            mandateRepository.save(saveMandate);

            // 7. call nupay

            NuPayResponse nuPayResponse=
                    nuPayClient.createMandate(nuPayRequest);

            // 8. convert nupay response to json

           String rowResponse=objectMapper.writeValueAsString(nuPayResponse);

           // 9.save NupayResponse

            saveMandate.setRowResponse(rowResponse);

            saveMandate.setNupayMandateId(nuPayResponse.getMandateId());
            saveMandate.setNupayReferenceId(nuPayResponse.getReferenceId());
            saveMandate.setMessage(nuPayResponse.getMessage());

            // 10. update status
            if ("SUCCESS".equalsIgnoreCase(nuPayResponse.getStatus())){

                saveMandate.setStatus(MandateStatus.NUPAY_SUCCESS);
            }else{

                saveMandate.setStatus(MandateStatus.NUPAY_FAILED);
            }

            //.11 save everything

           Mandate savedMandate=mandateRepository.save(saveMandate);

        }catch (Exception e){


            //nupay call failed
            saveMandate.setStatus(MandateStatus.NUPAY_FAILED);
            saveMandate.setMessage("unable to process nupay request..");
            mandateRepository.save(saveMandate);

            throw new RuntimeException("Nupay mandate creation failed ",e);
        }

        // 8 return response

        return mapToResponse(saveMandate);


    }

    private MandateResponse mapToResponse(Mandate saveMandate) {

        return MandateResponse.builder()
                .id(saveMandate.getId())
                .sourceRequestId(saveMandate.getSourceRequestId())
                .customerId(saveMandate.getCustomerId())
                .loanNumber(saveMandate.getLoanNumber())
                .amount(saveMandate.getAmount())
                .frequency(saveMandate.getFrequency())

                .startDate(saveMandate.getStartDate())
                .endDate(saveMandate.getEndDate())

                .nupayMandateId(saveMandate.getNupayMandateId())
                .nupayReferenceId(saveMandate.getNupayReferenceId())
                .message(saveMandate.getMessage())

                .status(saveMandate.getStatus().name())
                .createdAt(saveMandate.getCreatedAt())
                .updatedAt(saveMandate.getUpdatedAt())
                .build();
    }
    }

