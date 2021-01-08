# sqlbee
基于Java构建sql语句，提供了默认的sql执行器。

## sql构建器
示例代码:
```java
SqlBuilder.createQuery().table("TABLE1", "T1").sql();
SqlBuilder.createQuery().table("TABLE1", "T1").maxResults(300).sql();
SqlBuilder.createQuery()
    .table("TABLE1", "T1")
    .firstResults(100)
    .maxResults(300)
    .condition("=")
    .left("T1.CODE")
    .right(new Value("000000"))
    .end()
    .condition("=")
    .left("T1.is_new")
    .right(new Value(true))
    .end()
    .sql();
```
运行结果:
```sql
17:51:34.148 [main] INFO sql - ==> sql: SELECT * FROM TABLE1 AS T1
17:51:34.157 [main] INFO sql - ==> sql: SELECT * FROM TABLE1 AS T1 LIMIT 0, 300
17:51:34.160 [main] INFO sql - ==> sql: SELECT * FROM TABLE1 AS T1 WHERE (T1.CODE = '000000') AND (T1.is_new = true) LIMIT 100,300
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


