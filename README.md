# 🪪 RGM College ID Card Generator

A desktop application built in **Java** that digitally replicates the official **RGM College student ID card** — matching the exact shape, size, layout, and design of the physical card issued by the college.

---

## 📌 About the Project

This project was developed as a team effort to automate the generation of student ID cards for **RGM College of Engineering & Technology**. The generated ID card is a pixel-perfect digital replica of the original college ID — same dimensions, same layout, same fonts, and same structure.

Instead of manually designing ID cards, this application fetches student data from a database, populates the card template, attaches the student's photo, and allows printing — all from a single desktop interface.

---

## ✨ Features

- 🖼️ **Student Photo** — uploads and embeds student photo directly on the ID card
- 🗄️ **Database Connected** — fetches student details (name, roll number, branch, year, etc.) from MySQL database
- 🖨️ **Print Functionality** — directly print the generated ID card from the application
- 📐 **Exact Replica** — matches the real RGM College ID card in size, shape, color, and layout
- 📅 **Auto Date Handling** — uses JCalendar for date of birth and validity fields

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java (Swing/AWT) | Frontend GUI & ID card rendering |
| MySQL | Student data storage |
| JDBC (mysql-connector-j-9.6.0) | Database connectivity |
| JCalendar (jcalendar-1.4) | Date picker component |
| Java Print API | Print functionality |

---

## 📁 Project Structure

```
ID Card/
│
├── StdID.java              # Main application & ID card UI
├── IDCardPrinter.java      # Handles print functionality
├── DBService.java          # Database connection & queries
├── SequenceUtil.java       # Utility for ID sequencing
├── assets/                 # Images, logos, and card assets
├── output/                 # Generated ID card outputs
└── .github/                # GitHub configuration
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java JDK 8 or above
- MySQL Server
- Any Java IDE (VS Code / IntelliJ / Eclipse)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/SameeraNaureen2416/ID-Card-Generator.git
cd ID-Card-Generator
```

**2. Set up the database**
- Create a MySQL database
- Update DB credentials in `DBService.java`:
```java
String url = "jdbc:mysql://localhost:3306/your_database";
String user = "your_username";
String password = "your_password";
```

**3. Add dependencies to classpath**
```bash
javac -cp ".;jcalendar-1.4.jar;mysql-connector-j-9.6.0.jar" *.java
```

**4. Run the application**
```bash
java -cp ".;jcalendar-1.4.jar;mysql-connector-j-9.6.0.jar" StdID
```

---

## 👥 Team

Developed by students of **RGM College of Engineering & Technology** as a team project.

---

## 📄 License

This project is for educational purposes only. The ID card design belongs to RGM College of Engineering & Technology.
