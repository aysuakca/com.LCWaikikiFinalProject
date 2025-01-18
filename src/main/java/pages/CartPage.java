package pages;

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

public class CartPage {
    private static final Logger logger = LogManager.getLogger(CartPage.class);
    WebDriver driver;
    WebDriverWait wait;

    // Web elementlerini tanımlıyoruz
    @FindBy(xpath = "//a[contains(@href, 'sepetim')]")
    public WebElement cartButton; // Sepetim butonu

    @FindBy(xpath = "//input[@value='1']")
    public WebElement quantityInput; // Sepette ürün miktarını değiştirmek için input alanı

    @FindBy(xpath = "//p[@class='red last-items']")
    public List<WebElement> stockWarningElements; // Sepetteki kritik stok uyarı bildirimi

    @FindBy(xpath = "//button[normalize-space()='Tamam']")
    public WebElement confirmButton; // Stok limiti onaylama butonu

    @FindBy(xpath = "//span[@class='rd-cart-item-code']")
    public WebElement cartProductName; // Sepetteki ürün adı

    @FindBy(xpath = "//span[@class='rd-cart-item-color']")
    public WebElement cartProductColor; // Sepetteki ürün rengi

    @FindBy(xpath = "//input[contains(@class, 'item-quantity-input')]")
    public WebElement cartProductQuantity; // Sepetteki ürün miktarı

    @FindBy(xpath = "//span[@class='rd-cart-item-price mb-15']")
    public WebElement cartProductPrice; // Sepetteki ürün fiyatı

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
        logger.info("CartPage nesnesi başarıyla başlatıldı.");
    }

    //Sepetteki ürün miktarı kritik stok durumuna bakılarak artılır(kritik stok uyarısı yok ise maksimum değerde uyarı verilir)
    public int increaseProductQuantity(int desiredQuantity) {
        logger.info("Ürün miktarı artırılıyor. Hedef miktar: " + desiredQuantity);
        navigateToCart();

        int finalQuantity = desiredQuantity;

        try {
            int maxStock = getMaxStock(); // Maksimum stok miktarını al
            if (desiredQuantity > maxStock) {
                logger.warn("Hedef miktar maksimum stoğu aşıyor. Maksimum stok: " + maxStock);
                finalQuantity = maxStock; // Ürün miktarını maksimum stoğa ayarla
            }

            wait.until(ExpectedConditions.elementToBeClickable(quantityInput)).clear();
            quantityInput.sendKeys(String.valueOf(finalQuantity)); // Yeni miktarı gir
            logger.info("Ürün miktarı güncellendi: " + finalQuantity);

            handleStockLimitPopup(); // Stok limiti uyarısını kontrol et

        } catch (StaleElementReferenceException e) {
            logger.error("Hata: Element geçersiz oldu. Yeniden denenecek: " + e.getMessage());
            wait.until(ExpectedConditions.elementToBeClickable(quantityInput)).clear();
            quantityInput.sendKeys(String.valueOf(finalQuantity));
        } catch (Exception e) {
            logger.error("Adet artırma sırasında hata oluştu: " + e.getMessage());
        }

        return finalQuantity;
    }

    //Sepetim ekranına gidilir
    private void navigateToCart() {
        try {
            logger.info("Sepetim ekranına gidiliyor.");
            wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
            logger.info("Sepetim ekranına başarıyla gidildi.");
        } catch (Exception e) {
            logger.error("Sepetim ekranına gidilirken hata oluştu: " + e.getMessage());
        }
    }

    //
    private int getMaxStock() {
        try {
            if (!stockWarningElements.isEmpty()) {
                String stockText = stockWarningElements.get(0).getText(); // Stok bilgisini al
                int maxStock = Integer.parseInt(stockText.replaceAll("[^0-9]", "")); // Boşlukları ayıkla
                logger.info("Maksimum stok bilgisi alındı: " + maxStock);
                return maxStock;
            }
        } catch (Exception e) {
            logger.error("Kritik stok bilgisi alınamadı: " + e.getMessage());
        }
        return Integer.MAX_VALUE; // Stok bilgisi uyarısı gelmiyorsa maksimum değer döner
    }

    private void handleStockLimitPopup() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click(); // Stok limiti uyarisi onayı
            logger.warn("Stok limiti uyarısı çıktı ve onaylandı.");
        } catch (Exception e) {
            logger.info("Stok limiti pop-up'ı bulunamadı.");
        }
    }

    public void verifyCartDetails(String expectedProductName, String expectedColor, int expectedQuantity) {
        logger.info("Sepet detayları doğrulanıyor.");
        try {
            // Ürünün sepetteki bilgilerini alıyoruz
            String actualCartName = wait.until(ExpectedConditions.visibilityOf(cartProductName)).getText().trim();
            String actualCartColor = wait.until(ExpectedConditions.visibilityOf(cartProductColor)).getText().trim();
            int actualCartQuantity = Integer.parseInt(wait.until(ExpectedConditions.visibilityOf(cartProductQuantity)).getAttribute("value").trim());
            double actualCartPrice = Double.parseDouble(wait.until(ExpectedConditions.visibilityOf(cartProductPrice)).getText().replaceAll("[^\\d.]", "").trim());

            // Ürünün detay sayfasındaki adı ile sepetteki adını doğrulama
            if (!expectedProductName.toLowerCase().contains(actualCartName.toLowerCase())) {
                logger.error("Sepetteki ürün adı uyuşmuyor. Beklenen: " + expectedProductName + ", Bulunan: " + actualCartName);
            } else {
                logger.info("Ürün adı doğrulandı: " + actualCartName);
            }

            // Ürünün detay sayfasında seçilen rengi ile sepetteki rengini doğrulama
            if (actualCartColor.toLowerCase().startsWith("renk: ")) {
                actualCartColor = actualCartColor.substring(6).trim();
            }
            if (!actualCartColor.equalsIgnoreCase(expectedColor)) {
                logger.error("Sepetteki ürün rengi uyuşmuyor. Beklenen: " + expectedColor + ", Bulunan: " + actualCartColor);
            } else {
                logger.info("Ürün rengi doğrulandı: " + actualCartColor);
            }

            // Girilen ürün miktarı ile sepetteki ürün miktarını doğrulama
            if (actualCartQuantity != expectedQuantity) {
                logger.error("Sepetteki ürün miktarı uyuşmuyor. Beklenen: " + expectedQuantity + ", Bulunan: " + actualCartQuantity);
            } else {
                logger.info("Ürün miktarı doğrulandı: " + actualCartQuantity);
            }

            // Ürünün detay sayfasındaki fiyatı ile sepetteki fiyatını doğrulama
            if (actualCartPrice != TestData.selectedPrice) {
                logger.error("Sepetteki ürün fiyatı uyuşmuyor. Beklenen: " + TestData.selectedPrice + ", Bulunan: " + actualCartPrice);
            } else {
                logger.info("Ürün fiyatı doğrulandı: " + actualCartPrice);
            }

            logger.info("Sepet detayları doğrulama işlemi tamamlandı.");

        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("Sepet detayları doğrulanırken hata oluştu: " + e.getMessage());
        }
    }
}














