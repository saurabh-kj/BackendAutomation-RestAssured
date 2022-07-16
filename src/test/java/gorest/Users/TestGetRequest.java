package gorest.Users;

import com.jayway.jsonpath.Configuration;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.hamcrest.core.IsNull;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utills.SubSequentCalls;

import java.io.FileInputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasItems;

/**
 * Created by Saurabh Kumar. Using fake APIs https://dummyjson.com/
 */

public class TestGetRequest {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestGetRequest.class);
    SubSequentCalls ssc = new SubSequentCalls();
    String baseURI;
    String userPath;
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
            userPath = properties.getProperty("UserPath");
            token = ssc.getToken();
        }catch (Exception e){
            logger.error("Exception found at pre-condition: ", e);
            Assert.fail("Exception found at pre-condition: "+ e.getMessage(), e);
        }
    }
    @Test(priority = 0)
    public void fetchAPIData(){
        Response response = given().request(Method.GET, baseURI+userPath);
        System.out.println(response.getBody().prettyPrint());  //to print GET API Data on console

        given().baseUri(baseURI).basePath(userPath).log().all().get().then().statusCode(SC_OK).log().headers();
    }

    @Test(priority = 1)
    public void verifyUserIDIsNotBlank(){
        try{
            given().baseUri(baseURI).basePath(userPath).get().then().assertThat().
                    statusCode(SC_OK).body("users.id", IsNull.notNullValue());
        }catch (Exception e){
            logger.error("Exception found: ", e);
            Assert.fail("Exception found: "+ e.getMessage(), e);
        }
    }

    @Test(priority = 2)
    public void verifyUserName(){
        int userID = 0;
        try {
            //First method
        given().baseUri(baseURI).basePath(userPath).when().get().then().assertThat().
                statusCode(SC_OK).body("users.firstName", hasItems("Sheldon"));
            /*
            //Second Method
            Response response = given().baseUri(baseURI).basePath(userPath).when().get().then().assertThat().
                    statusCode(SC_OK).and().body("name", hasItems("Sheldon")).extract().response();
            List<String> userNameList = response.path("name");
            for (int i = 0; i < userNameList.size(); i++) {
                if(response.path("name [" + "]").equals("Devika Varrier")){
                    userID = response.path("id[" + i + "]");
                    break;
                }
            }
            System.out.println("userID: " + userID);
            */
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown Test case failed :" + e.getMessage(), e);
        }
    }

    /*@Test(priority = 3)
    public void verifyBlondHairUsers(){
        try {
            Response response = given().baseUri(baseURI).basePath(userPath).when().get().then().
                    assertThat().statusCode(SC_OK)
                            .extract().response();
//            DocumentContext dc = JsonPath.using(configuration).parse(response);
            Map<String, String> map = response.jsonPath().getMap("users.hair");
            System.out.println(map.get("color"));
//            System.out.println(response.getBody().prettyPrint());
        }catch (Exception e){

        }
    }*/
}
