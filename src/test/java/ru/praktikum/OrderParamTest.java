package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderParamTest {

    String firstName;
    String lastName;
    String address;
    int metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    String[] color;
    boolean expected;

    public OrderParamTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color, boolean expected) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] getLionSexData() {
        return new Object[][] {
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{"BLACK"},
                        true
                },
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{"BLACK", "GREY"},
                        true
                },
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{},
                        true
                },

        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void createOrderTest() {
        Order clientOrderBodyRq = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        //запрос на создание заказа и проверка тела ответа и статус кода
        Response rs =
                given().log().all()
                        .header("Content-type", "application/json")
                        .body(clientOrderBodyRq)
                        .when()
                        .post("/api/v1/orders");
        rs.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);

        Integer track = rs.then().extract().body().path("track");

        boolean actual = !track.equals(0);
        assertEquals(expected, actual);
    }
}