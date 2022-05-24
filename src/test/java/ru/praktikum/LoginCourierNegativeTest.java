package ru.praktikum;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierNegativeTest {

//    @Test
//    public void loginCourierWithoutLogin() {
//        Courier courier = new Courier("densky","password");
//        CourierClient.createCourier(courier);
//        Courier courierWithoutLogin = new Courier("password");
//        ValidatableResponse rs = CourierClient.loginCourier(courierWithoutLogin);
//        rs.assertThat().body("message", equalTo("Недостаточно данных для входа"))
//                .and()
//                .statusCode(400);
//        CourierClient.deleteCourier(courier);
//    }

    @Test
    public void loginCourierWithoutPassword() {
        Courier courier = new Courier("densky","password");
        CourierClient.createCourier(courier);
        ValidatableResponse rs = CourierClient.loginCourier(new Courier().setPassword("password"));
        rs.assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
        CourierClient.deleteCourier(courier);
    }

    @Test
    public void loginNonexistentCourier() {
        String login = RandomStringUtils.randomAlphabetic(9);
        String password = RandomStringUtils.randomAlphabetic(9);
        Courier courierRandom = new Courier(login, password);

        given()
                .baseUri(CourierClient.getBaseURI())
                .header("Content-type", "application/json")
                .body(courierRandom)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

}
