package com.tm.example.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@Component
public class ClientBean {
  private final DataSource dataSource;
  private SimpleJdbcCall sumCall;
  private SimpleJdbcCall pnlCall;

  @Autowired
  public ClientBean(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostConstruct
  public void init() {
      JdbcTemplate template = new JdbcTemplate(dataSource);
      sumCall = new SimpleJdbcCall(template)
              .withProcedureName("GET_SUM")
              .declareParameters(
                      new SqlParameter("a", Types.INTEGER),
                      new SqlParameter("b", Types.INTEGER),
                      new SqlOutParameter("theSum", Types.INTEGER),
                      new SqlOutParameter( "rowCount", Types.INTEGER))
              .returningResultSet("theSum", (RowMapper<Integer>) (resultSet, i) -> resultSet.getInt(0))
              .returningResultSet("rowCount", (RowMapper<Integer>) (resultSet, i) -> resultSet.getInt(0));
      pnlCall = new SimpleJdbcCall(template)
          .withProcedureName("FETCH_PNL");

  }

  public void findPnls() {
    Map<String, Object> resultMap = pnlCall.execute(new MapSqlParameterSource());
    resultMap.entrySet().forEach(System.out::println);
  }

  public void findSum() {
      MapSqlParameterSource paramMap = new MapSqlParameterSource()
              .addValue("a", 5)
              .addValue("b", 20);
      Map<String, Object> resultMap = sumCall.execute(paramMap);
      resultMap.entrySet().forEach(System.out::println);
  }
}