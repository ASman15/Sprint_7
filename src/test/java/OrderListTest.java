import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderListTest {
    private final CourierSteps createCourier = new CourierSteps();
    private final OrderSteps createOrder = new OrderSteps();
    String login = "asassin";
    String password = "1234";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void ordersListIsNotNull() {
        Integer courierId = createCourier
                .loginNewCourier(login, password).extract().body().path("id");
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders?courierId" + courierId)
                .then()
                .assertThat().statusCode(200).and()
                .body("orders", notNullValue());
    }
    @Test
    public void shouldReturnOrdersList() {
        Integer courierId = createCourier
                .loginNewCourier(login, password).extract().body().path("id");
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
        Integer id = createCourier.loginNewCourier(login, password).extract().body().path("id");
        if (id != null) {
            createCourier.deleteCourier(id);
        }
    }
}
