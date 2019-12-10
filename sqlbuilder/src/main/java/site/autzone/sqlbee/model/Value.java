package site.autzone.sqlbee.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

public class Value implements IValue {
  private Object value;
  private String valueType = TYPE.STRING.getName();
  private Integer idx;

  public Value() {}

  public Value(Object value) {
    this.value = value;
  }

  public Value(Object value, String valueType) {
    this.value = value;
    this.valueType = valueType;
  }

  public Value(Object value, String valueType, Integer idx) {
    this.value = value;
    this.valueType = valueType;
    this.idx = idx;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getValueType() {
    return valueType;
  }

  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  @Override
  public String toText() {
      return (idx==null)?"?":"${"+idx+"}";
  }
  
  @Override
  public Object convert() {
      try {
          return ConvertUtils.convert(value, Class.forName(map.get(valueType.toLowerCase())));
      } catch (ClassNotFoundException e) {
          e.printStackTrace();
          throw new IllegalArgumentException("Class not found:"+map.get(valueType.toLowerCase()));
      }
  }

  @Override
  public void setIdx(Integer idx) {
    this.idx = idx;
  }

  @Override
  public Integer getIdx() {
    return this.idx;
  }

  public enum TYPE {
    BOOLEAN("boolean", 0),
    INT("int", 1),
    LONG("long", 2),
    FLOAT("float", 3),
    DOUBLE("double", 4),
    STRING("string", 5),
    DATE("date", 6),
    DATE_FORMAT_STR("date_format_str", 7);

    private String name;
    private int index;

    public static String getName(int index) {
      for (TYPE c : TYPE.values()) {
        if (c.getIndex() == index) {
          return c.name;
        }
      }
      return null;
    }

    private TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }
  }

  /** 类型key默认小写 */
  private Map<String, String> map =
      new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
          put("boolean", "java.lang.Boolean");
          put("int", "java.lang.Integer");
          put("long", "java.lang.Long");
          put("float", "java.lang.Float");
          put("double", "java.lang.Double");
          put("string", "java.lang.String");
          put("date", "java.util.Date");
          put("date_format_str", "java.lang.String");
        }
      };
}
