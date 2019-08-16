package com.vytrack.tests_API.API;

import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class APIDay3_GSON {

    @Test
    public void testWithJsonToHashMap() {
        Response response = given().accept(ContentType.JSON)
                .when().get(ConfigurationReader.get("API_url") + "/employees/120");

        HashMap<String, String> map = response.as(HashMap.class);
        System.out.println(map.keySet());
        System.out.println(map.values());

        assertEquals(map.get("employee_id"), 120);


    }

    @Test
    public void convertJsonToListOfMaps() {
        Response response = given().accept(ContentType.JSON)
                .when().get(ConfigurationReader.get("API_url") + "/departments");

        //convert the response that contains departments information into list of maps
        //List<Map<String, String>> listOfMaps = response.as(ArrayList.class);
        List <Map> listOfMaps = response.jsonPath().getList("items", Map.class);
        System.out.println(listOfMaps.get(0));

        //asert that first department name is "Administration"

        assertEquals(listOfMaps.get(0).get("department_name"), "Administration");

        }
}
