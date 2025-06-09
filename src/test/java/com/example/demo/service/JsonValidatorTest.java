package com.example.demo.service;

import com.example.demo.exception.JsonValidateException;
import com.example.demo.validator.JsonValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonValidatorTest {

  private static final Logger logger = LogManager.getLogger();

  @InjectMocks
  private JsonValidator jsonValidator;

  @Test
  void jsonValidate_correct() throws JsonValidateException {
    String json = "{ \n" +
        "    \"id\": \"A0001\", \n" +
        "    \"name\": \"monitor\", \n" +
        "    \"brand\": \"ViewSonic\", \n" +
        "    \"price\": \"5000\" \n" +
        "}";
    String format = "[id:String, name:String, \n" +
        "brand:String, price:Integer]";
    logger.info("input: json: {}, format: {}", json, format);

    String expectResult = "";
    String actualResult = jsonValidator.jsonValidate(json, format);
    logger.info("output: {}", actualResult);

    Assertions.assertEquals(expectResult, actualResult);

  }

  @Test
  void jsonValidate_formatError() throws JsonValidateException {
    String json = "{ \n" +
        "\"id\": \"A0001\", \n" +
        "\"name\": \"monitor\", \n" +
        "\"memo\": \"ViewSonic V20\", \n" +
        "\"price\": \"NT 5000\" \n" +
        "}";
    String format = "[id:String, name:String, \n" +
        "brand:String, price:Integer]";
    logger.info("input: json: {}, format: {}", json, format);

    String expectResult = "type error: price must be Integer, loss data: brand";
    String actualResult = jsonValidator.jsonValidate(json, format);
    logger.info("output: {}", actualResult);

    Assertions.assertEquals(expectResult, actualResult);
  }

  @Test
  void jsonValidate_nestedFormatError() throws JsonValidateException {
    String json = "{ \n" +
        "   \"id\": \"A0001\", \n" +
        "   \"name\": \"monitor\", \n" +
        "   \"price\": \"5000\", \n" +
        "   \"user\": { \n" +
        "       \"uid\": \"U0001\" \n" +
        "     }, \n" +
        "   \"purchase\": { \n" +
        "       \"id\": \"P0001\", \n" +
        "       \"date\": \"20220206\" \n" +
        "   }\n" +
        "}";
    String format = "[id:String, name:String, \n" +
        "brand:String, price:Integer, \n" +
        "user:[uid:String, name:String], \n" +
        "purchase:[id:String, \n" +
        "date:Date]]";
    logger.info("input: json: {}, format: {}", json, format);

    String expectResult = "type error: purchase/date must be Date, " +
        "loss data: brand, user/name";
    String actualResult = jsonValidator.jsonValidate(json, format);
    logger.info("output: {}", actualResult);

    Assertions.assertEquals(expectResult, actualResult);
  }

  @Test
  void jsonValidate_tripleNestedFormatError() throws JsonValidateException {
    String json = "{ \n" +
            "   \"id\": \"A0001\", \n" +
            "   \"name\": \"monitor\", \n" +
            "   \"price\": \"5000\", \n" +
            "   \"user\": { \n" +
            "       \"uid\": \"U0001\", \n" +
            "       \"name\": \"Steven Rogers\", \n" +
            "       \"tel\": \"001\" \n" +
            "   } \n" +
            "}";

    String format = "[id:String, name:String, \n" +
        "price:Integer, " +
        "user:[uid:String, " +
        "name:[first_name:String, " +
        "last_name:String]], " +
        "brand:String]";
    logger.info("input: json: {}, format: {}", json, format);

    String expectResult = "loss data: user/name/first_name, user/name/last_name, brand";
    String actualResult = jsonValidator.jsonValidate(json, format);
    logger.info("output: {}", actualResult);

    Assertions.assertEquals(expectResult, actualResult);
  }


}