# LC Waikiki Final Project

## Genel Bakış
Bu proje, LC Waikiki e-ticaret platformu için geliştirilmiş kapsamlı bir test otomasyon sistemi sunmaktadır. Sistem, giriş yapma, kategori gezinme, ürün seçimi, sepet işlemleri ve doğrulama gibi temel işlevleri otomatikleştirmek için tasarlanmıştır.

## Teknolojiler ve Araçlar
- **Programlama Dili**: Java
- **Proje Yönetim Aracı**: Maven
- **Test Framework**: TestNG
- **Web Test Otomasyonu**: Selenium WebDriver
- **Raporlama**: Allure Reports, Log4j

## Test Senaryoları
1. **Giriş Testi**

- LC Waikiki ana sayfasına gidilir.
- Geçerli bir e-posta ve şifre girilir.
  
2. **Kategori Seçim Testi**

- "Çocuk & Bebek" kategorisini seçilir.
- "Kız Çocuk (6-14 yaş)" sekmesine gidilir ve "Mont ve Kaban" seçeneği seçilir.
- Ürünler, beden ve renk ile filtrelenir.

3. **Ürün Testi**

- Ürünler, "En çok satanlar" olacak şekilde sıralanır.
- Stokta olan bedenlerden rastgele bir beden seçilir.
- Ürün sepete eklenir.

4. **Sepet Testi**

- Sepetim sayfasina gidilir.
- Ürün miktarı stok durumuna göre artırılır. 
- Sepetteki ürünün detayları doğrulanır.

5. **Favoriler Testi**

- Favorilerim ekranına gidilir.
- Favorilere eklenen ürünün listede olduğu kontrol edilir.	

Test detaylarını görmek için [buraya tıklayın](.LCWTestCase.pdf).


## Allure Report Sonuçları
![Allure Raporu](screenshots/allureReport.png)
![Allure Raporu2](screenshots/allureReports2.png)

## Proje Ekran Görüntüleri

### 1. Login Testi
![Login Testi](screenshots/loginTest.png)

### 2. Kategori Seçimi Testi
![Kategori Seçimi](screenshots/category-selection.png)

### 3. Filtre Seçimi Testi
![Kategori Seçimi](screenshots/productActions.png)

### 4. Sıralama Testi
![Kategori Seçimi](screenshots/productActionsSorting.png)

### 5. Ürün Seçim Testi
![Kategori Seçimi](screenshots/productActionsSelectSize.png)

### 6. Sepete Ekleme ve Doğrulama Testi
![Kategori Seçimi](screenshots/cartVerification.png)

### 7. Favorilere Ekleme Testi
![Kategori Seçimi](screenshots/addFavorites.png)

### 8. Favoriler Doğrulama Testi
![Kategori Seçimi](screenshots/favoritesPage.png)

### Author
- **Derya Aysu Kılıç**



