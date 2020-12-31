package site.autzone.sqlbee.value;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.IValue;

public class Value implements IValue {
    private Object value;
    private Class valueType;
    private Integer idx;

    public Value() {
    }

    public Value(Object value) {
        this.value = value;
    }

    public Value(Object value, Class valueType) {
        this.value = value;
        this.valueType = valueType;
        this.validateType();
    }

    public Value(Object value, Class valueType, Integer idx) {
        this.value = value;
        this.valueType = valueType;
        this.validateType();
        this.idx = idx;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String output() {
        return (idx == null) ? "?" : "${" + idx + "}";
    }

    @Override
    public Object convert() {
        if(this.valueType == null) {
            this.valueType = this.value.getClass();
            validateType();
        }
        if(this.value.getClass() == this.valueType) {
            return this.value;
        }
        return ConvertUtils.convert(value, this.valueType);
    }

    @Override
    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    @Override
    public Integer getIdx() {
        return this.idx;
    }

    private void validateType() {
        Validate.isTrue(Boolean.class == this.valueType || Integer.class == this.valueType
                || Long.class == this.valueType || Float.class == this.valueType || Double.class == this.valueType || String.class == this.valueType || Date.class == this.valueType ||
                java.sql.Date.class == this.valueType);
        if(java.sql.Date.class == this.valueType) {
            this.valueType = Date.class;
        }
    }
}
