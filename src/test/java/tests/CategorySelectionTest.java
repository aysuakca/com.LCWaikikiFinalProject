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
import pages.CategoryPage;
import utils.Driver;
import utils.TestData;

import java.util.Arrays;
import java.util.List;

@Epic("LC Waikiki E-Ticaret Uygulaması") // Test senaryosunun bağlı olduğu ana modül
@Feature("Kategori ve Ürün Seçimi") // Testin odaklandığı özellik
public class CategorySelectionTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(CategorySelectionTest.class); // Loglama için Log4j Logger nesnesi
    CategoryPage categoryPage;

    @BeforeClass
    public void setupPages() {
        // PageFactory ile CategoryPage nesnesini başlatıyoruz
        categoryPage = PageFactory.initElements(Driver.getDriver(), CategoryPage.class);
        logger.info("CategoryPage nesnesi başarıyla başlatıldı.");
    }

    @Test(priority = 2, dependsOnMethods = "tests.LoginTest.loginTest")
    @Description("Ana kategoriyi seçer.") // Testin ne yaptığını açıklayan bir not
    public void selectCategory() {
        logger.info("Ana kategori seçimi başlatılıyor.");
        performCategorySelection("ÇOCUK & BEBEK");
        logger.info("Ana kategori seçimi tamamlandı.");
    }

    @Step("Kategori seçimi yapılıyor: {categoryName}")
    public void performCategorySelection(String categoryName) {
        logger.info("Kategori seçiliyor: " + categoryName);
        categoryPage.selectCategory(categoryName);
        logger.info("Kategori seçimi tamamlandı: " + categoryName);
    }

    @Test(priority = 3, dependsOnMethods = "selectCategory")
    @Description("Alt kategoriyi seçer.")
    public void selectSubMenu() {
        logger.info("Alt kategori seçimi başlatılıyor.");
        performSubMenuSelection("KIZ ÇOCUK (6-14 YAŞ)");
        logger.info("Alt kategori seçimi tamamlandı.");
    }

    @Step("Alt kategori seçimi yapılıyor: {subMenuName}")
    public void performSubMenuSelection(String subMenuName) {
        logger.info("Alt kategori seçiliyor: " + subMenuName);
        categoryPage.selectSubMenu(subMenuName);
        logger.info("Alt kategori seçimi tamamlandı: " + subMenuName);
    }

    @Test(priority = 4, dependsOnMethods = "selectSubMenu")
    @Description("Ürün kategorisini seçer.")
    public void selectOutFit() {
        logger.info("Ürün kategorisi seçimi başlatılıyor.");
        performOutfitSelection("Mont ve Kaban");
        logger.info("Ürün kategorisi seçimi tamamlandı.");
    }

    @Step("Ürün kategorisi seçimi yapılıyor: {outfitCategory}")
    public void performOutfitSelection(String outfitCategory) {
        logger.info("Ürün kategorisi seçiliyor: " + outfitCategory);
        categoryPage.selectOutFit(outfitCategory);
        logger.info("Ürün kategorisi seçimi tamamlandı: " + outfitCategory);
    }

    @Test(priority = 5, dependsOnMethods = "selectOutFit")
    @Description("Ürün için beden seçer.")
    public void selectSizes() {
        logger.info("Beden seçimi işlemi başlatılıyor.");
        List<String> targetSizes = Arrays.asList("5-6 Yaş", "6 Yaş", "6-7 Yaş");
        performSizeSelection(targetSizes);
        logger.info("Beden seçimi işlemi tamamlandı.");
    }

    @Step("Beden seçimi yapılıyor: {targetSizes}")
    public void performSizeSelection(List<String> targetSizes) {
        logger.info("Seçilecek bedenler: " + targetSizes);
        categoryPage.selectSizeFilter(targetSizes);
        logger.info("Beden seçimi tamamlandı: " + targetSizes);
    }

    @Test(priority = 6, dependsOnMethods = "selectOutFit")
    @Description("Ürün için renk seçer.")
    public void selectColor() {
        logger.info("Renk seçimi işlemi başlatılıyor.");
        performColorSelection("BEJ");
        logger.info("Renk seçimi işlemi tamamlandı.");
    }

    @Step("Renk seçimi yapılıyor: {colorName}")
    public void performColorSelection(String colorName) {
        logger.info("Seçilen renk: " + colorName);
        categoryPage.selectColor(colorName);
        TestData.selectedColor = colorName;
        logger.info("Renk seçimi tamamlandı: " + colorName);
    }
}
