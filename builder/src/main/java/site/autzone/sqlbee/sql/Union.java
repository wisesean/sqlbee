package site.autzone.sqlbee.sql;

import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import site.autzone.sqlbee.HasChildren;
import site.autzone.sqlbee.ISql;
import site.autzone.sqlbee.ITextAble;

import java.util.Arrays;
import java.util.List;

/**
 * union & union all
 */
public class Union implements ITextAble, HasChildren {
    private String type = "DISTINCT";
    private ISql unionSql;

    public String getType() {
        return type;
    }

    public void all() {
        this.type = "ALL";
    }

    public ISql getUnionSql() {
        return unionSql;
    }

    public void setUnionSql(ISql unionSql) {
        this.unionSql = unionSql;
    }

    @Override
    public String output() {
        this.unionSql.isSubSql(true);
        StringBuffer sb = new StringBuffer("UNION ").append(this.type).append(" ").append(unionSql.output());
        return sb.toString();
    }

    @Override
    public List<ITextAble> getChildren() {
        return Arrays.asList(unionSql);
    }

    @Override
    public ITextAble getChild(int index) {
        Validate.isTrue(index == 0);
        return unionSql;
    }
}
