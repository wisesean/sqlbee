package site.autzone.sqlbee.sql.query;

import org.junit.Assert;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.sql.Sql;
import site.autzone.sqlbee.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadSafeTest {
    public static int count = 1000;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tasks.add(() -> {
                SqlBuilder sql = SqlBuilder.createQuery()
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
                        .inCondition().column("T1.TID").subSql(SqlBuilder.createQuery().table("TABLE2", "T2").column("t2.id").condition("=")
                                .left("T2.is_new")
                                .right(new Value(true))
                                .end()
                                .sql())
                        .end()
                        .inCondition().column("T1.SID")
                        .subSql("SELECT T3.ID FROM TABLE3 AS T3 WHERE T3.ID = T1.SID OR (1 = 1)").end()
                       ;
                return sql.build().output();
            });
        }
        ExecutorService pool = Executors.newFixedThreadPool(count);
        List<Future<String>> results = pool.invokeAll(tasks);
        Assert.assertEquals(count, results.size());
        for (int i = 0; i < results.size(); i++) {
            String actSql = results.get(i).get();
            System.out.println(actSql);
            Assert.assertEquals("SELECT * FROM TABLE1 AS T1 WHERE (T1.CODE = ?) AND (T1.is_new = ?) AND (T1.TID IN(SELECT t2.id FROM TABLE2 AS T2 WHERE (T2.is_new = ?))) AND (T1.SID IN(SELECT T3.ID FROM TABLE3 AS T3 WHERE T3.ID = T1.SID OR (1 = 1))) LIMIT ?,?",
                    actSql);
        }
        pool.shutdown();
    }
}
