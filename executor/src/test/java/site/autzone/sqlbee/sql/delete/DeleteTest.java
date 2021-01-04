package site.autzone.sqlbee.sql.delete;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.sql.Sql;
import site.autzone.sqlbee.value.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DeleteTest {
    private Connection connection;

    @Before
    public void setupDB() throws Exception {
        Class.forName("org.h2.Driver");
        String db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/employees.sql'";
        connection = DriverManager.getConnection(db);
    }

    @Test
    public void delete() {
        Assert.assertEquals(1, this.delete(SqlBuilder.createDelete()
                .table("employee", "employee")
                .condition("=").left("id").right(new Value(1)).end().sql()
        ));
    }

    public int delete(Sql sql) {
        QueryRunner run = new QueryRunner();
        try {
            return run.execute(connection, sql.prepareSql(),  sql.getParameters().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void closeBD() {
        DbUtils.closeQuietly(connection);
    }
}
