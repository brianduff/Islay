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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

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
  
  public <T> T create(Class<T> requiredClazz, JSONObject jsonObject) {
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
        TypeConverter<?> converter = getConverter(field.getType());
        if (converter != null) {
          Object convertedValue = converter.convert(this, field.getType(), value);
          setField(field, instance, convertedValue);
        } else {
          log.warning("No suitable converter for incovertible field " + value);
        }
      }
    }
    
    return instance;
  }
  
  /**
   * Gets a converter that knows how to create an instance of {@code type} from
   * a JSONObject. If none is find, walks up the interface and class hierarchy of
   * type to look for a suitable converter.
   * 
   * @param type
   * @return
   */
  private TypeConverter<?> getConverter(Class<?> type) {
    // TODO(bduff) this might be slow. If so, we should cache.
    
    // Try this type.
    TypeConverter<?> converter = typeConverters.get(type);
    if (converter != null) {
      return converter;
    }
    // Try the interfaces implemented by this type.
    for (Class<?> ifType : type.getInterfaces()) {
      converter = getConverter(ifType);
      if (converter != null) {
        return converter;
      }
    }
    // Try the supertype.
    Class<?> superType = type.getSuperclass();
    if (superType == null) {
      return null;
    }
    return getConverter(superType);
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
    T convert(JsonObjectFactory factory, Class<?> clazz, Object jsonValue);
  }
  
  /**
   * Converts a string in the ISO date format to a java Date object.
   * 
   * @author bduff
   */
  private static class IsoDateConverter implements TypeConverter<Date> {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    @Override
    public Date convert(JsonObjectFactory factory, Class<?> clazz, Object jsonValue) {
      try {
        return format.parse((String) jsonValue);
      } catch (ParseException e) {
        log.log(Level.SEVERE, "Bad date " + jsonValue, e);
        return null;
      }
    }
  }

  /**
   * A converter that creates objects recursively through the factory for specific
   * types.
   */
  private static class RecursiveConverter<T> implements TypeConverter<T> {
    @SuppressWarnings("unchecked")
    @Override
    public T convert(JsonObjectFactory factory, Class<?> clazz, Object jsonValue) {
      return (T) factory.create(clazz, (JSONObject) jsonValue);
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
    
    public <T> Builder withRecursiveConverter(Class<?>... classes) {
      for (Class<?> clazz : classes) {
        typeConverters.put(clazz, new RecursiveConverter<Object>());
      }
      return this;
    }
    
    public JsonObjectFactory build() {
      return new JsonObjectFactory(ImmutableMap.copyOf(typeConverters));
    }
  }
}
