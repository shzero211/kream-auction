package site.shkrr.kreamAuction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //Auditing 설정
public class KreamAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(KreamAuctionApplication.class, args);
	}

}
