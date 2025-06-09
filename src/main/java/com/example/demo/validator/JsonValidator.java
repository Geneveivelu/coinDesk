package com.example.demo.validator;

import com.example.demo.enumaration.JsonValidateErrorEnum;
import com.example.demo.enumaration.JsonValidateExceptionEnum;
import com.example.demo.enumaration.JsonValidateFormatEnum;
import com.example.demo.exception.JsonValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonValidator {

  private static final Logger logger = LogManager.getLogger();

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final String datePattern = "yyyy-MM-dd";

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern).withResolverStyle(ResolverStyle.STRICT);

  private final Map<JsonValidateErrorEnum, List<String>> validateErrorMap = new LinkedHashMap<>();

  public String jsonValidate(String json, String format) throws JsonValidateException {
    //enum 錯誤類型，List 錯誤欄位
    for(JsonValidateErrorEnum error : JsonValidateErrorEnum.values()) {
      validateErrorMap.put(error, new ArrayList<>());
    }

    try {
      JsonNode jsonNode = objectMapper.readTree(json);
      Map<String, Object> formatfeildMap = parseFormat(format);
      validate(jsonNode, formatfeildMap, "");

      return buildErrorResult(formatfeildMap);
    } catch (JsonProcessingException e) {
      logger.error("[json]:{} parsing failed", json);
      throw new JsonValidateException(JsonValidateExceptionEnum.JSON_PARSING_ERROR.getErrorMessage());
    }
  }

  private Map<String, Object> parseFormat(String format) throws JsonValidateException {
    //有序的巢狀結構
    format = format.trim();
    if (format.startsWith("[") && format.endsWith("]")) {
      //取出中段
      format = format.substring(1, format.length() -1 );
      logger.info("formatString: {} start parse fields.", format);
      return parseFields(format);
    } else {
      logger.info("[format]: {} format incorrect", format);
      throw new JsonValidateException(JsonValidateExceptionEnum.FORMAT_PARSING_ERROR.getErrorMessage());
    }
  }

  private void validate(JsonNode jsonNode, Map<String, Object> formatfeildMap, String path) throws JsonValidateException {
    for (Map.Entry<String, Object> fieldFormat : formatfeildMap.entrySet()) {
      String fieldName = fieldFormat.getKey();
      String fullPath = path.isBlank() ? fieldName : path + "/" + fieldName;
      if (!jsonNode.has(fieldName)) {
        validateErrorMap.computeIfAbsent(JsonValidateErrorEnum.LOSS_DATA, fieldsList -> new ArrayList<>()).add(fullPath);
        continue;
      }

      JsonNode value = jsonNode.get(fieldName);
      Object expectFormat = fieldFormat.getValue();

      if (expectFormat instanceof String formatString) {
        if (!typeCheck(formatString, value)) {
          validateErrorMap.computeIfAbsent(JsonValidateErrorEnum.TYPE_ERROR, fieldList -> new ArrayList<>()).add(fullPath);
        }
      } else if (expectFormat instanceof List<?>) {
        try {
          List<Map<String, Object>> expectMap = objectMapper.convertValue(expectFormat, new TypeReference<>() {});
          validate(value, expectMap.get(0), fullPath);
        } catch (Exception e) {
          throw new JsonValidateException(JsonValidateExceptionEnum.NESTED_STRUCT_VALIDATE_FAILED.getErrorMessage());
        }
      }
    }
  }

  private Map<String, Object> parseFields(String format){
    Map<String, Object> feildTreeMap = new LinkedHashMap<>();
    int index = 0;

    while(index < format.length()) {
      int keyStartIndex = index;
      int keyEndIndex = index;
      while (format.charAt(keyEndIndex) != ':') {
        keyEndIndex++;
      }
      String key = format.substring(keyStartIndex, keyEndIndex);
      key = key.trim();
      logger.info("index: {} ~ {} ,key: {}", keyStartIndex, keyEndIndex, key);
      index = keyEndIndex + 1;
      if (format.charAt(index) == '[') {
        int objectStartIndex = index + 1;
        int objectEndIndex = objectStartIndex;
        int objectCount = 1;
        while (objectCount > 0 && objectEndIndex < format.length()) {
          if (format.charAt(objectEndIndex) == '[') {
            objectCount++;
          } else if (format.charAt(objectEndIndex) == ']') {
            objectCount--;
          }
          objectEndIndex++;
        }

        String objectString = format.substring(objectStartIndex, objectEndIndex - 1);
        logger.info("index: {} ~ {} ,objectString: {}", objectStartIndex, objectEndIndex, objectString);
        Map<String, Object> objectFieldTreeMap = parseFields(objectString);
        feildTreeMap.put(key, List.of(objectFieldTreeMap));
        index = objectEndIndex + 1;
      } else {
        int formatStartIndex = index;
        int formatEndIndex = index + 1;
        while (formatEndIndex < format.length() && format.charAt(formatEndIndex) != ',') {
          formatEndIndex++;
        }
        String fieldFormat = format.substring(formatStartIndex, formatEndIndex);
        logger.info("index: {} ~ {} ,fieldFormat: {}", formatStartIndex, formatEndIndex, fieldFormat);
        feildTreeMap.put(key, fieldFormat);
        index = formatEndIndex + 1;
      }
    }

    return feildTreeMap;
  }

  private String buildErrorResult(Map<String, Object> formatfeildMap) throws JsonValidateException {
    List<String> errorResultList = new ArrayList<>();

    for(Map.Entry<JsonValidateErrorEnum, List<String>> validateErrorEntry: validateErrorMap.entrySet()) {
      JsonValidateErrorEnum errorEnum = validateErrorEntry.getKey();

      if (!validateErrorEntry.getValue().isEmpty()) {
        List<String> errorfieldsList = validateErrorEntry.getValue();
        String errorMessage = errorEnum.getError() + ": " + errorEnum.getErrorMessage();

        if (errorEnum.equals(JsonValidateErrorEnum.TYPE_ERROR)) {
          for (String path : errorfieldsList) {
            String fieldFormat = getFieldFormat(path, formatfeildMap);
            errorResultList.add(String.format(errorMessage, path, fieldFormat));
          }
        } else {
          String listString = String.join(", ", errorfieldsList);
          errorResultList.add(String.format(errorMessage, listString));
        }
      }
    }
    logger.info(errorResultList);

    return errorResultList.isEmpty() ? "" : String.join(", ", errorResultList);
  }

  private String getFieldFormat(String fullPath, Map<String, Object> fullFieldFormatMap) throws JsonValidateException {
    if (fullPath.contains("/")) {
      int indexOfSlash = fullPath.indexOf("/");
      String mainField = fullPath.substring(0, indexOfSlash);
      if(fullFieldFormatMap.get(mainField) != null) {
        List<Map<String, Object>> fieldFormatList = objectMapper.convertValue(fullFieldFormatMap.get(mainField), new TypeReference<>() {});
        return getFieldFormat(fullPath.substring(indexOfSlash + 1), fieldFormatList.get(0));
      } else {
        logger.error("Field: {} type not found ", fullPath);
        throw new JsonValidateException(JsonValidateExceptionEnum.FIELD_FORMAT_NOT_FOUND.getErrorMessage());
      }
    } else {
      if (fullFieldFormatMap.get(fullPath) != null) {
        return (String) fullFieldFormatMap.get(fullPath);
      } else {
        logger.error("Field: {} type not found ", fullPath);
        throw new JsonValidateException(JsonValidateExceptionEnum.FIELD_FORMAT_NOT_FOUND.getErrorMessage());
      }
    }
  }

  private boolean typeCheck(String formatString, JsonNode node) throws JsonValidateException {
    logger.info("format: {} check", formatString);
    JsonValidateFormatEnum formatEnum = JsonValidateFormatEnum.getFormatEnum(formatString);
    switch (formatEnum) {
      case STRING:
        if (!node.isTextual()) {
          return false;
        }
        break;
      case INTEGER:
        if (!node.asText().matches("^-?\\d+$")) {
          return false;
        }
        break;
      case DATE:
        String date = node.asText().trim();
        logger.info("date: [{}] check format, expect pattern: {}", date, datePattern);
        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
          try {
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            logger.info("date: [{}] parse success => {}", date, localDate);
          } catch (Exception e) {
            logger.info("date: [{}] parse failed", date);
            return false;
          }
        } else {
          return false;
        }
        break;
    }
    return true;
  }
}
