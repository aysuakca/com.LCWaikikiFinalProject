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
import pages.FavoritesPage;
import utils.Driver;

@Epic("LC Waikiki E-Ticaret Uygulaması")
@Feature("Favoriler İşlemleri")
public class FavoritesTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FavoritesTest.class); // Loglama için Log4j Logger oluşturuluyor
    FavoritesPage favoritesPage;

    @BeforeClass
    public void setupPages() {
        // PageFactory kullanılarak FavoritesPage nesnesi başlatılıyor
        favoritesPage = PageFactory.initElements(Driver.getDriver(), FavoritesPage.class);
    }

    @Test(priority = 12, dependsOnMethods = "tests.CartVerificationTest.verifyCartDetailsTest")
    @Description("Ürünü favorilere ekler ve favori ürün doğrulaması yapar.")
    public void addToFavoritesTest() {
        performAddToFavorites();
        performNavigateToFavoritesPage();
        performVerifyFavoriteProduct();
        logger.info("Favoriler testi başarıyla tamamlandı.");
    }

    @Step("Favorilere ekleme işlemi") // Allure raporunda adım olarak görünecek
    public void performAddToFavorites() {
        logger.info("Ürün favorilere ekleniyor."); // Log: Favorilere ekleme işlemi başlatılıyor
        favoritesPage.addToFavorites(); // Favorilere ekleme işlemi gerçekleştirilir
        logger.info("Ürün başarıyla favorilere eklendi."); // Log: İşlem tamamlandı
    }

    @Step("Favorilerim sayfasına git")
    public void performNavigateToFavoritesPage() {
        logger.info("Favorilerim sayfasına gidiliyor.");
        favoritesPage.navigateToFavoritesPage();
        logger.info("Favorilerim sayfasına başarıyla gidildi.");
    }

    @Step("Favori ürün doğrulaması yap")
    public void performVerifyFavoriteProduct() {
        logger.info("Favori ürün doğrulaması yapılıyor.");
        favoritesPage.verifyFavoriteProduct();
        logger.info("Favorilerdeki ürün başarıyla doğrulandı.");
    }
}



