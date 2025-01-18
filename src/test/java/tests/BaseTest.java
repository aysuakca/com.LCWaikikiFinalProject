package tests;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import pages.LoginPage;
import utils.Driver;

public class BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class); // Loglama için Log4j Logger nesnesi
    LoginPage loginPage;

    @BeforeSuite
    @Step("Test ortamı kuruluyor...")
    public void setup() {
        logger.info("Test ortamı kuruluyor...");

        // Tarayıcıyı başlatma ve siteye gitme
        Driver.getDriver().get("https://www.lcw.com");
        logger.info("Tarayıcı başlatıldı ve LC Waikiki web sitesine gidildi.");

        // PageFactory ile LoginPage nesnesini başlatma
        loginPage = PageFactory.initElements(Driver.getDriver(), LoginPage.class);
        logger.info("LoginPage nesnesi başarıyla başlatıldı.");

        // Çerez banner'ını kapatma
        loginPage.closeCookiesBanner();
        logger.info("Çerez banner'ı kapatıldı.");
    }

    @BeforeMethod
    @Step("Bir sonraki test için bekleniyor...")
    public void waitBeforeTest() throws InterruptedException {
        logger.info("Bir sonraki test için 3 saniye bekleniyor...");
        Thread.sleep(3000); // 3 saniye bekleme
    }

    @AfterSuite
    @Step("Test tamamlandı, tarayıcı kapatılıyor...")
    public void tearDown() {
        logger.info("Test tamamlandı, tarayıcı kapatılıyor...");

        // Tüm testlerden sonra tarayıcıyı kapat
        Driver.quitDriver();
        logger.info("Tarayıcı başarıyla kapatıldı.");
    }
}


