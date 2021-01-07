# sqlbee
基于Java构建sql语句，提供了默认的sql执行器。

## sql构建器
示例代码:
```java
SqlBuilder.createQuery().table("TABLE1", "T1").sql();
SqlBuilder.createQuery().table("TABLE1", "T1").maxResults(300).sql();
SqlBuilder.createQuery().table("TABLE1", "T1").firstResults(100).maxResults(300).condition("=").left("T1.CODE").right(new Value("000000")).end().sql();
```
运行结果:
```sql
14:43:12.908 [main] DEBUG sql - ==> Preparing: SELECT * FROM TABLE1 AS T1
14:43:12.957 [main] DEBUG sql - ==>Parameters: 
14:43:12.959 [main] DEBUG sql - ==> Preparing: SELECT * FROM TABLE1 AS T1 LIMIT 0, ?
14:43:12.960 [main] DEBUG sql - ==>Parameters: 300(Integer)
14:43:12.962 [main] DEBUG sql - ==> Preparing: SELECT * FROM TABLE1 AS T1 WHERE (T1.CODE = ?) LIMIT ?,?
14:43:12.962 [main] DEBUG sql - ==>Parameters: 000000(String), 100(Integer), 300(Integer)
```

## sql执行器
配置SqlRunner:
```java
  @Bean
  public SqlRunner sqlRunner() {
      return new SqlRunner(this.dataSource());
  }
```
使用runner执行sql
```java
  //注入sqlRunner
  @Autowired SqlRunner sqlRunner;
  //构建sql
  Sql sql = SqlBuilder.createQuery().table("TABLE1", "T1").build();
  List<Table> list = sqlRunner.queryBeans(sql, Table.class);
```


