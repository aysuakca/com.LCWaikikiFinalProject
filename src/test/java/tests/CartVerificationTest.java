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
import pages.CartPage;
import pages.CategoryPage;
import utils.Driver;
import utils.TestData;

@Epic("LC Waikiki E-Ticaret Uygulaması")
@Feature("Sepet İşlemleri ve Doğrulama")
public class CartVerificationTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(CartVerificationTest.class);
    CartPage cartPage;
    CategoryPage categoryPage;

    @BeforeClass
    public void setupPages() {
        // PageFactory kullanılarak CartPage ve CategoryPage nesnelerini başlatıyoruz
        cartPage = PageFactory.initElements(Driver.getDriver(), CartPage.class);
        categoryPage = PageFactory.initElements(Driver.getDriver(), CategoryPage.class);
        logger.info("CartPage ve CategoryPage nesneleri başarıyla başlatıldı.");
    }

    @Test(priority = 10, dependsOnMethods = "tests.ProductActionsTest.sortProducts")
    @Description("Sepetteki ürün miktarını artırır.")
    public void increaseProductQuantityTest() {
        logger.info("Ürün miktarını artırma testi başlatılıyor.");
        performIncreaseProductQuantity(5); // Sepetteki ürün miktarını artırmak için method çağrılır
        logger.info("Ürün miktarını artırma testi başarıyla tamamlandı.");
    }

    @Step("Sepetteki ürün miktarını artır: Hedef Adet = {desiredQuantity}")
    public void performIncreaseProductQuantity(int desiredQuantity) {
        logger.info("Sepetteki ürün miktarı artırılıyor. Hedef Adet: " + desiredQuantity);
        int finalQuantity = cartPage.increaseProductQuantity(desiredQuantity);
        TestData.selectedQuantity = finalQuantity; // Sonuç, TestData'ya kaydedilir
        logger.info("Ürün miktarı artırma işlemi tamamlandı. Son Adet: " + finalQuantity);
    }

    @Test(priority = 11, dependsOnMethods = "tests.CartVerificationTest.increaseProductQuantityTest")
    @Description("Sepetteki ürün detaylarını doğrular.")
    public void verifyCartDetailsTest() {
        logger.info("Sepet detaylarını doğrulama testi başlatılıyor.");
        performVerifyCartDetails(); // Sepetteki ürün detaylarını doğrulama methodu çağrılır
        logger.info("Sepet detaylarını doğrulama testi başarıyla tamamlandı.");
    }

    @Step("Sepet detaylarını doğrula")
    public void performVerifyCartDetails() {
        logger.info("Sepetteki ürün detayları doğrulanıyor.");
        cartPage.verifyCartDetails(TestData.selectedProductName, TestData.selectedColor, TestData.selectedQuantity); // Sepetteki ürün detayları doğrulanır
        logger.info("Ürün detayları ve sepetteki bilgiler başarıyla doğrulandı.");
    }
}
