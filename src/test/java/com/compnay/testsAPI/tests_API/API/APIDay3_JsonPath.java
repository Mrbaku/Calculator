package com.compnay.testsAPI.tests_API.API;

import com.compnay.Utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.junit.Assert.assertEquals;

public class APIDay3_JsonPath {

    /*
    Given Accept type is json
    When I send GET request to REST url
    http://34.205.63.51:1000/ords/hr/regions
    Then status code is 200
    And response content should be json
    And 4 regions shoud be retured
    And Americas is one of the regionn names



     */

    @Test
    public void regionTest() {
        given().accept(ContentType.JSON)
                .when().get(ConfigurationReader.get("API_url") + "/regions")
                .then().statusCode(200)
                .and().contentType(ContentType.JSON)
                .and().body("items.region_id", hasSize(4))
                //regions, validation of multiple values
                .and().assertThat().body("items.region_name", hasItem("Americas"))
                .and().assertThat().body("items.region_name", hasItems("Americas", "Asia"));
    }

    /*
    Given Accept type is JSON
    And Params are limit 100
    When I send GET request to
    http://34.205.63.51:1000/ords/hr/employee
    Then Status code is 200
    And Response content should be Json
    And 100 employees data should be Json Response body

     */

    @Test
    public void paramTest01() {
        given().accept(ContentType.JSON)
                .and().params("limit", 100)
                .when().get(ConfigurationReader.get("API_url") + "/employees")
                .then().statusCode(200)
                .and().assertThat().contentType(ContentType.JSON)
                .and().assertThat().body("items.employee_id", hasSize(100));

    }

    /*

    Given Accept type is JSON
    And Params are limit 100
    And path param is 110
    When I send GET request to
    http://34.205.63.51:1000/ords/hr/employee
    Then Status code is 200
    And Response content should be Json
    And 100 employees data should be Json Response body

     */

    @Test
    public void pathParamTest02() {
        given().accept(ContentType.JSON)
                .and().params("limit", 100)
                .and().pathParams("employee_id", 110)
                .when().get(ConfigurationReader.get("API_url") + "/employees/{employee_id}")
                .then().statusCode(200)
                .and().contentType(ContentType.JSON)
                .and().assertThat().body("employee_id", equalTo(110));

    }

    /*
       Given Accept type is JSON
    And Params are limit 100
    When I send GET request to
    http://34.205.63.51:1000/ords/hr/employee
    Then Status code is 200
    And Response content should be Json
    All employee ids should be returned
     */

    @Test
    public void testWithJsonPath() {

        Map<String, Integer> rParamMap = new HashMap<>();
        rParamMap.put("limit", 100);

        Response response = given().accept(ContentType.JSON) // header
                .and().params(rParamMap) //query param/request param
                .and().pathParams("employee_id", 177) //path param
                .when().get(ConfigurationReader.get("API_url") + "/employees/{employee_id}");

        JsonPath json = response.jsonPath(); //get json body and assing to jsonPath object
        System.out.println(json.getInt("employee_id"));
        System.out.println(json.getString("last_name"));
        System.out.println(json.getString("job_id"));
        System.out.println(json.getInt("salary"));
        System.out.println(json.getString("links[0].href"));

        //assign all hrefs into a list of strings

        List<String> hrefs = json.getList("links.href");
    }

    /*
         Given Accept type is JSON
    And Params are limit 100
    When I send GET request to
    http://34.205.63.51:1000/ords/hr/employee
    Then Status code is 200
    And Response content should be Json
    All employee ids should be returned

     */

    @Test
    public void testJsonPathWithLists(){
        Map<String, Integer> rParamMap = new HashMap<>();
        rParamMap.put("limit", 100);

        Response response = given().accept(ContentType.JSON) // header
                .and().params(rParamMap) //query param/request param
                .when().get(ConfigurationReader.get("API_url") + "/employees");

        assertEquals(response.statusCode(),200);
        //JsonPath json = new JsonPath(FilePath.json)
        //JsonPath json = new JsonPath(respnose.asString())
        JsonPath json = response.jsonPath();

        //get all empl_id into an arrayList

        List<Integer> empIds = json.getList("items.employee_id");
        System.out.println(empIds);
        //assert that there are 100 employees_id
        assertEquals(empIds.size(), 100);

        //get all emails and assign it to email list
        List<String> emails = json.getList("items.email");
        System.out.println(emails);
        assertEquals(emails.size(), 100);

        //get all employee ids that are greater than 150
        List<String> empIdsList = json.getList("items.findAll{it.employee_id >150}.employee_id");
        System.out.println(empIdsList);

        //get all emp last names whose salary is more than 7000
        List<String> lastnames = json.getList("items.findAll{it.salary >7000}.last_name");
        System.out.println(lastnames);


    }


}
