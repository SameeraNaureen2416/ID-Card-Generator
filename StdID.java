import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

// Age validation imports
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Period;

public class StdID extends JFrame {

    JTextField name, regno, course;
    JDateChooser dob;
    JLabel photoLabel;
    byte[] photoBytes;

    Font labelFont = new Font("Arial", Font.BOLD, 18);
    Font fieldFont = new Font("Arial", Font.PLAIN, 18);

    // 30mm x 22mm @ ~300 DPI
    static final int PHOTO_WIDTH = 260;
    static final int PHOTO_HEIGHT = 354;

    public StdID() {

        setTitle("RGM College ID Card Application Form");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(makeLabel("Student Name"));
        name = makeTextField();
        panel.add(name);

        panel.add(makeLabel("Registration Number"));
        regno = makeTextField();
        panel.add(regno);

        panel.add(makeLabel("Course"));
        course = makeTextField();
        panel.add(course);

        panel.add(makeLabel("Date of Birth"));
        dob = new JDateChooser();
        dob.setDateFormatString("dd-MM-yyyy");
        dob.setFont(fieldFont);
        panel.add(dob);

        panel.add(makeLabel("Photo (30mm × 22mm)"));

        JPanel photoPanel = new JPanel(new BorderLayout());
        photoLabel = new JLabel("No Photo", JLabel.CENTER);
        photoLabel.setPreferredSize(new Dimension(130, 177));
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton uploadBtn = new JButton("Upload Photo");
        uploadBtn.setFont(fieldFont);
        uploadBtn.addActionListener(e -> uploadPhoto());

        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(uploadBtn, BorderLayout.SOUTH);
        panel.add(photoPanel);

        JButton submit = new JButton("Register");
        submit.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(new JLabel());
        panel.add(submit);

        submit.addActionListener(e -> submitForm());

        add(panel);
        setVisible(true);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(labelFont);
        return l;
    }

    private JTextField makeTextField() {
        JTextField t = new JTextField();
        t.setFont(fieldFont);
        return t;
    }

    // ---------- PHOTO UPLOAD & RESIZE ----------
    private void uploadPhoto() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                BufferedImage original = ImageIO.read(file);

                BufferedImage resized =
                        resizeImage(original, PHOTO_WIDTH, PHOTO_HEIGHT);

                photoLabel.setIcon(new ImageIcon(
                        resized.getScaledInstance(
                                photoLabel.getWidth(),
                                photoLabel.getHeight(),
                                Image.SCALE_SMOOTH)));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resized, "jpg", baos);
                photoBytes = baos.toByteArray();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid image file");
            }
        }
    }

    private BufferedImage resizeImage(BufferedImage src, int w, int h) {
        BufferedImage img =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return img;
    }

    // ---------- SUBMIT FORM WITH AGE VALIDATION ----------
    private void submitForm() {
        try {
            String sname = name.getText().trim();
            String rno = regno.getText().trim();
            String crs = course.getText().trim();

            if (sname.isEmpty() || rno.isEmpty() || crs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            if (dob.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select Date of Birth");
                return;
            }

            // ---- AGE > 16 CHECK ----
            LocalDate birthDate = dob.getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            int age = Period.between(birthDate, LocalDate.now()).getYears();

            if (age <= 16) {
                JOptionPane.showMessageDialog(this,
                        "Age must be greater than 16 years");
                return;
            }

            String birth = new SimpleDateFormat("dd-MM-yyyy")
                    .format(dob.getDate());

            if (photoBytes == null) {
                JOptionPane.showMessageDialog(this, "Please upload photo");
                return;
            }

            // Save to DB
            DBService.saveStudent(
                    sname, rno, crs, birth, photoBytes
            );

            // Generate ID Card
            IDCardPrinter.generate(rno);

            JOptionPane.showMessageDialog(this,
                    "Registration successful!\nID Card generated.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        new StdID();
    }
}
