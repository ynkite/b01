package com.example.b01;


import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@SpringBootTest

public class DataSourceTests {
    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException {
        @Cleanup // close() 메서드를 자동으로 호출 해주는 어노테이션
        Connection con = dataSource.getConnection();
        log.info(con);
        Assertions.assertNotNull(con); // connction 객체가 NotNull인지 확인
        // 데이터베이스 연결이 성공적으로 이루어졌는지 확정 짓는 코드
    }
}
