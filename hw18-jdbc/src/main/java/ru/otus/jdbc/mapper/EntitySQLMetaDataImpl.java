package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
  private final EntityClassMetaData<?> entityClassMetaDataClient;

  private final String id;
  private final String fieldsCommaSeparated;

  public <T> EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
    this.entityClassMetaDataClient = entityClassMetaDataClient;
    this.id = entityClassMetaDataClient.getIdField().getName();
    this.fieldsCommaSeparated = entityClassMetaDataClient.getAllFields().stream()
        .map(Field::getName)
        .collect(Collectors.joining(", "));
  }

  @Override
  public String getSelectAllSql() {
    return String.format(
        "select %s from %s",
        fieldsCommaSeparated, entityClassMetaDataClient.getIdField().getName());
  }

  @Override
  public String getSelectByIdSql() {
    return String.format(
        "select %s from %s where %s = ?",
        fieldsCommaSeparated, entityClassMetaDataClient.getName(), this.id);
  }

  @Override
  public String getInsertSql() {
    var fields = entityClassMetaDataClient.getFieldsWithoutId();

    return String.format(
        "insert into %s(%s) values (%s)",
        entityClassMetaDataClient.getName(),
        fields.stream().map(Field::getName).collect(Collectors.joining(", ")),
        String.join(",", Collections.nCopies(fields.size(), "?")));
  }

  @Override
  public String getUpdateSql() {
    return String.format(
        "update %s set %s = ? where %s = ?",
        entityClassMetaDataClient.getName(),
        entityClassMetaDataClient.getFieldsWithoutId().stream()
            .map(Field::getName)
            .collect(Collectors.joining(" = ?, ")),
        this.id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public EntityClassMetaData<Object> getEntityClassMetaDataClient() {
    return (EntityClassMetaData<Object>) this.entityClassMetaDataClient;
  }
}
