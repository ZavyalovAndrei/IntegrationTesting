package com.zavialov.integrationTesting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    public static final GenericContainer<?> containerProd = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromPath(".", Paths.get("target"))
                    .withDockerfileFromBuilder(builder ->
                            builder
                                    .from("openjdk:18-alpine3.15")
                                    .expose(8081)
                                    .add("ProdIntegrationTesting-0.0.1-SNAPSHOT.jar", "prodtest.jar")
                                    .entryPoint("java", "-jar", "/prodtest.jar")
                                    .build()))
            .withExposedPorts(8081);
    @Test
    void test() {
        ResponseEntity<String> forEntityProd = restTemplate.getForEntity("http://localhost:" +
                containerProd.getMappedPort(8081) + "/profile", String.class);
        Assertions.assertEquals("Current profile is production", forEntityProd.getBody());
    }
}