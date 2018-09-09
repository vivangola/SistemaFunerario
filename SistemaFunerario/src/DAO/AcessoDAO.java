/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AcessoDAO extends ConnectionDAO {

    public boolean incluir(AcessoModel acessoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "INSERT INTO acesso (login, senha, tipo, ativo) VALUES(?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            ps.setString(2, acessoM.getSenha());
            ps.setInt(3, acessoM.getTipo());
            ps.setInt(4, acessoM.getAtivo());
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Este usuário já existe, por favor tente novamente!");
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir acesso!");
            }
        }
    }

    public boolean alterar(AcessoModel acessoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE acesso SET login=?, senha=?, tipo=?, ativo=? WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            ps.setString(2, acessoM.getSenha());
            ps.setInt(3, acessoM.getTipo());
            ps.setInt(4, acessoM.getAtivo());
            ps.setString(5, acessoM.getLogin());
            ps.execute();
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

    public boolean excluir(AcessoModel acessoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            ps.execute();
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

    public boolean buscar(AcessoModel acessoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT * FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            rs = ps.executeQuery();

            if (rs.next()) {
                acessoM.setLogin(rs.getString("login"));
                acessoM.setSenha(rs.getString("senha"));
                acessoM.setTipo(Integer.parseInt(rs.getString("tipo")));
                return true;
            }
            return false;
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
