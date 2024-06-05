import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class LoginCourierTest {
    String login;
    String password = "1234";
    String firstName = "altair";
    private final CourierSteps createCourier = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void isCourierCanAutorize() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier.createNewCourier(login, password, firstName);
        createCourier.loginNewCourier(login, password).statusCode(200);
    }
    @Test
    public void courierCanNotAutorizedWithoutLogin() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier.createNewCourier(login, password, firstName);
        createCourier.loginNewCourier("", password)
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
    }
    @Test
    public void courierCanNotAutorizedWithoutPassword() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier.createNewCourier(login, password, firstName);
        createCourier.loginNewCourier(login, "")
                        .statusCode(400)
                        .body("message", is("Недостаточно данных для входа"));
    }
    @Test
    public void courierCanNotAutorizedIfLogAndPassIncorrect() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier.createNewCourier(login, password, firstName);
        createCourier.loginNewCourier("ninja", "1111")
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }
    @Test
    public void idIsReturning() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier.createNewCourier(login, password, firstName);
        Integer id = createCourier.loginNewCourier(login, password).extract().body().path("id");
        System.out.println(id);
    }


    @After
    public void tearDown() {
        Integer id = createCourier.loginNewCourier(login, password).extract().body().path("id");
        if (id != null) {
            createCourier.deleteCourier(id).statusCode(200).and().body("ok", is(true));
        }
    }
}
