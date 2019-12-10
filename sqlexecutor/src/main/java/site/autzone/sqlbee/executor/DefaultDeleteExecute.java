package site.autzone.sqlbee.executor;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultDeleteExecute implements DeleteExecute {
	@Autowired
	DataSource dataSource;

	public int delete(String sql , Object... params) {
		QueryRunner run = new QueryRunner(dataSource);
		try {
			return run.execute(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
