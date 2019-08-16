package com.vytrack.tests_API.API;
import com.vytrack.utilities.ConfigurationReader;
import cucumber.api.java.it.Ma;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class APIDay5_PostThenGet {

    @Test
    public void postEmployeeThenEmp(){
        double randomId = new Random().nextInt(99999);
        String url = ConfigurationReader.get("API_employees");

        Map reqEmployee = new HashMap<>();
        reqEmployee.put("employee_id", randomId);
        reqEmployee.put("first_name", "PostName");
        reqEmployee.put("last_name", "PostLname");
        reqEmployee.put("email", "EM" + randomId);
        reqEmployee.put("hire_date", "2019-09-24T11:23:00Z");
        reqEmployee.put("job_id", "IT_PROG");
        reqEmployee.put("salary", 1200);
        reqEmployee.put("commision_pct", null);
        reqEmployee.put("manager_id", null);
        reqEmployee.put("department_id", 90.0);

        //Given Content type and Accept type is JSON
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(reqEmployee).when().post(url);
        assertEquals(response.statusCode(), 201);

        Map postResEmployee = response.body().as(Map.class);

        //And Response JSON should contaon Empl info
        for (Object key : reqEmployee.keySet()) {
            System.out.println(postResEmployee.get(key) + " <> "+reqEmployee.get(key));
            assertEquals(postResEmployee.get(key), reqEmployee.get(key));

        }
        //When i send a Get request with "1100" id
        //Then Status code is 200
        //And employee JSON response data should match the posted jspn data

        response = given().accept(ContentType.JSON)
                    .when().get(url+randomId);

        Map getResMap = response.body().as(Map.class);
        for (Object keys : reqEmployee.keySet()) {
            System.out.println(keys + ": " + reqEmployee.get(keys) + " <> "+ getResMap.get(keys));
            assertEquals(getResMap.get(keys), reqEmployee.get(keys));
        }



    }
}
