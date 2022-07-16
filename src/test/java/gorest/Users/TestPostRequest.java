package gorest.Users;

import com.jayway.jsonpath.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utills.ReadFile;
import utills.SubSequentCalls;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

public class TestPostRequest {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestGetRequest.class);
    Map<String, String> headerProperties;
    SubSequentCalls ssc = new SubSequentCalls();
    String baseURI;
    String postPath;
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
            postPath = properties.getProperty("PostPath");
            token = ssc.getToken();
        }catch (Exception e){
            logger.error("Exception found at pre-condition: ", e);
            Assert.fail("Exception found at pre-condition: "+ e.getMessage(), e);
        }
    }

    @Test
    public void verifyResponse201(){
        try{
            given().baseUri(baseURI).basePath(postPath).headers(headerProperties).log().all().when().post().then().
                    assertThat().statusCode(SC_CREATED).log().all();
        }catch (Exception e){
            logger.info("Exception found: ", e);
            Assert.fail("Exception found: " + e.getMessage(), e);
        }
    }
}
