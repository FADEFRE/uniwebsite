package swtp12.modulecrediting;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import swtp12.modulecrediting.model.*;
import swtp12.modulecrediting.repository.ApplicationRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class ModulecreditingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModulecreditingApplication.class, args);
	}

}
