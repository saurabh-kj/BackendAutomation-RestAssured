package gorest.Users;

import com.jayway.jsonpath.Configuration;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utills.ReadFile;
import utills.SubSequentCalls;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

/**
 * Created by Saurabh Kumar. Using fake APIs https://dummyjson.com/
 */

public class TestPostRequest {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestGetRequest.class);
    Map<String, String> headerProperties;
    SubSequentCalls ssc = new SubSequentCalls();
    String baseURI;
    String userAdd;
    Properties properties;
    String token;

    private static Configuration configuration = Configuration.defaultConfiguration();

    @BeforeMethod
    public void preCondition(){
        try{
            FileInputStream fis = new FileInputStream("./src/main/resources/config.properties");
            headerProperties = ReadFile.readProperties("./src/main/resources/header.properties");
            properties = new Properties();
            properties.load(fis);
            baseURI = System.getProperty("HOST");
            if (baseURI == null){
                baseURI = properties.getProperty("HOST");
            }
            userAdd = properties.getProperty("UserAdd");
            token = ssc.getToken();

        }catch (Exception e){
            logger.error("Exception found at pre-condition: ", e);
            Assert.fail("Exception found at pre-condition: "+ e.getMessage(), e);
        }
    }

    @Test
    public void verifyResponse201(){
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("firstName", "Saurabh");
            jsonObject.put("lastName", "Kumar");
            jsonObject.put("age", "29");

            String addData = jsonObject.toJSONString();
            System.out.println(addData);

            // Adding data to POST API
            given().baseUri(baseURI).contentType(ContentType.JSON).accept(ContentType.JSON).
                    body(addData).when().post(userAdd).then().assertThat().statusCode(SC_CREATED).log().all();

        }catch (Exception e){
            logger.info("Exception found: ", e);
            Assert.fail("Exception found: " + e.getMessage(), e);
        }
    }
}
