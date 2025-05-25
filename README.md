# 💰 Personal Finance Tracker – Android App (Kotlin)

A simple and intuitive mobile application that helps users track their income, expenses, and savings with category-based analysis, budget alerts, and data persistence. Built with Kotlin and Android Studio.

## 🚀 Features

### ✅ Core Features

- 🔄 Transaction Management  
  - Add, edit, and delete income/expense entries  
  - Each transaction includes: Title, Amount, Category, Date

- 📊 Category-wise Spending Analysis  
  - Categorize expenses (e.g., Food, Transport, Bills, Entertainment)  
  - View summary reports by category

- 💸 Monthly Budget Setup  
  - Set a monthly spending limit  
  - Visual indicator and warning when nearing or exceeding the limit

- 💾 Data Persistence  
  - Use SharedPreferences to save:
    - Currency preference
    - Monthly budget
    - Transaction history

---

### 🎁 Bonus Features

- 📁 Data Backup & Restore  
  - Export transactions as text or JSON file to internal storage  
  - Restore previous backups

- 🔔 Push Notifications  
  - Alerts when spending is near or over budget  
  - Optional daily reminders to log expenses

---

## 🛠️ Tech Stack

- 📱 Kotlin (Android Development)
- 🧩 SharedPreferences (Data Persistence)
- 🗃️ Internal Storage (Backup)
- 📣 Notification Manager (Alerts)
- 🖼️ XML Layouts (UI Design)

---

## ✅ How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Randimal441/PersonalFinanceTracker.git

---
🔗 Connect with Me

🔗 LinkedIn: Randimal Lamahewa

💻 GitHub: @Randimal441

## 📂 Folder Structure

```bash
.
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/yourname/financetracker/
│   │   │   │   ├── ui/
│   │   │   │   ├── model/
│   │   │   │   ├── storage/
│   │   │   │   └── utils/
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   └── build.gradle


