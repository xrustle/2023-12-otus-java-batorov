package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
  private final DbExecutor dbExecutor;
  private final EntitySQLMetaData entitySQLMetaData;
  private final EntityClassMetaData<T> entityClassMetaData;

  @SuppressWarnings("unchecked")
  public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
    this.dbExecutor = dbExecutor;
    this.entitySQLMetaData = entitySQLMetaData;
    this.entityClassMetaData =
        (EntityClassMetaData<T>) entitySQLMetaData.getEntityClassMetaDataClient();
  }

  @Override
  public Optional<T> findById(Connection connection, long id) {
    return dbExecutor.executeSelect(
        connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), findByIdHandler());
  }

  @Override
  public List<T> findAll(Connection connection) {
    return dbExecutor
        .executeSelect(
            connection,
            entitySQLMetaData.getSelectAllSql(),
            Collections.emptyList(),
            findAllHandler())
        .orElseThrow(() -> new RuntimeException("Unexpected error"));
  }

  @Override
  public long insert(Connection connection, T rec) {
    try {
      return dbExecutor.executeStatement(
          connection, entitySQLMetaData.getInsertSql(), getFieldsWithoutIdValues(rec));
    } catch (Exception e) {
      throw new DataTemplateException(e);
    }
  }

  @Override
  public void update(Connection connection, T rec) {
    try {
      dbExecutor.executeStatement(
          connection, entitySQLMetaData.getUpdateSql(), getAllFieldsValues(rec));
    } catch (Exception e) {
      throw new DataTemplateException(e);
    }
  }

  private Function<ResultSet, T> findByIdHandler() {
    return rs -> {
      try {
        if (rs.next()) {
          return instanceOf(rs);
        }
        return null;
      } catch (SQLException e) {
        throw new DataTemplateException(e);
      }
    };
  }

  private Function<ResultSet, List<T>> findAllHandler() {
    return rs -> {
      var recordList = new ArrayList<T>();

      try {
        while (rs.next()) {
          recordList.add(instanceOf(rs));
        }
        return recordList;
      } catch (SQLException e) {
        throw new DataTemplateException(e);
      }
    };
  }

  private T instanceOf(ResultSet resultSet) {
    var allFields = this.entityClassMetaData.getAllFields();
    var constructor = this.entityClassMetaData.getConstructor();
    var initArgs = allFields.stream()
        .map(field -> {
          try {
            return resultSet.getObject(field.getName(), field.getType());
          } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
          }
        })
        .toArray();

    try {
      return constructor.newInstance(initArgs);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new ReflectionRuntimeException(e.getMessage());
    }
  }

  private List<Object> getAllFieldsValues(T rec) {
    var allFields = this.entityClassMetaData.getAllFields();

    return getListOfValues(rec, allFields);
  }

  private List<Object> getFieldsWithoutIdValues(T rec) {
    var fieldsWithoutId = this.entityClassMetaData.getFieldsWithoutId();

    return getListOfValues(rec, fieldsWithoutId);
  }

  @SuppressWarnings("java:S3011")
  private List<Object> getListOfValues(T rec, List<Field> fields) {
    List<Object> values = new ArrayList<>();

    for (Field field : fields) {
      boolean accessible = field.trySetAccessible();
      field.setAccessible(true);
      try {
        Object value = field.get(rec);
        values.add(value);
      } catch (IllegalAccessException e) {
        throw new ReflectionRuntimeException(e.getMessage());
      }
      field.setAccessible(accessible);
    }

    return values;
  }
}
