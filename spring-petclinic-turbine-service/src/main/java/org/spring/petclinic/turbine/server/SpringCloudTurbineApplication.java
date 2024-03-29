package org.spring.petclinic.turbine.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableTurbine
@EnableHystrixDashboard
@EnableDiscoveryClient
public class SpringCloudTurbineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudTurbineApplication.class, args);
	}
}