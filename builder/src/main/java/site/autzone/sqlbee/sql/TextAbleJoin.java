package site.autzone.sqlbee.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.autzone.sqlbee.ITextAble;
import site.autzone.sqlbee.injection.SqlCheck;

import java.util.List;

/**
 * TextAble字符串连接
 * @see ITextAble
 */
public class TextAbleJoin {
    final static Logger LOG = LoggerFactory.getLogger("sqlbee.sql");

    public static <T extends ITextAble> String joinWithSkip(SqlCheck.MODE mode, List<T> list, String skipChar) {
        StringBuffer sb = new StringBuffer();
        for(ITextAble textAble : list) {
            String text = textAble.output();
            switch (mode) {
                case STRICT:
                    SqlCheck.containsSqlInjectionStrict(text);
                    break;
                case LOOSELY:
                    SqlCheck.containsSqlInjectionLoosely(text);
                    break;
                default:
                    LOG.warn("uncheck text input:{}, there may be a risk of SQL injection.", text);
            }
            if(sb.length() == 0) {
                sb.append(text);
            }else {
                sb.append(skipChar).append(text);
            }
        }
        return sb.toString();
    }
}
