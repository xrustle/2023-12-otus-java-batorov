package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
  private final String name;
  private final Constructor<T> constructor;
  private final List<Field> fieldList;
  private final List<Field> fieldListWithoutId;
  private Field id;

  @SuppressWarnings("unchecked")
  public EntityClassMetaDataImpl(Class<T> type) {
    name = type.getSimpleName();

    Field[] fields = type.getDeclaredFields();
    fieldList = new ArrayList<>();
    fieldListWithoutId = new ArrayList<>();

    for (Field field : fields) {
      if (id == null && field.isAnnotationPresent(Id.class)) {
        id = field;
      } else {
        fieldListWithoutId.add(field);
      }
      fieldList.add(field);
    }

    var parameterTypes = fieldList.stream().map(Field::getType).toList().toArray(new Class<?>[0]);

    try {
      constructor = type.getConstructor(parameterTypes);
    } catch (NoSuchMethodException e) {
      throw new ReflectionRuntimeException(e.getMessage());
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Constructor<T> getConstructor() {
    return this.constructor;
  }

  @Override
  public Field getIdField() {
    return this.id;
  }

  @Override
  public List<Field> getAllFields() {
    return this.fieldList;
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    return this.fieldListWithoutId;
  }
}
