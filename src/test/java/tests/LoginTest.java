package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.Driver;

@Epic("LC Waikiki E-Ticaret Uygulaması")
@Feature("Kullanıcı Giriş")
public class LoginTest extends BaseTest {
    // Log4j Logger'ı tanımlıyoruz, böylece her önemli olayı loglayabiliriz
    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    LoginPage loginPage;

    @BeforeClass
    public void setupPages() {
        // PageFactory kullanarak LoginPage nesnesini başlatıyoruz
        // Bu, sayfa elementlerini Selenium tarafından yönetilebilir hale getirir
        loginPage = PageFactory.initElements(Driver.getDriver(), LoginPage.class);
    }

    @Test(priority = 1)
    @Description("Kullanıcı giriş bilgilerini kullanarak sisteme giriş yapar.")
    public void loginTest() {
        logger.info("Kullanıcı giriş testi başlatılıyor.");
        performLogin("lcwbootcamptest@gmail.com", "test123");
        logger.info("Kullanıcı giriş testi başarıyla tamamlandı.");
    }

    @Step("Kullanıcı giriş yapıyor: E-posta = {email}, Şifre = {password}")
    public void performLogin(String email, String password) {
        loginPage.login(email, password);
    }
}
