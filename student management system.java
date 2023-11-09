import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Proj {
    Connection con;
    Statement stm;
    PreparedStatement insertStatement;
    Scanner scan;

    public Proj() {
        scan = new Scanner(System.in);

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String url = "jdbc:ucanaccess://C:/AishuJava/pytek.accdb";
            String user = "";
            String password = "";
            con = DriverManager.getConnection(url, user, password);
            insertStatement = con.prepareStatement(
                    "INSERT INTO register (f_name, l_name, gender, dob, address, number) VALUES (?, ?, ?, ?, ?, ?)");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String f_name, String l_name, String gender, String dob, String address, String number) {
        try {
            insertStatement.setString(1, f_name);
            insertStatement.setString(2, l_name);
            insertStatement.setString(3, gender);
            insertStatement.setString(4, dob);
            insertStatement.setString(5, address);
            insertStatement.setString(6, number);
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Record inserted.");
            } else {
                System.out.println("Record not inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int recordId, String newFirstName, String newLastName) {
        try {
            String updateSQL = "UPDATE register SET f_name = ?, l_name = ? WHERE id = ?";
            PreparedStatement updateStatement = con.prepareStatement(updateSQL);
            updateStatement.setString(1, newFirstName);
            updateStatement.setString(2, newLastName);
            updateStatement.setInt(3, recordId);

            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record found to update.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search(String searchCriteria) {
        try {
            String searchSQL = "SELECT * FROM register WHERE f_name LIKE ?";
            PreparedStatement searchStatement = con.prepareStatement(searchSQL);
            searchStatement.setString(1, "%" + searchCriteria + "%");

            ResultSet resultSet = searchStatement.executeQuery();

            while (resultSet.next()) {
                String f_name = resultSet.getString("f_name");
                String l_name = resultSet.getString("l_name");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("dob");
                String address = resultSet.getString("address");
                String number = resultSet.getString("number");

                System.out.println("First Name: " + f_name);
                System.out.println("Last Name: " + l_name);
                System.out.println("Gender: " + gender);
                System.out.println("Date of Birth: " + dob);
                System.out.println("Address: " + address);
                System.out.println("Number: " + number);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void view() {
        try {
            String sql = "SELECT * FROM register";
            ResultSet resultSet = stm.executeQuery(sql);

            while (resultSet.next()) {
                String f_name = resultSet.getString("f_name");
                String l_name = resultSet.getString("l_name");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("dob");
                String address = resultSet.getString("address");
                String number = resultSet.getString("number");

                System.out.println("First Name: " + f_name);
                System.out.println("Last Name: " + l_name);
                System.out.println("Gender: " + gender);
                System.out.println("Date of Birth: " + dob);
                System.out.println("Address: " + address);
                System.out.println("Number: " + number);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeResources() {
        try {
            if (insertStatement != null)
                insertStatement.close();
            if (con != null)
                con.close();
            if (scan != null)
                scan.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) {
            int opt;
            boolean b = true;
            Proj t = new Proj();
            while (b) {
                System.out.println("\n\n1. Add Record\n2. Update Record\n3. Search Records\n4. View Records\n5. Exit");
                opt = scan.nextInt();
                switch (opt) {
                    case 1:
                        System.out.print("Enter First Name: ");
                        String f_name = scan.next();
                        System.out.print("Enter Last Name: ");
                        String l_name = scan.next();
                        System.out.print("Enter Gender: ");
                        String gender = scan.next();
                        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                        String dob = scan.next();
                        System.out.print("Enter Address: ");
                        String address = scan.next();
                        System.out.print("Enter Number: ");
                        String number = scan.next();
                        t.insert(f_name, l_name, gender, dob, address, number);
                        break;
                    case 2:
                        System.out.print("Enter ID of the record to update: ");
                        int recordId = scan.nextInt();
                        System.out.print("Enter New First Name: ");
                        String newFirstName = scan.next();
                        System.out.print("Enter New Last Name: ");
                        String newLastName = scan.next();
                        t.update(recordId, newFirstName, newLastName);
                        break;
                    case 3:
                        System.out.print("Enter search criteria: ");
                        String searchCriteria = scan.next();
                        t.search(searchCriteria);
                        break;
                    case 4:
                        t.view();
                        break;
                    case 5:
                        t.closeResources();
                        b = false;
                        break;
                }
            }
        }
    }
}
