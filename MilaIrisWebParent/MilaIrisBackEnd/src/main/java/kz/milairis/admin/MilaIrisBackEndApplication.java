package kz.milairis.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"kz.milairis.common.entity"})
public class MilaIrisBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilaIrisBackEndApplication.class, args);
	}
}
