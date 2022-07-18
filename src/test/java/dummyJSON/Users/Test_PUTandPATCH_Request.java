package dummyJSON.Users;

import com.jayway.jsonpath.Configuration;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utills.SubSequentCalls;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

/**
 * Created by Saurabh Kumar. Using fake APIs https://dummyjson.com/
 */

public class Test_PUTandPATCH_Request {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Test_GET_Request.class);
    SubSequentCalls ssc = new SubSequentCalls();
    String baseURI;
    String userPath;
    Properties properties;
    String token;
    String userID = "2";

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
            userPath = properties.getProperty("UserPath");
            token = ssc.getToken();
        }catch (Exception e){
            logger.error("Exception found at pre-condition: ", e);
            Assert.fail("Exception found at pre-condition: "+ e.getMessage(), e);
        }
    }

    @Test(priority = 0)
    public void updateDataWithPUT(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("firstName", "Chandler");
            jsonObject.put("lastName", "Binge");

            //update value in Nested JSON (Value for Hair object)
            JSONObject hairObject = new JSONObject();
            hairObject.put("color", "Brown");
            hairObject.put("type", "Strands");
            jsonObject.put("hair", hairObject);

            String updateValue = jsonObject.toJSONString();
            System.out.println(updateValue);

            //update data in PUT API Request
            given().baseUri(baseURI).basePath(userPath).header("Content-Type", "application/json").
                    accept(ContentType.JSON).body(updateValue).put(userID).then().assertThat().
                    statusCode(SC_OK).log().all();
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    @Test(priority = 1)
    public void updateDataWithPATCH(){
        try{
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("eyeColor", "Blue");

            //update value in Nested JSON (Value for Bank object)
            JSONObject bankObject = new JSONObject();
            bankObject.put("cardExpire", "10/23");
            bankObject.put("cardType", "VISA");
            jsonObject.put("bank", bankObject);

            String updateValue = jsonObject.toJSONString();
            System.out.println(updateValue);

            //update data in PUT API Request
            given().baseUri(baseURI).basePath(userPath).header("Content-Type", "application/json").
                    accept(ContentType.JSON).body(updateValue).patch(userID).then().assertThat().
                    statusCode(SC_OK).log().all();
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

}
