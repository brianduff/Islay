package org.dubh.islay.hub.server;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;

/**
 * A simple factory that simplifies creation of java objects based on JSON.
 * 
 * @author bduff
 */
public class JsonObjectFactory {
  private static final Logger log = Logger.getLogger(JsonObjectFactory.class.getName());
  private final ImmutableMap<Class<?>, TypeConverter<?>> typeConverters;
  
  JsonObjectFactory(ImmutableMap<Class<?>, TypeConverter<?>> typeConverters) {
    this.typeConverters = typeConverters;
  }
  
  public <T> T create(Class<T> requiredClazz, String jsonObjectString) {
    JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonObjectString);
    T instance;
    try {
      instance = requiredClazz.newInstance();
    } catch (InstantiationException e) {
      throw new IllegalStateException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    
    for (Object entry : jsonObject.entrySet()) {
      Map.Entry<?, ?> e = (Map.Entry<?, ?>) entry;
      Field field = findField(requiredClazz, (String) e.getKey());
      if (field == null) continue;
      Object value = e.getValue();
      if (field.getType().isAssignableFrom(value.getClass())) {
        setField(field, instance, value);
      } else {
        // Can we find a converter for this type?
        TypeConverter<?> converter = typeConverters.get(field.getType());
        if (converter != null) {
          converter.convert(value);
          setField(field, instance, value);
        } else {
          log.warning("No suitable converter for incovertible field " + value);
        }
      }
    }
    
    return instance;
  }
  
  private void setField(Field field, Object instance, Object value) {
    try {
      field.set(instance, value);
    } catch (IllegalArgumentException e1) {
      throw new IllegalStateException(e1);
    } catch (IllegalAccessException e1) {
      throw new IllegalStateException(e1);
    }
  }
  
  private Field findField(Class<?> clazz, String fieldName) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    } catch (NoSuchFieldException e) {
      log.fine("Ignoring " + fieldName + " since it does not exist in " + clazz);
      return null;
    }
  }
  
  /**
   * An object that knows how to convert json values to an instance of type T.
   */
  private interface TypeConverter<T> {
    T convert(Object jsonValue);
  }
  
  /**
   * Converts a string in the ISO date format to a java Date object.
   * 
   * @author bduff
   */
  private static class IsoDateConverter implements TypeConverter<Date> {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    @Override
    public Date convert(Object jsonValue) {
      try {
        return format.parse((String) jsonValue);
      } catch (ParseException e) {
        log.log(Level.SEVERE, "Bad date " + jsonValue, e);
        return null;
      }
    }
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  /**
   * Creates instances of {@link JsonObjectFactory}.
   */
  public static class Builder {
    private final Map<Class<?>, TypeConverter<?>> typeConverters = Maps.newHashMap();
    
    private Builder() {}
    
    public Builder withIsoDateConverter() {
      typeConverters.put(Date.class, new IsoDateConverter());
      return this;
    }
    
    public JsonObjectFactory build() {
      return new JsonObjectFactory(ImmutableMap.copyOf(typeConverters));
    }
  }
}
