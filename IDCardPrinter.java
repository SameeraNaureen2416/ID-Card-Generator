import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;

public class IDCardPrinter {

    static final String URL = "jdbc:mysql://localhost:3306/id_card_db";
    static final String USER = "root";
    static final String PASS = "Naureen@2416"; // change

    static final int CARD_WIDTH = 638;
    static final int CARD_HEIGHT = 1011;

    public static void generate(String regNo) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USER, PASS);

        int year = 2000 + Integer.parseInt(regNo.substring(0, 2));
        String table = "students_" + year + "_gen";

        ResultSet rs = con.createStatement()
                .executeQuery("SELECT * FROM " + table +
                        " WHERE registration_no='" + regNo + "'");

        if (!rs.next()) {
            throw new Exception("Student record not found");
        }

        String name = rs.getString("name");
        String course = rs.getString("course");
        String dob = rs.getString("dob");
        byte[] photoBytes = rs.getBytes("photo");

        con.close();

        BufferedImage card = new BufferedImage(
                CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = card.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // ================= BACKGROUND =================
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        // ================= HEADER =================
        BufferedImage logo = loadImage("assets/logo");
        g.drawImage(logo, 30, 25, 110, 110, null);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("RGM \nCOLLEGE OF ENGINEERING", 160, 80);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("(AUTONOMOUS)", 300, 105);
        g.drawString("Nandyal-518 501, Andhra Pradesh", 225, 130);
        g.drawString("ph:08514-275203/275204 | Fax:08514-275123", 190, 155);

        // Red strip
        g.setColor(new Color(180, 0, 0));
        g.fillRect(0, 160, CARD_WIDTH, 45);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("STUDENT IDENTITY CARD", 170, 192);

        // ================= PHOTO =================
        BufferedImage photo =
                ImageIO.read(new ByteArrayInputStream(photoBytes));
        g.drawImage(photo, 220, 230, 200, 260, null);
        g.setColor(Color.BLACK);
        g.drawRect(220, 230, 200, 260);

        // ================= DETAILS =================
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Name : " + name, 90, 540);
        g.drawString("Reg No : " + regNo, 90, 585);
        g.drawString("Course : " + course, 90, 630);
        g.drawString("DOB : " + dob, 90, 675);

        // ================= FOOTER =================
        BufferedImage sign = loadImage("assets/principal_sign");
        g.drawImage(sign, 430, 740, 160, 60, null);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Principal", 465, 820);

        g.setColor(Color.GRAY);
        g.drawLine(0, 860, CARD_WIDTH, 860);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.BLACK);
        g.drawString("Website: www.rgmcet.edu.in", 60, 905);
        g.drawString("Email: principal@rgmcet.edu.in", 350, 905);

        g.dispose();

        new File("output").mkdirs();
        ImageIO.write(card, "png",
                new File("output/" + regNo + ".png"));
    }

    // -------- SAFE IMAGE LOADER --------
    // -------- SMART ASSET LOADER (HANDLES DOUBLE EXTENSIONS) --------
private static BufferedImage loadImage(String base) throws Exception {
    File dir = new File("assets");

    if (!dir.exists()) {
        throw new Exception("assets folder not found");
    }

    for (File f : dir.listFiles()) {
        String name = f.getName().toLowerCase();
        if (name.startsWith(new File(base).getName().toLowerCase())) {
            return ImageIO.read(f);
        }
    }

    throw new Exception("Missing asset: " + base);
}

}
