import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CourierSteps {
    File courierData = new File("src/test/java/resources/newCourier.json");
    public ValidatableResponse createNewCourier() {
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierData)
                        .when()
                        .post("/api/v1/courier").then();
    }
    public ValidatableResponse createCourierWithData(String courierData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierData)
                .when()
                .post("/api/v1/courier").then();
    }

    public ValidatableResponse loginNewCourier() {
        return given()
                .contentType(ContentType.JSON)
                .body(courierData)
                .when()
                .post("/api/v1/courier/login").then();
    }
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}")
                .then();
    }
}
