package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}
}	

//    @Bean
//    public CommandLineRunner callPersonService(PersonService personService) {
//        return args -> {
//            // here you can call any service method, no controller involved:
//            List<Person> all = personService.findAll();
//            System.out.println("Found " + all.size() + " persons:");
//            all.forEach(p -> System.out.println(" • " + p.getEmail()));
//            // you could even create, update, delete…
//        };
//    }

