package in.Kavin.removebg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "in.Kavin.removebg.repository")
@EntityScan(basePackages = "in.Kavin.removebg.entity") // ✅ Fix is here
@EnableFeignClients
public class RemovebgApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemovebgApplication.class, args);
    }
}
