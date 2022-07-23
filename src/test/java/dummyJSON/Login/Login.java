package dummyJSON.Login;

import com.jayway.jsonpath.Configuration;
import dummyJSON.Users.Test_GET_Request;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.SubSequentCalls;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

/**
 * Created by Saurabh Kumar. Using fake APIs https://dummyjson.com/
 */

public class Login {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Test_GET_Request.class);
    SubSequentCalls ssc = new SubSequentCalls();
    String baseURI;
    String loginPath;
    Properties properties;
    String token;

    private static Configuration configuration = Configuration.defaultConfiguration();

    @BeforeMethod
    public void preCondition(){
        try{
            FileInputStream fis = new FileInputStream("./src/main/resources/config.properties");
            properties = new Properties();
            properties.load(fis);
            baseURI = System.getProperty("HOST");
            if (baseURI == null){
                baseURI = properties.getProperty("HOST");
            }
            loginPath = properties.getProperty("LoginPath");
            token = ssc.getToken();
        }catch (Exception e){
            logger.error("Exception found at pre-condition: ", e);
            Assert.fail("Exception found at pre-condition: "+ e.getMessage(), e);
        }
    }

    @Test
    public void verifyLogin(){
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("username", "kminchelle");
            jsonObject.put("password", "0lelplR");

            String auth = JSONValue.toJSONString(jsonObject); //jsonObject.toJSONString().;
            System.out.println(auth);

            given().baseUri(baseURI).header("Content-Type", "application/json").contentType(ContentType.JSON).
                    body(auth).post(loginPath).then().assertThat().statusCode(SC_OK).log().all();

        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    @Test(priority = 1)
    public void verifyEmptyField(){
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("username", "");
            jsonObject.put("password", "");

            String auth = JSONValue.toJSONString(jsonObject);
            System.out.println(auth);

            given().baseUri(baseURI).header("Content-Type", "application/json").contentType(ContentType.JSON).
                    when().body(auth).post(loginPath).then().
                    assertThat().statusCode(SC_BAD_REQUEST).log().all();
        }catch (Exception e){
            logger.error("Exception", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    @Test(priority = 2)
    public void invalidCredentials(){
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("username", "kminchelle");
            jsonObject.put("password", "test123");

            String auth = JSONValue.toJSONString(jsonObject);
            System.out.println(auth);

            given().baseUri(baseURI).header("Content-Type", "application/json").contentType(ContentType.JSON).
                    when().body(auth).post(loginPath).then().
                    assertThat().statusCode(SC_UNAUTHORIZED).log().all();
        }catch (Exception e){
            logger.error("Exception", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }
}
