import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrderListTest {
    private final CourierSteps createCourier = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void shouldReturnOrdersList() {
        Integer courierId = createCourier.loginNewCourier().extract().body().path("id");
        Object listOfOrder = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders?courierId" + courierId)
                .then()
                .extract().body().path("orders");
        System.out.println(listOfOrder);
    }

    @After
    public void tearDown() {
        Integer id = createCourier.loginNewCourier().extract().body().path("id");
        if (id != null) {
            createCourier.deleteCourier(id);
        }
    }
}
