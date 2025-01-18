package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.NoSuchElementException;
public class LoginPage {
    WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LoginPage.class);


    // Web elementlerini tanımlıyoruz
    @FindBy(xpath = "//span[contains(text(),'Giriş Yap')]")
    public WebElement loginButton; // Giriş sayfasına gitmek için buton

    @FindBy(xpath = "//input[@placeholder='E-posta Adresi veya Telefon Numarası']")
    public WebElement emailField; // E-posta adresi input alanı

    @FindBy(xpath = "//button[normalize-space()='Devam Et']")
    public WebElement continueButton; // Devam et butonu

    @FindBy(xpath = "//input[@placeholder='Şifreniz']")
    public WebElement passwordField; // Şifre input alanı

    @FindBy(id = "cookieseal-banner-accept")
    public WebElement acceptButton; // Çerezleri kabul et butonu

    // PageFactory elemanlarını başlatır
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Kullanıcı giriş yapıyor: E-posta = {email}, Şifre = ****")
    public void login(String email, String password) {
        try {
            loginButton.click(); // Giriş yap butonuna tıklıyoruz
            logger.info("Giriş yap butonuna tıklandı.");

            emailField.sendKeys(email); // E-posta alanına mailimizi giriyoruz
            logger.info("E-posta alanına değer gönderildi: " + email);

            continueButton.click(); // Devam et butonuna tıklıyoruz
            logger.info("Devam et butonuna tıklandı.");

            passwordField.sendKeys(password); // Şifre alanına şifremizi giriyoruz
            logger.info("Şifre alanına değer gönderildi.");

            logger.info("Giriş bilgileri başarıyla gönderildi.");
        } catch (Exception e) {
            logger.error("Giriş işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @Step("Çerez banner'ı kapatılıyor")
    public void closeCookiesBanner() {
        try {
            logger.info("Çerez banner'ı kapatma işlemi başlatılıyor.");
            acceptButton.click(); // Çerez kapatma butonuna tıklıyoruz
        } catch (NoSuchElementException e) {
            logger.warn("Çerez banner'ı bulunamadı, zaten kapalı.");
        }
    }
}



