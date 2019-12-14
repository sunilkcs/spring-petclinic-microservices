package org.spring.petclinic.hsqldb.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.hsqldb.Server;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class SpringCloudHsqldbApplication {

	@Value("${hsqldb.port}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudHsqldbApplication.class, args);
	}

	@PostConstruct
	private void startDB() {
		Server server = new Server();
		server.setDatabaseName(0, "petclinic");
		server.setDatabasePath(0, "mem:petclinic");
		server.setPort(port);
		server.start();
	}
}