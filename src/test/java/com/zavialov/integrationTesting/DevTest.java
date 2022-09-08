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
class DevTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    public static final GenericContainer<?> containerDev = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromPath(".", Paths.get("target"))
                    .withDockerfileFromBuilder(builder ->
                            builder
                                    .from("openjdk:18-alpine3.15")
                                    .expose(8080)
                                    .add("DevIntegrationTesting-0.0.1-SNAPSHOT.jar", "devtest.jar")
                                    .entryPoint("java", "-jar", "/devtest.jar")
                                    .build()))
            .withExposedPorts(8080);

    @Test
    void test() {
        ResponseEntity<String> forEntityDev = restTemplate.getForEntity("http://localhost:" +
                containerDev.getMappedPort(8080) + "/profile", String.class);
        Assertions.assertEquals("Current profile is dev", forEntityDev.getBody());
    }
}