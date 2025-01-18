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
    }

    //Ana kategori listesinden kategori seçilir
    @Step("Ana Kategori seçiliyor: {targetCategory}")
    public void selectCategory(String targetCategory) {
        try {
            Actions actions = new Actions(driver);

            for (WebElement category : categories) {
                if (category.getText().trim().equalsIgnoreCase(targetCategory)) {
                    actions.moveToElement(category).perform();
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    //Bir alt kategori seçilir
    @Step("Alt Kategori seçiliyor: {subMenuName}")
    public void selectSubMenu(String subMenuName) {
        try {
            Actions actions = new Actions(driver);

            for (WebElement subMenu : subMenus) {
                if (subMenu.getText().trim().equals(subMenuName)) {
                    actions.moveToElement(subMenu).perform();
                    break;
                }
            }
        } catch (Exception e) {
        }
    }


    //Ürün kategorisi seçilir
    @Step("Ürün kategorisi seçiliyor: {outfitCategory}")
    public void selectOutFit(String outfitCategory) {
        try {
            for (WebElement outfit : outfitCategories) {
                if (outfit.getText().trim().equals(outfitCategory)) {
                    outfit.click();
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    //Beden seçimi yapılır
    @Step("Beden filtreleri seçiliyor: {targetSizes}")
    public void selectSizeFilter(List<String> targetSizes) {
        try {
            List<WebElement> sizes = driver.findElements(By.xpath("//div[contains(@class,'size-filter')]/div"));

            for (String targetSize : targetSizes) {
                boolean isSizeSelected = false;

                for (WebElement size : sizes) {
                    String sizeText = size.getText().replaceAll("\\s+", " ").replaceAll("\\(.*?\\)", "").trim();

                    System.out.println("Bulunan beden: '" + sizeText + "'");

                    if (sizeText.equalsIgnoreCase(targetSize)) {
                        try {
                            if (targetSize.equalsIgnoreCase("6 Yaş")) {
                                clickAge6Filter();
                            } else {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", size);
                                wait.until(ExpectedConditions.elementToBeClickable(size));
                                size.click();
                            }
                            wait.until(ExpectedConditions.attributeContains(size, "class", "filter-option--active"));
                            System.out.println("Beden filtresi başarıyla uygulandı: " + sizeText);
                            isSizeSelected = true;
                            break;

                        } catch (Exception e) {
                            System.err.println("Beden seçimi sırasında hata oluştu: " + e.getMessage());
                        }
                    }
                }

                if (!isSizeSelected) {
                    System.out.println("Beden bulunamadı veya seçilemedi: " + targetSize);
                }
            }

            System.out.println("Beden seçim işlemi tamamlandı.");

        } catch (Exception e) {
            System.err.println("Beden filtresi sırasında genel bir hata oluştu: " + e.getMessage());
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






