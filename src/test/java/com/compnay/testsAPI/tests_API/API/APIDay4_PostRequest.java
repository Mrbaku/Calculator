package com.vytrack.tests_API.API;

import com.vytrack.tests_API.Beans.Countries;
import com.vytrack.tests_API.Beans.Region;
import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class APIDay4_PostRequest {

    /*
    Given content type is Json
    And Accept type is Json
    When I send POST request to
    http://34.205.63.51:1000/ords/hr/regions
    with request body:
    {"region_id" : 69,"region_name" : "MrBaku"}
     Then status code should be 200
     And response body should match request body
  */

    @Test
    public void postNewRegion(){
        String url = ConfigurationReader.get("API_regions");
       // String requestJson = "{\"region_id\" : 1991,\"region_name\" : \"MrBaku\"}";
        Map requestMap = new HashMap<>();
        requestMap.put("region_id", 6969);
        requestMap.put("region_name", "MrbakuNumberOne");

        Response response = given().accept(ContentType.JSON)
                             .and().contentType(ContentType.JSON)
                             .and().body(requestMap)
                             .when().post(url);
        System.out.println(response.statusLine());
        response.prettyPrint();
        assertEquals(response.statusCode(), 201);

        Map responseMap = response.body().as(Map.class);
        assertEquals(responseMap.get("region_id"), requestMap.get("region_id"));
        assertEquals(responseMap.get("region_name"), requestMap.get("region_name"));


    }

    @Test
    public void postUsingPOJO(){
        String url = ConfigurationReader.get("API_regions");
        Region region = new Region();
        region.setRegionId(new Random().nextInt(99999));
        region.setRegionName("BOOM");

        Response response = given().accept(ContentType.JSON)
                            .and().contentType(ContentType.JSON)
                            .and().body(region)
                            .when().post(url);
        assertEquals(response.statusCode(), 201);

        Region responseRegion = response.body().as(Region.class);

        //And response body should match request body
        //region id and region name must match
        assertEquals(responseRegion.getRegionId(), region.getRegionId());
        assertEquals(responseRegion.getRegionName(), region.getRegionName());
    }

     /*
    Given content type is Json
    And Accept type is Json
    When I send POST request to
    http://34.205.63.51:1000/ords/hr/regions
    with request body:
    "country_id": "AR",
    "country_name": "Argentina",
    "region_id": 2,     Then status code should be 200
     And response body should match request body
  */

     @Test
        public void postUsingPOJO02(){
         String url = ConfigurationReader.get("API_countries");
         Countries countries = new Countries();
         countries.setCountryId("FF");
         countries.setCountryName("A country");
         countries.setRegionId(4);

         Response response = given().log().all().accept(ContentType.JSON).and().contentType(ContentType.JSON)
                                .and().body(countries).when().post(url);
         assertEquals(response.statusCode(), 201);

         Countries resCountry = response.body().as(Countries.class);
         assertEquals(resCountry.getCountryId(), countries.getCountryId());
         assertEquals(resCountry.getCountryName(), countries.getCountryName());
         assertEquals(resCountry.getRegionId(), countries.getRegionId());



     }
}
