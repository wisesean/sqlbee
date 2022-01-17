package site.autzone.sqlbee.executor;

import javax.sql.DataSource;

public interface RouteDs {
    /**
     * 设置数据源
     *
     * @param dataSource
     */
    void dataSource(DataSource dataSource);
}
