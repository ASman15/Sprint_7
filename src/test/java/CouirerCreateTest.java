import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

public class CouirerCreateTest {
    String login;
    String password = "1234";
    String firstName = "altair";
    private final CourierSteps createCourier = new CourierSteps();
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    public void createNewCourierSucesfell() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier
                .createNewCourier(login, password, firstName)
                .statusCode(201).and().body("ok", is(true));
    }
    @Test
    public void notAllowedToCreateTwoIdenticalCouriers() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier
                .createNewCourier(login, password, firstName).statusCode(201);
        createCourier
                .createNewCourier(login, password, firstName).statusCode(409);
    }

    @Test
    public void createCourierWithoutPassword(){
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier
                .createNewCourier(login, "", firstName).statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
    @Test
    public void createCourierWithoutLogin() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier
                .createNewCourier("", password, firstName).statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
    @Test
    public void createCourierWithRepetitiveLogin() {
        login = RandomStringUtils.randomAlphabetic(10);
        createCourier
                .createNewCourier(login, password, firstName).statusCode(201);
        createCourier
                .createNewCourier(login, "1234", "ecio").statusCode(409)
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }
    @After
    public void tearDown() {
        login = RandomStringUtils.randomAlphabetic(10);
        Integer courierId = createCourier.loginNewCourier(login, password).extract().body().path("id");
        if (courierId != null) {
            createCourier.deleteCourier(courierId).statusCode(200).and().body("ok", is(true));
        }
    }
}
