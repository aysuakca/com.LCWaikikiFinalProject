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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductPage {
    private static final Logger logger = LogManager.getLogger(ProductPage.class);
    WebDriver driver;
    WebDriverWait wait;

    // Web elementlerini tanımlıyoruz
    @FindBy(xpath = "//button[@class='dropdown-button__button']")
    public WebElement sortButton; // Sıralama menüsü butonu

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/div[5]/div[1]/div/div//*[normalize-space()]")
    public List<WebElement> sortingOptions; // Sıralama seçenekleri

    @FindBy(xpath = "//div[@class='product-grid']/div")
    public List<WebElement> products; // Ürün listesi

    @FindBy(xpath = "//h1[@class='product-title']")
    public WebElement productNameElement; // Ürün adı

    @FindBy(xpath = "//a[@class='color-option active']")
    public WebElement productColorElement; // Ürün rengi

    @FindBy(xpath = "//span[@class='price-in-cart']")
    public WebElement productPriceInCart; // Ürünün sepetteki fiyatı

    @FindBy(xpath = "//span[@class='price']")
    public WebElement productPrice; // Ürünün fiyatı

    @FindBy(xpath = "//button[contains(@class, 'option-size-box')]")
    public List<WebElement> sizeButtons; // Beden seçenekleri

    @FindBy(xpath = "//button[text()='SEPETE EKLE']")
    public WebElement addToCartButton; // Sepete ekle butonu

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    //Ürün listesini belirli bir kategoriye göre sıralama
    @Step("Sıralama filtresi uygulanıyor: {targetOption}")
    public void selectSortingOption(String targetOption) {
        try {
            WebElement sortButton = driver.findElement(By.xpath("//button[@class='dropdown-button__button']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", sortButton);
            sortButton.click();
            System.out.println("Sırala butonuna tıklandı.");

            List<WebElement> sortingOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[5]/div[1]/div/div//*[normalize-space()]")));

            for (WebElement option : sortingOptions) {
                String optionText = option.getText().trim();
                logger.debug("Bulunan sıralama seçeneği: '" + optionText + "'");

                if (optionText.equalsIgnoreCase(targetOption)) {
                    option.click();
                    System.out.println("Sıralama seçeneği seçildi: " + targetOption);

                    wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[contains(@class, 'updated-sorting-results')]")));
                    logger.info("Sıralama başarıyla uygulandı: " + targetOption);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Sıralama seçimi sırasında hata oluştu: " + e.getMessage());
        }
    }

    //Listedeki 4. ürünü seçme ve ürün detay sayfasına gitme
    @Step("Ürün detay sayfasına gidiliyor:")
    public void viewProductDetails() {
        try {
            if (products.size() >= 4) {
                WebElement fourthProduct = products.get(3);
                fourthProduct.click();
                closeOverlayWithJS();

                TestData.selectedProductName = productNameElement.getText().trim();
                TestData.selectedColor = productColorElement.getText().trim();

                if (driver.findElements(By.xpath("//span[@class='price-in-cart']")).size() > 0) {
                    TestData.selectedPrice = Double.parseDouble(productPriceInCart.getText().replaceAll("[^\\d.]", "").trim());
                } else {
                    TestData.selectedPrice = Double.parseDouble(productPrice.getText().replaceAll("[^\\d.]", "").trim());
                }

                logger.info("Ürün bilgileri TestData'ya kaydedildi. Ad: " + TestData.selectedProductName +
                        ", Renk: " + TestData.selectedColor + ", Fiyat: " + TestData.selectedPrice);
            } else {
                logger.warn("Ürün bulunamadı!");
            }
        } catch (Exception e) {
            logger.error("Ürün detayına gidilirken hata oluştu: " + e.getMessage());
        }
    }

    //Overlay kapatma
    public void closeOverlayWithJS() {
        try {
            logger.info("Overlay devre dışı bırakılıyor.");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("let overlay = document.querySelector('.evam-first-screen');" +
                    "if (overlay) { overlay.style.display = 'none'; }");
        } catch (Exception e) {
        }
    }

    //Sadece stokta olan bedenler arasından random seçim yapma ve sepete ekleme
    @Step("Stokta bulunan bedenlerden biri seçiliyor ve sepete ekleniyor:")
    public void selectSizeAndAddToCart() {
        closeOverlayWithJS();
        try {
            logger.info("Stokta bulunan bedenler kontrol ediliyor.");
            List<WebElement> availableSizes = new ArrayList<>();
            for (WebElement size : sizeButtons) {
                if (!size.getAttribute("class").contains("out-of-stock")) {
                    availableSizes.add(size);
                }
            }

            if (availableSizes.isEmpty()) {
                logger.warn("Uygun beden bulunamadı!");
                return;
            }

            Random random = new Random();
            WebElement selectedSize = availableSizes.get(random.nextInt(availableSizes.size()));
            wait.until(ExpectedConditions.elementToBeClickable(selectedSize));
            selectedSize.click();
            logger.info("Rastgele seçilen beden: " + selectedSize.getText());

            addToCartButton.click();
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Beden seçimi sırasında bir hata oluştu: " + e.getMessage());
        }
    }
}












