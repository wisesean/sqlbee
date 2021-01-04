package site.autzone.sqlbee.sql.update;

import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.autzone.sqlbee.ISql;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.sql.Table;
import site.autzone.sqlbee.value.DateValue;
import site.autzone.sqlbee.value.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UpdateTest {
    private Connection connection;

    @Before
    public void setupDB() throws Exception {
        Class.forName("org.h2.Driver");
        String db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/employees.sql'";
        connection = DriverManager.getConnection(db);
    }

    @Test
    public void update() {
        Assert.assertEquals(1, this.updateEmail(SqlBuilder.createUpdate()
                .table("employee", "employee")
                .column("employee.firstname", "autzone.site")
                .condition("=").left("id").right(new Value(1)).end().sql()
        ));
    }

    public int updateEmail(ISql sql) {
        QueryRunner run = new QueryRunner();
        try {
            return run.update(connection, sql.output(),  sql.getParameters().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void closeBD() {
        DbUtils.closeQuietly(connection);
    }
}
