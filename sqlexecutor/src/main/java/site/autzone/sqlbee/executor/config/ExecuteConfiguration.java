package site.autzone.sqlbee.executor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import site.autzone.sqlbee.executor.BatchExecute;
import site.autzone.sqlbee.executor.DefaultBatchExecute;
import site.autzone.sqlbee.executor.DefaultDeleteExecute;
import site.autzone.sqlbee.executor.DefaultInsertExecute;
import site.autzone.sqlbee.executor.DefaultQueryExecute;
import site.autzone.sqlbee.executor.DefaultUpdateExecute;
import site.autzone.sqlbee.executor.DeleteExecute;
import site.autzone.sqlbee.executor.InsertExecute;
import site.autzone.sqlbee.executor.QueryExecute;
import site.autzone.sqlbee.executor.UpdateExecute;

@Configuration
public class ExecuteConfiguration {

  @Bean
  public InsertExecute insertExecute() {
    return new DefaultInsertExecute();
  }

  @Bean
  public UpdateExecute updateExecute() {
    return new DefaultUpdateExecute();
  }

  @Bean
  public DeleteExecute deleteExecute() {
    return new DefaultDeleteExecute();
  }

  @Bean
  public QueryExecute queryExecute() {
    return new DefaultQueryExecute();
  }

  @Bean
  public BatchExecute batchExecute() {
    return new DefaultBatchExecute();
  }
}
