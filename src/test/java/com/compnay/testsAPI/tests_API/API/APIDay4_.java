package com.vytrack.tests_API.API;

import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class APIDay4_ {

    @Test
    public void warmUp() {
        Map<String, Integer> rParamMap = new HashMap<>();
        rParamMap.put("limit", 10);

        Response response = given().accept(ContentType.JSON) // header
                .and().params(rParamMap) //query param/request param
                .when().get(ConfigurationReader.get("API_url") + "/regions");
        assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPath = response.jsonPath();
        assertEquals(jsonPath.getInt("items[0].region_id"), 1);
        assertEquals(jsonPath.getString("items[0].region_name"), "Europe");
        assertEquals(jsonPath.getInt("items[1].region_id"), 2);
        assertEquals(jsonPath.getString("items[1].region_name"), "Americas");
        assertEquals(jsonPath.getInt("items[2].region_id"), 3);
        assertEquals(jsonPath.getString("items[2].region_name"), "Asia");
        assertEquals(jsonPath.getInt("items[3].region_id"), 4);
        assertEquals(jsonPath.getString("items[3].region_name"), "Middle East and Africa");


    }

    @Test
    public void warmUp2() {
        Map<String, Integer> rParamMap = new HashMap<>();
        rParamMap.put("limit", 10);

        Response response = given().accept(ContentType.JSON) // header
                .and().params(rParamMap) //query param/request param
                .when().get(ConfigurationReader.get("API_url") + "/regions");
        assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPath = response.jsonPath();
        //deserialize json to List Map
        List<Map> regions = jsonPath.getList("items", Map.class);

        //this map is representing the expected key value
        Map<Integer, String> expectedRegions = new HashMap<>();
        expectedRegions.put(1, "Europe");
        expectedRegions.put(2, "Americas");
        expectedRegions.put(3, "Asia");
        expectedRegions.put(4, "Middle East and Africa");


        for (Integer regionId : expectedRegions.keySet()) {
            System.out.println("Looking for region: " + regionId);
            for (Map map : regions) {
                if (map.get("region_id") == regionId) {
                    assertEquals(map.get("region_name"), expectedRegions.get(regionId));
                }

            }

        }

    }

    @Test
    public void warmUp03(){

        List<String> testingData = Arrays.asList("1 Europe", "2 Americas", "3 Asia", "4 Middle East and Africa");
        String url = ConfigurationReader.get("API_url")+"/regions";
        Response response = given().accept(ContentType.JSON).and().params("limit", 10).when().get(url);
        assertEquals(response.statusCode(), 200);

        JsonPath json = response.jsonPath();

        List<String> regionNames = new ArrayList<>();

        for (Object item : json.getList("items")) {
            regionNames.add(((HashMap)item).get("region_id").toString()+" "
                    + ((HashMap)item).get("region_name").toString());
        }
        assertTrue(regionNames.containsAll(testingData));
    }

}
