package com.tm.example.db;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.SqlReturnUpdateCount;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Configuration
public class ClientBean {
  private final SimpleJdbcCall sumCall;
  private final SimpleJdbcCall pnlCall;

  private final Map<String, DbMeasureResult> pnlTemplate = Maps.newHashMapWithExpectedSize(1);

  @Autowired
  public ClientBean(SimpleJdbcCall pnlCall, SimpleJdbcCall sumCall) {
    this.pnlCall = pnlCall;
    this.sumCall = sumCall;
    pnlCall.addDeclaredParameter(new SqlReturnUpdateCount("cout"));
    pnlCall.addDeclaredParameter(new SqlReturnResultSet("rs1", processPnlExtractor()));
  }


  public CompletableFuture findPnls() {
    return CompletableFuture
        .supplyAsync(() -> {
          Map<String, Object> execute = pnlCall.execute(new MapSqlParameterSource());
          log.info("Map = {}", execute);
          return execute.get("cout");
        })
        .thenAccept(cout -> {
          log.info("Processing notification with {} and {}", pnlTemplate.get("pnl"), cout);
          log.info("Finished....");
        });
  }

  public CompletableFuture findPnlsWitExtractor() {
    return CompletableFuture
        .supplyAsync(() -> {
          Map<String, Object> execute = pnlCall.execute(new MapSqlParameterSource());
          log.info("Map = {}", execute);
          return Pair.of(execute.get("cout"),  ((DbMeasureResult) execute.get("rs1")));
        })
        .thenAccept(pair -> {
          log.info("Processing notification with {} and {}", pair.getRight(), pair.getLeft());
          log.info("Finished....");
        });
  }

  private ResultSetExtractor<DbMeasureResult> processPnlExtractor() {
    return rs -> {
      DbMeasureResult dbMeasureResult = null;
      while(rs.next()) {
        if(dbMeasureResult == null) {
          dbMeasureResult = processingPnl(rs);
        }
        processingPnl(rs);
      }
      return dbMeasureResult;
    };
  }

  RowCallbackHandler processPnlHandler() {
    return rs -> {
      final DbMeasureResult dbMeasureResult = processingPnl(rs);
      pnlTemplate.putIfAbsent("pnl", dbMeasureResult);
    };
  }

  private DbMeasureResult processingPnl(ResultSet rs) throws SQLException {
    final DbMeasureResult dbMeasureResult = DbMeasureResult.builder()
        .id(rs.getInt("id"))
        .name(rs.getString("name"))
        .title(rs.getString("title"))
        .build();
    log.info("Processing pnl = {}", dbMeasureResult);
    return dbMeasureResult;
  }

  public void findSum() {
      MapSqlParameterSource paramMap = new MapSqlParameterSource()
              .addValue("a", 5)
              .addValue("b", 20);
      Map<String, Object> resultMap = sumCall.execute(paramMap);
      resultMap.entrySet().forEach(System.out::println);
  }
}