package com.example;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.configuration.ProfileManager;
import io.restassured.RestAssured;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class NativeITBase {

    static Logger LOG = LoggerFactory.getLogger(NativeExampleResourceIT.class);

    static String quarkusProfile = ProfileManager.getActiveProfile();

    static Integer testPort = ConfigProvider
            .getConfig()
            .getValue("%" + quarkusProfile + ".quarkus.http.port", Integer.class);

    static String dockerImageName = new DockerImageName("0815hunter/demo", "1.0-SNAPSHOT").toString();

    @Container
    static GenericContainer<?> demoApp = new GenericContainer<>(dockerImageName)
            .withLogConsumer(new Slf4jLogConsumer(LOG))
            .withExposedPorts(testPort)
            .withCreateContainerCmdModifier(cmd -> {
                cmd.withCmd("./application", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.profile=" + quarkusProfile);
            });

    @BeforeAll
    public static void setUp() {
        RestAssured.port = demoApp.getMappedPort(testPort);
    }
}
