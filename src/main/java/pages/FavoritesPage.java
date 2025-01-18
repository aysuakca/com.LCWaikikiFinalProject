package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestData;

import java.time.Duration;
import java.util.List;

public class FavoritesPage {
    WebDriver driver;
    private static final Logger logger = LogManager.getLogger(FavoritesPage.class);
    WebDriverWait wait;

    // Web elementlerini tanımlıyoruz
    @FindBy(xpath = "//a[contains(@class, 'cart-add-to-favorite')]/i[contains(@class, 'fa-heart')]")
    public WebElement favoriteButton; // Favorilere ekleme butonu

    @FindBy(xpath = "//a[@href='/favorilerim']//*[name()='svg']")
    public WebElement favoritesIcon; // Favorilerim sayfasına yönlendiren buton

    @FindBy(xpath = "//span[@class='rd-cart-item-code']")
    public List<WebElement> favoriteProducts; // Favorilerim sayfası

    public FavoritesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    //Sepetteki ürünü favorilerime ekleme
    @Step("Seçilen ürün favorilere ekleniyor:")
    public void addToFavorites() {
        try {
            logger.info("Favorilere ekleme işlemi başlatılıyor.");
            wait.until(ExpectedConditions.elementToBeClickable(favoriteButton)).click();
            logger.info("Favorilere ekle butonuna tıklandı.");

            wait.until(ExpectedConditions.attributeContains(favoriteButton, "class", "favorited"));
            logger.info("Ürün başarıyla favorilere eklendi.");
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("Favorilere ekleme sırasında bir sorun oluştu: " + e.getMessage());
        }
    }

    //Favorilerim sayfasına gitme
    @Step("Favorilerim sayfasına gidiliyor:")
    public void navigateToFavoritesPage() {
        try {
            logger.info("Favorilerim sayfasına yönlendirme işlemi başlatılıyor.");
            wait.until(ExpectedConditions.elementToBeClickable(favoritesIcon)).click();
            logger.info("Favorilerim sayfasına başarıyla gidildi.");
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("Favorilerim sayfasına yönlendirme sırasında bir sorun oluştu: " + e.getMessage());
        }
    }

    //Seçilen ürünün adı ile favorilerim sayfasındaki ürün adını doğrulama
    @Step("Favorilerimde ürün doğrulaması yapılıyor:")
    public void verifyFavoriteProduct() {
        try {
            logger.info("Favorilerdeki ürün doğrulama işlemi başlatılıyor.");
            wait.until(ExpectedConditions.visibilityOfAllElements(favoriteProducts));

            boolean productFound = false;
            for (WebElement product : favoriteProducts) {
                if (product.getText().equals(TestData.selectedProductName)) {
                    logger.info("Ürün favorilerde bulundu: " + TestData.selectedProductName);
                    productFound = true;
                    break;
                }
            }

            if (!productFound) {
                logger.warn("Ürün favorilerde bulunamadı: " + TestData.selectedProductName);
            }
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("Favoriler doğrulama sırasında bir sorun oluştu: " + e.getMessage());
        }
    }
}








