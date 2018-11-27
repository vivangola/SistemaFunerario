package DAO;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConnectionDAO {

    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/bd_funeraria";
    private final String USER = "root";
    private final String PASS = "";
    private Connection con = null;

    public Connection getConnection(){
        try {
           Class.forName(DRIVER);
           con = (Connection) DriverManager.getConnection(this.URL, this.USER, this.PASS);
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Por favor ligue o XAMPP!");
            System.exit(0);
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Por favor ligue o XAMPP!");
            System.exit(0);
        }
        return con;
    }

}
