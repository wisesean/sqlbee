# sqlbee
基于Java构建sql语句，提供了默认的sql执行器。

## sql构建器
示例代码:
```java
SqlBuilder.createQuery().table("TABLE1", "T1").go();
SqlBuilder.createQuery().table("TABLE1", "T1").end().firstResults(100).end().go();
SqlBuilder.createQuery().table("TABLE1", "T1").end().firstResults(100).end().maxResults(300).end().isMysql().go();
SqlBuilder.createQuery().table("TABLE1", "T1").end().firstResults(100).end().maxResults(300).end().condition("=").left("T1.CODE").right(new Value("000000")).end().isMysql().go();
```
运行结果:
```sql
00:33:22.706 [main] DEBUG sql - ==> Preparing: SELECT * FROM TABLE1 AS T1
00:33:22.752 [main] DEBUG sql - ==>Parameters: 
00:33:22.756 [main] DEBUG sql - ==> Preparing: SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM TABLE1 AS T1) AS A) WHERE A.RNUM >= ?
00:33:22.756 [main] DEBUG sql - ==>Parameters: 100(Integer)
00:33:22.757 [main] DEBUG sql - ==> Preparing: SELECT * FROM TABLE1 AS T1 LIMIT ?,?
00:33:22.757 [main] DEBUG sql - ==>Parameters: 100(Integer), 300(Integer)
00:33:22.760 [main] DEBUG sql - ==> Preparing: SELECT * FROM TABLE1 AS T1 WHERE (T1.CODE = ?) LIMIT ?,?
00:33:22.760 [main] DEBUG sql - ==>Parameters: 000000(String), 100(Integer), 300(Integer)
```

