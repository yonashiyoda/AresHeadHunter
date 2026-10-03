<p align="center">
  <img src="https://via.placeholder.com/900x250/1a1a1a/ff4757?text=AresHeadHunter" alt="AresHeadHunter Banner" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/build-passing-success?style=for-the-badge">
  <img src="https://img.shields.io/badge/Minecraft-1.20.4%20--%201.21-blue?style=for-the-badge&logo=minecraft">
  <img src="https://img.shields.io/badge/Paper%2FSpigot-Supported-orange?style=for-the-badge">
  <img src="https://img.shields.io/badge/Java-17%2B-yellow?style=for-the-badge&logo=openjdk">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge">
</p>

<p align="center">
  <b>Türkiye Minecraft sunucuları için geliştirilmiş; gerçek skinli, nadirlik sistemli ve soul-eat buff mekanikli yeni nesil HeadHunter eklentisi.</b>
</p>

---

## ✨ Özellikler

* 🎭 **Gerçek Skin Entegrasyonu:** Öldürülen oyuncuların kafa verileri anlık olarak işlenir; kafalar üzerinde oyuncunun gerçek skini görünür.
* 🏷️ **Holografik & Dinamik Açıklama (Lore):** Kafanın üzerine bakıldığında veya incelendiğinde öldüren, ölen, ölüm sebebi ve tarih gibi detaylar şık bir formatla listelenir.
* 💎 **Nadirlik (Rarity) Sistemi:** Kill streak (seri) durumlarına ve rütbelere göre düşen kafalar *Yaygın, Nadir, Epik ve Efsanevi* olarak kategorize edilir.
* 🍖 **Ruh Sömürme (Soul Eat) Buff Mekaniği:** Kafaları envanterinizde sağ tıklayarak yiyebilir, PvP anında taktiksel üstünlük sağlayacak geçici buff'lar (Hız, Can Yenilenmesi vb.) kazanabilirsiniz.
* 📢 **Akıllı Ödül ve Duyuru Sistemi (`/bounty`):** Belalınızın başına ödül koyun. Küçük ödüller sohbet üzerinden duyurulurken, büyük bütçeli büyük ödüller tüm sunucuda dikkat çekici duyurularla yankılansın!
* ⚡ **Tamamen Asenkron Altyapı:** Veritabanı ve kafa düşme işlemleri tamamen asenkron yürütülür; sunucu performansını (TPS) asla düşürmez.

---

## 📦 Gereksinimler

Eklentinin sorunsuz çalışabilmesi için sunucunuzda aşağıdaki eklentilerin kurulu olması gerekmektedir:

| Eklenti | Açıklama |
| :--- | :--- |
| **Paper API / Spigot** | Önerilen sürüm: `1.20.4 – 1.21` |
| **Vault** | Ekonomi ve para ödülleri işlemleri için |
| **PlaceholderAPI** | Dinamik etiketler ve Placeholder desteği için |

---

## 🎮 Komutlar & Yetkiler

### 📌 Komutlar
| Komut | Açıklama | İzin |
| :--- | :--- | :--- |
| `/bounty <oyuncu> <miktar>` | Hedeflenen oyuncunun başına ödül koyar. | `aresheadhunter.bounty` |
| `/headhunter reload` | Eklenti yapılandırma dosyalarını yeniler. | `aresheadhunter.admin` |

### 🔑 Yetkiler
| Yetki | Açıklama | Varsayılan |
| :--- | :--- | :--- |
| `aresheadhunter.bounty` | Ödül sistemi komutlarını kullanır. | Herkes (`true`) |
| `aresheadhunter.admin` | Ayarları yönetir ve reload atar. | Yalnızca OP (`op`) |

---

## 📥 Kurulum

1. Releases sayfasından **AresHeadHunter.jar** dosyasını indirin.
2. Sunucunuzun `plugins/` klasörüne atın.
3. Sunucunuzu yeniden başlatın (`reload` yerine `/stop` önerilir).
4. `plugins/AresHeadHunter/config.yml` dosyasından mesajları ve buff sürelerini kendi sunucu konseptinize göre düzenleyin.

---

## 🛠️ Derleme (Compilation)

Projeyi kendi bilgisayarınızda kaynak koddan derlemek için:

```bash
mvn clean package