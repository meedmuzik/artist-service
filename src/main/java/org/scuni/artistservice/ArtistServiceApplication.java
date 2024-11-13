package org.scuni.artistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableNeo4jRepositories(basePackages = "org.scuni.artistservice.repository")
@EnableTransactionManagement
public class ArtistServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtistServiceApplication.class, args);
	}

}
