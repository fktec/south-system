package br.com.south.system.irs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "br.com.south.system.irs" })
@SpringBootApplication
public class SouthSystemIrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SouthSystemIrsApplication.class, args);
	}

}
