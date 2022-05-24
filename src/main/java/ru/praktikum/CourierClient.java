package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String baseURI = "http://qa-scooter.praktikum-services.ru/";

    private static RequestSpecification baseSpec = new RequestSpecBuilder()
            .setBaseUri(baseURI)
            .setContentType("application/json")
            .log(LogDetail.ALL)
            .build();

    public static RequestSpecification getBaseSpec() {
        return baseSpec;
    }

    public static String getBaseURI() {
        return baseURI;
    }

    @Step("Запрос на создание курьера: {courier}")
    public static ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().log().all();
    }

    @Step("Запрос на логин курьера: {courier}, который возвращает его id")
    public static ValidatableResponse loginCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().log().all();
    }

    @Step("Запрос на удаление курьера с id: {id}")
    public static void deleteCourier(Courier courier) {
        Integer id = loginCourier(courier).and().extract().body().path("id");
        given()
                .spec(getBaseSpec())
                .when()
                .delete("/api/v1/courier/{id}", id)
                .then().log().all();
    }
}
