/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.ContaModel;
import MODEL.DependenteModel;
import VIEW.AlterarTitularView;
import com.mysql.jdbc.MysqlDataTruncation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AlterarTitularDAO extends ConnectionDAO {

    public boolean concluir(ContaModel contaM, DependenteModel dependM) {
        
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "call alterarTitular_sp(?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            ps.setString(2, dependM.getNome());
            System.err.println(ps);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir obito!");
            }
        }
    }
    
    public boolean buscarPessoas(ContaModel contaM, AlterarTitularView altV){
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaPessoas_sp(-1,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            System.err.println(ps);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                altV.txtCodConta.setText(rs.getString("codigo"));
                altV.cmbDepend.addItem(rs.getString("nome"));
            }
             return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        
    }
    
}
