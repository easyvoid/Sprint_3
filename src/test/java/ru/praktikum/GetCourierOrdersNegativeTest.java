package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetCourierOrdersNegativeTest {

    Courier courier = new Courier("densky", "password", "lalka");
    LoginId loginId;
    Integer id;

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
        id = response.then().extract().body().path("id");

        //запрос на удаление созданного ранее курьера
        given()
                .header("Content-type", "application/json")
                .body(loginId)
                .when()
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void getCourierOrdersListTest() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?courierId={id}", id)
                .then().assertThat().body("message", equalTo("Курьер с идентификатором " + id + " не найден"))
                .and()
                .statusCode(404);
    }
}
