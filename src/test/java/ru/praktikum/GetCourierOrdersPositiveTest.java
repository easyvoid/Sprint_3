package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetCourierOrdersPositiveTest {

    Courier courier = new Courier("densky", "password", "lalka");
    LoginId loginId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

        //запрос на создание нового курьера
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier").then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        //логин созданным курьером, чтобы получить его id
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        loginId = response.body().as(LoginId.class);
    }

    @Test
    public void getCourierOrdersListTest() {
        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?courierId=" + loginId.getId())
                .then().assertThat().body("orders", empty())
                .and()
                .statusCode(200);
    }

    @After
    public void tearDown() {
        given()
                .header("Content-type", "application/json")
                .body(loginId)
                .when()
                .delete("/api/v1/courier/" + loginId.getId())
                .then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }
}
