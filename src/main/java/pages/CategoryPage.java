package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CategoryPage {
    private static final Logger logger = LogManager.getLogger(CategoryPage.class);
    WebDriver driver;
    WebDriverWait wait;

    // Web elementlerini tanımlıyoruz
    @FindBy(xpath = "//nav[@class='menu-nav']//a")
    public List<WebElement> categories; // Ana kategoriler listesi

    @FindBy(xpath = "//ul[contains(@class, 'nav-tab__tab-list')]/button")
    public List<WebElement> subMenus; // Alt kategoriler listesi

    @FindBy(xpath = "//section[contains(@class,'content-tab')]//a")
    public List<WebElement> outfitCategories; // Ürün kategorileri listesi

    @FindBy(xpath = "//div[contains(@class,'size-filter')]/div")
    public List<WebElement> sizes; // Beden seçenekleri listesi

    @FindBy(xpath = "//div[@class='filter-option']//span[text()='6 Yaş']")
    public WebElement age6Filter; // "6 Yaş" beden filtresi

    @FindBy(xpath = "//div[@class='collapsible-filter-container__content-area collapsible-filter-container__content-area--color-filter']/div")
    public List<WebElement> colors; // Renk filtreleri listesi

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
        logger.info("CategoryPage nesnesi başarıyla başlatıldı.");
    }

    //Ana kategori listesinden kategori seçilir
    @Step("Ana Kategori seçiliyor: {targetCategory}")
    public void selectCategory(String targetCategory) {
        try {
            logger.info("Ana kategori seçiliyor: " + targetCategory);
            Actions actions = new Actions(driver);

            for (WebElement category : categories) {
                if (category.getText().trim().equalsIgnoreCase(targetCategory)) {
                    actions.moveToElement(category).perform();
                    logger.info("Ana kategori başarıyla seçildi: " + targetCategory);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Ana kategori seçimi sırasında hata oluştu: " + e.getMessage());
        }
    }

    //Bir alt kategori seçilir
    @Step("Alt Kategori seçiliyor: {subMenuName}")
    public void selectSubMenu(String subMenuName) {
        try {
            logger.info("Alt kategori seçiliyor: " + subMenuName);
            Actions actions = new Actions(driver);

            for (WebElement subMenu : subMenus) {
                if (subMenu.getText().trim().equals(subMenuName)) {
                    actions.moveToElement(subMenu).perform();
                    logger.info("Alt kategori başarıyla seçildi: " + subMenuName);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Alt kategori seçimi sırasında hata oluştu: " + e.getMessage());
        }
    }


    //Ürün kategorisi seçilir
    @Step("Ürün kategorisi seçiliyor: {outfitCategory}")
    public void selectOutFit(String outfitCategory) {
        try {
            logger.info("Ürün kategorisi seçiliyor: " + outfitCategory);

            for (WebElement outfit : outfitCategories) {
                if (outfit.getText().trim().equals(outfitCategory)) {
                    outfit.click();
                    logger.info("Ürün kategorisi başarıyla seçildi: " + outfitCategory); // Log: İşlem tamamlandı
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Ürün kategorisi seçimi sırasında hata oluştu: " + e.getMessage()); // Log: Hata
        }
    }

    //Beden seçimi yapılır
    @Step("Beden filtreleri seçiliyor: {targetSizes}")
    public void selectSizeFilter(List<String> targetSizes) {
        try {
            logger.info("Beden filtreleme işlemi başlatılıyor. Hedef bedenler: " + targetSizes); // Log: İşlem başlıyor

            for (String targetSize : targetSizes) {
                boolean isSizeSelected = false;

                for (WebElement size : sizes) {
                    String sizeText = size.getText().replaceAll("\\s+", " ").replaceAll("\\(.*?\\)", "").trim();
                    logger.debug("Bulunan beden: '" + sizeText + "'"); // Log: Bulunan beden

                    if (sizeText.equalsIgnoreCase(targetSize)) {
                        if (targetSize.equalsIgnoreCase("6 Yaş")) {
                            clickAge6Filter();
                        } else {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", size);
                            wait.until(ExpectedConditions.elementToBeClickable(size)).click();
                        }
                        wait.until(ExpectedConditions.attributeContains(size, "class", "filter-option--active"));
                        logger.info("Beden filtresi başarıyla uygulandı: " + sizeText); // Log: İşlem tamamlandı
                        isSizeSelected = true;
                        break;
                    }
                }

                if (!isSizeSelected) {
                    logger.warn("Beden bulunamadı veya seçilemedi: " + targetSize); // Log: Beden bulunamadı
                }
            }

            logger.info("Beden filtreleme işlemi tamamlandı."); // Log: İşlem tamamlandı
        } catch (Exception e) {
            logger.error("Beden filtresi sırasında hata oluştu: " + e.getMessage()); // Log: Hata
        }
    }

    //6 yaş liste ile çekilemediğinden özel method oluşturulur ve seçilir
    @Step("Beden filtreleri seçiliyor: 6 Yaş")
    public void clickAge6Filter() {
        try {
            logger.info("6 Yaş filtresi seçiliyor."); // Log: İşlem başlıyor
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", age6Filter);
            logger.info("6 Yaş filtresi başarıyla seçildi."); // Log: İşlem tamamlandı
        } catch (Exception e) {
            logger.error("6 Yaş filtresi seçimi sırasında hata oluştu: " + e.getMessage()); // Log: Hata
        }
    }

    //Renk seçimi yapılır
    @Step("Renk filtresi seçiliyor: {colorName}")
    public void selectColor(String colorName) {
        try {
            logger.info("Renk filtresi seçiliyor: " + colorName); // Log: İşlem başlıyor

            for (WebElement color : colors) {
                String colorText = color.getText().replaceAll("\\s+", " ").replaceAll("\\(.*?\\)", "").trim();
                logger.debug("Bulunan renk: '" + colorText + "'"); // Log: Bulunan renk

                if (colorText.equalsIgnoreCase(colorName)) {
                    color.click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'updated-filter-results')]")));
                    logger.info("Renk filtresi başarıyla uygulandı: " + colorName); // Log: İşlem tamamlandı
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Renk filtresi seçimi sırasında hata oluştu: " + e.getMessage()); // Log: Hata
        }
    }
}






