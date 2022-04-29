package ru.praktikum;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetCourierOrdersNegativeTest {

    Courier courier = new Courier("densky", "password", "lalka");

    @Test
    public void getCourierOrdersListTest() {

        CourierClient.create(courier);
        Integer id = CourierClient.login(courier);
        CourierClient.delete(id);

        given()
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?courierId={id}", id)
                .then().assertThat().body("message", equalTo("Курьер с идентификатором " + id + " не найден"))
                .and()
                .statusCode(404);
    }
}
