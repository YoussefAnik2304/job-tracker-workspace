package com.jobtracker.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Standard context tests for the Backend application.
 */
@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() throws Exception {
        // Test 1: Verifies database connection handshake
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.isValid(2)).isTrue();
            System.out.println("Successfully connected to: " + connection.getMetaData().getDatabaseProductName());
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TRIGGER_FAILING_TEST", matches = "true")
    void testEnforcedFailure() {
        // Test 2: Intentionally fails to verify CI pipeline blocks/branch protections
        throw new RuntimeException("Intentionally breaking build to verify Branch Protection rules!");
    }
}
