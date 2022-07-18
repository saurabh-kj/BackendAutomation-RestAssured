package dummyJSON.Users;

import com.jayway.jsonpath.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utills.SubSequentCalls;

import java.io.FileInputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by Saurabh Kumar. Using fake APIs https://dummyjson.com/
 */

public class Test_DELETE_Request {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Test_GET_Request.class);
    SubSequentCalls ssc = new SubSequentCalls();
    String baseURI;
    String userPath;
    Properties properties;
    String token;
    String userID = "3";

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
    public void deleteUser(){
        try{
            given().baseUri(baseURI).basePath(userPath).when().delete(userID).
                    then().assertThat().statusCode(SC_OK).log().all();
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }
}
