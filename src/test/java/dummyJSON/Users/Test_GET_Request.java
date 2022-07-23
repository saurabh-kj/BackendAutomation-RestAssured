package dummyJSON.Users;

import com.jayway.jsonpath.Configuration;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.core.IsNull;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.SubSequentCalls;

import java.io.FileInputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasItems;

/**
 * Created by Saurabh Kumar. Using fake APIs https://dummyjson.com/
 */

public class Test_GET_Request {
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
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
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
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
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
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    @Test(priority = 3)
    public void readDataByID(){
        try{
            String userID = "2";
            given().baseUri(baseURI).basePath(userPath).get(userID).then().assertThat().
                    statusCode(SC_OK).log().all();
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    @Test(priority = 4)
    public void fetchParticulerField(){
        try {
            Response response = given().baseUri(baseURI).basePath(userPath).get(userID).then().assertThat().
                    statusCode(SC_OK).extract().response();
            JsonPath jPath = response.jsonPath();

            //Fetch First Name
            String fname = jPath.get("firstName");
            System.out.println("First Name: " + fname);

            //Fetch hair color
            String hairColor = jPath.get("hair.color");
            System.out.println(fname +"'s hair color is: " + hairColor);

            //Fetch user's address
            String address = jPath.get("address.address");
            System.out.println(fname + "'s address is: " + address);

            //Fetch user's city
            String city = jPath.get("address.city");
            System.out.println(fname + "'s city is: " + city);
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    /*@Test
    public void verifyBlondHairUsers(){
        try {
//            given().baseUri(baseURI).basePath(userPath).get("/filter?key=hair.color=Black").
//                    then().assertThat().statusCode(SC_OK).extract().response();
//            System.out.println(response.getBody().prettyPrint());
            given().baseUri(baseURI).baseUri(userPath).queryParam("key", "hair.color=Black").get("/filter").
                    getBody().prettyPrint();
//            System.out.println(res.getBody().prettyPrint());
        }catch (Exception e){

        }
    }*/
}
