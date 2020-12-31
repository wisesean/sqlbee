package site.autzone.sqlbee.sql;

import site.autzone.sqlbee.ITextAble;

import java.util.List;

/**
 * TextAble字符串连接
 * @see ITextAble
 */
public class TextAbleJoin {
    public static <T extends ITextAble> String joinWithSkip(List<T> list, String skipChar) {
        StringBuffer sb = new StringBuffer();
        for(ITextAble textAble : list) {
            if(sb.length() == 0) {
                sb.append(textAble.output());
            }else {
                sb.append(skipChar).append(textAble.output());
            }
        }
        return sb.toString();
    }
}
