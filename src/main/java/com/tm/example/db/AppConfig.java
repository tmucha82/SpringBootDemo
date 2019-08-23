package com.tm.example.db;

import com.tm.example.model.Measure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

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
  public Function<ResultSetExtractor<? extends Measure>, SimpleJdbcCall> simpleJdbcCall(@Autowired JdbcTemplate jdbcTemplate) {
    return resultSetExtractor -> new SimpleJdbcCall(jdbcTemplate)
            .declareParameters(
                new SqlReturnUpdateCount("cout"),
                new SqlReturnResultSet("rs1", resultSetExtractor));
  }

  @Bean
  public Function<ResultSet, DbMeasureResult> processingMeasure() {
    return rs -> {
      try {
        return DbMeasureResult.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .title(rs.getString("title"))
                .build();
      } catch (SQLException e) {
        throw new IllegalArgumentException();
      }
    };
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
        .findPnlsWitExtractor()
        .get();
  }
}