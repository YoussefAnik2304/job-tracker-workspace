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
        // Intentionally throw an exception to test CI pipeline build failure blocks merge
        throw new RuntimeException("Intentionally breaking build to verify Branch Protection rules!");
    }
}
