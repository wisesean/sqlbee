package site.autzone.sqlbee.injection;

import org.apache.commons.lang3.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 非预编译内容sql注入检查
 */
public class SqlCheck {
    public enum MODE {
        NONE,STRICT,LOOSELY
    }
    /**
     * 严格模式,检查输入是否存在sql注入风险，返回true表示含有
     * and|exec|insert|select|drop|grant|alter|delete|update|chr|mid|master|truncate|char|declare|or|;|+|'|%
     * @param obj
     * @return
     */
    public static void containsSqlInjectionStrict(String obj) {
        Pattern pattern= Pattern.compile("\\b(and|exec|insert|select|drop|grant|alter|delete|update|chr|mid|master|truncate|char|declare|or)\\b|(;|\\+|'|%)");
        Matcher matcher=pattern.matcher(obj==null?"":obj.toLowerCase());
        Validate.isTrue(!matcher.find(), "输入内容存在SQL注入安全风险,请检查输入内容:"+obj);
    }

    /**
     * 宽松模式,检查输入是否存在sql注入风险，返回true表示拼接的内容中含有:
     * exec|insert|drop|grant|alter|delete|update|master|truncate|declare
     * @param obj
     * @return
     */
    public static void containsSqlInjectionLoosely(String obj){
        Pattern pattern= Pattern.compile("\\b(exec|insert|drop|grant|alter|delete|update|master|truncate|declare)\\b");
        Matcher matcher=pattern.matcher(obj==null?"":obj.toLowerCase());
        Validate.isTrue(!matcher.find(), "输入内容存在SQL注入安全风险,请检查输入内容:"+obj);
    }
}
