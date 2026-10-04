package smfg.nupay_serviceAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NupayServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NupayServiceApiApplication.class, args);
	}

}
