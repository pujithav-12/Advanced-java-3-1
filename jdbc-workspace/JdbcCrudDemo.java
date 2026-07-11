import java.sql.*;
import java.util.Scanner;

public class JdbcCrudDemo {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish Connection to the 'companydb' database
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/companydb",
                    "javauser",
                    "StrongPass@123!");
                    
            Statement stmt = con.createStatement();
            int choice;
            
            do {
                System.out.println("\n========== EMPLOYEE MANAGEMENT SYSTEM ==========");
                System.out.println("1. Create Table");
                System.out.println("2. Insert Employee");
                System.out.println("3. Display Employees");
                System.out.println("4. Update Employee Role");
                System.out.println("5. Delete Employee");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                
                choice = sc.nextInt();
                sc.nextLine(); // Consume the newline character
                
                switch (choice) {
                    case 1:
                        String createTable = "CREATE TABLE IF NOT EXISTS employees("
                                + "id INT PRIMARY KEY,"
                                + "name VARCHAR(50),"
                                + "role VARCHAR(50))";
                        stmt.executeUpdate(createTable);
                        System.out.println("Table Created Successfully!");
                        break;
                        
                    case 2:
                        System.out.print("Enter ID: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // Consume newline
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Role: ");
                        String role = sc.nextLine();
                        
                        // Using PreparedStatement for secure data entry
                        String insert = "INSERT INTO employees (id, name, role) VALUES (?, ?, ?)";
                        try (PreparedStatement pstmt = con.prepareStatement(insert)) {
                            pstmt.setInt(1, id);
                            pstmt.setString(2, name);
                            pstmt.setString(3, role);
                            pstmt.executeUpdate();
                            System.out.println("Record Inserted Successfully!");
                        }
                        break;
                        
                    case 3:
                        ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
                        System.out.println("\nID\tNAME\t\tROLE");
                        System.out.println("---------------------------------------");
                        while (rs.next()) {
                            System.out.println(
                                    rs.getInt("id") + "\t"
                                            + rs.getString("name") + "\t\t"
                                            + rs.getString("role"));
                        }
                        rs.close();
                        break;
                        
                    case 4:
                        System.out.print("Enter Employee ID to Update: ");
                        int uid = sc.nextInt();
                        sc.nextLine(); // Consume newline
                        System.out.print("Enter New Role: ");
                        String newRole = sc.nextLine();
                        
                        String update = "UPDATE employees SET role = ? WHERE id = ?";
                        try (PreparedStatement pstmt = con.prepareStatement(update)) {
                            pstmt.setString(1, newRole);
                            pstmt.setInt(2, uid);
                            int rows = pstmt.executeUpdate();
                            if (rows > 0)
                                System.out.println("Record Updated Successfully!");
                            else
                                System.out.println("Employee Not Found!");
                        }
                        break;
                        
                    case 5:
                        System.out.print("Enter Employee ID to Delete: ");
                        int did = sc.nextInt();
                        
                        String delete = "DELETE FROM employees WHERE id = ?";
                        try (PreparedStatement pstmt = con.prepareStatement(delete)) {
                            pstmt.setInt(1, did);
                            int d = pstmt.executeUpdate();
                            if (d > 0)
                                System.out.println("Record Deleted Successfully!");
                            else
                                System.out.println("Employee Not Found!");
                        }
                        break;
                        
                    case 6:
                        System.out.println("Thank You!");
                        break;
                        
                    default:
                        System.out.println("Invalid Choice!");
                }
            } while (choice != 6);
            
            con.close();
            sc.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
