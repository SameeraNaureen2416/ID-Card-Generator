import java.sql.*;

public class DBService {

    static final String URL = "jdbc:mysql://localhost:3306/id_card_db";
    static final String USER = "root";
    static final String PASS = "Naureen@2416";

    public static void saveStudent(
            String name,
            String regNo,
            String course,
            String dob,
            byte[] photo
    ) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USER, PASS);

        int year = 2000 + Integer.parseInt(regNo.substring(0, 2));
        String table = "students_" + year + "_gen";

        String create =
                "CREATE TABLE IF NOT EXISTS " + table + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100)," +
                "registration_no VARCHAR(20) UNIQUE," +
                "course VARCHAR(50)," +
                "dob VARCHAR(20)," +
                "photo LONGBLOB" +
                ")";
        con.createStatement().execute(create);

        String insert =
                "INSERT INTO " + table +
                "(name, registration_no, course, dob, photo) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "name=?, course=?, dob=?, photo=?";

        PreparedStatement ps = con.prepareStatement(insert);
        ps.setString(1, name);
        ps.setString(2, regNo);
        ps.setString(3, course);
        ps.setString(4, dob);
        ps.setBytes(5, photo);

        ps.setString(6, name);
        ps.setString(7, course);
        ps.setString(8, dob);
        ps.setBytes(9, photo);

        ps.executeUpdate();
        con.close();
    }
}
