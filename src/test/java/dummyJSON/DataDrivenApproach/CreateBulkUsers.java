package dummyJSON.DataDrivenApproach;

import com.jayway.jsonpath.Configuration;
import dummyJSON.Users.Test_GET_Request;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ReadFile;
import utils.SubSequentCalls;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static utils.ExcelUtils.*;

public class CreateBulkUsers {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Test_GET_Request.class);
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
    @Test(dataProvider = "createBulkUsers")
    public void createBulkUsers(String firstName, String lastName, String age){
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("age", lastName);

            String addData = jsonObject.toJSONString();
            System.out.println(addData);

            // Adding data to POST API
            Response response = given().baseUri(baseURI).header("Content-Type", "application/json").
                    contentType(ContentType.JSON).accept(ContentType.JSON).
                    body(addData).when().post(userAdd).then().assertThat().statusCode(SC_CREATED).
                    extract().response();
            String responseBody = response.getBody().asString();
            System.out.println(response.getBody().prettyPrint());

            //Assertion for inserted data
            Assert.assertEquals(responseBody.contains(firstName), true);
            Assert.assertEquals(responseBody.contains(lastName), true);
            Assert.assertEquals(responseBody.contains(age), true);
        }catch (Exception e){
            logger.error("Exception: ", e);
            Assert.fail("Exception thrown. Test Case Failed due to: " + e.getMessage(), e);
        }
    }

    @DataProvider(name = "createBulkUsers")
    String[][] getUserData() throws IOException {
        //Read data from excel
        String path = "./src/test/testdata/users.xlsx";
        int row_count = getRowCount(path, "Sheet1");
        int col_count = getCellCount(path, "Sheet1", 1);

        String userData[][] = new String[row_count][col_count];
        for (int i = 1; i <= row_count ; i++) {                                       // for rows
            for (int j = 0; j < col_count; j++) {                                     // for columns
                userData [i-1][j] = getCellData(path, "Sheet1", i, j);
            }
        }
        return userData ;
    }
}
