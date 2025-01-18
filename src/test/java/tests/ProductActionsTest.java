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

import pages.ProductPage;
import utils.Driver;

// Test senaryosunun ait olduğu genel başlık ve özellikleri belirtiyoruz
@Epic("LC Waikiki E-Ticaret Uygulaması")
@Feature("Ürün İşlemleri ve Sepete Ekleme")
public class ProductActionsTest extends BaseTest {

    // Loglama yapmak için Log4j Logger nesnesi tanımlanıyor
    private static final Logger logger = LogManager.getLogger(ProductActionsTest.class);

    ProductPage productPage;

    @BeforeClass
    public void setupPages() {
        // PageFactory kullanılarak ProductPage nesnesi başlatılıyor.
        // Bu, sayfayla ilişkili öğelerin Selenium tarafından yönetilmesini sağlar.
        productPage = PageFactory.initElements(Driver.getDriver(), ProductPage.class);
    }

    @Test(priority = 7, dependsOnMethods = "tests.CategorySelectionTest.selectOutFit")
    @Description("Ürünleri sıralar.")
    public void sortProducts() {
        performProductSorting("En çok satanlar"); // Ürünleri belirli bir kritere göre sıralıyoruz
    }

    @Step("Ürünler sıralanıyor: {sortingOption}")
    public void performProductSorting(String sortingOption) {
        logger.info("Ürün sıralama işlemi başlatıldı: " + sortingOption);
        productPage.selectSortingOption(sortingOption);
        logger.info("Ürünler başarıyla sıralandı: " + sortingOption);
    }

    @Test(priority = 8, dependsOnMethods = "sortProducts")
    @Description("Ürün detaylarını görüntüler.")
    public void ProductDetails() {
        performViewProductDetails();
    }

    @Step("Ürün detayları görüntüleniyor.")
    public void performViewProductDetails() {
        logger.info("Ürün detayları görüntüleniyor.");
        productPage.viewProductDetails(); //
        logger.info("Ürün detayları başarıyla görüntülendi.");
    }

    @Test(priority = 9, dependsOnMethods = "ProductDetails")
    @Description("Ürün için beden seçimi yapar ve sepete ekler.")
    public void selectSizeAndAddToCart() {
        performAddToCart();
    }

    @Step("Ürün sepete ekleniyor.")
    public void performAddToCart() {
        logger.info("Ürün sepete ekleniyor.");
        productPage.selectSizeAndAddToCart();
        logger.info("Ürün sepete başarıyla eklendi.");
    }
}






