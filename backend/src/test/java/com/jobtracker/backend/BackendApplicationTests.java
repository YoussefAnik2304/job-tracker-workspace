package com.jobtracker.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class BackendApplicationTests {

	@Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() throws Exception {
        // Force an active physical connection handshake to the PostgreSQL container
        try (Connection connection = dataSource.getConnection()) {
            // Assert that the connection is active and valid
            assertThat(connection.isValid(2)).isTrue();
            System.out.println("Successfully connected to: " + connection.getMetaData().getDatabaseProductName());
        }
		// testing 
    }
}
