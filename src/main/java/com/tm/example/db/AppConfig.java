package com.tm.example.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.concurrent.ExecutionException;

@Slf4j
@Configuration
@ComponentScan
public class AppConfig {

  @Bean
  JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
     return new JdbcTemplate(dataSource);
  }

  @Bean
  public SimpleJdbcCall pnlCall(@Autowired JdbcTemplate jdbcTemplate) {
    return new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName("FETCH_PNL");
  }

  @Bean
  public SimpleJdbcCall sumCall(@Autowired JdbcTemplate jdbcTemplate) {
    return new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName("GET_SUM")
        .declareParameters(
            new SqlParameter("a", Types.INTEGER),
            new SqlParameter("b", Types.INTEGER),
            new SqlOutParameter("theSum", Types.INTEGER),
            new SqlOutParameter("rowCount", Types.INTEGER))
        .returningResultSet("theSum", (RowMapper<Integer>) (resultSet, i) -> resultSet.getInt(0))
        .returningResultSet("rowCount", (RowMapper<Integer>) (resultSet, i) -> resultSet.getInt(0));
  }

  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("sum-procedure.sql")
        .setSeparator("/;")
        .build();
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class);
    context.getBean(ClientBean.class).findSum();
    context.getBean(ClientBean.class)
//        .findPnls()
        .findPnlsWitExtractor()
        .get();
  }
}