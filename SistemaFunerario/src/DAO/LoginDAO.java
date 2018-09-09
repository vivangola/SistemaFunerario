/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.ConnectionDAO;
import MODEL.LoginModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author junio
 */
public class LoginDAO extends ConnectionDAO {

    public boolean login(LoginModel loginM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        if (!validaUsuario(loginM)) {
            return false;
        }

        if (!validaStatus(loginM)) {
            return false;
        }

        String sql = "SELECT login, senha, tipo, ativo FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, loginM.getLogin());
            rs = ps.executeQuery();

            if (rs.next()) {
                if (loginM.getSenha().equals(rs.getString(2))) {
                    loginM.setLogin(rs.getString(1));
                    loginM.setTipo(rs.getInt(3));
                    loginM.setAtivo(rs.getInt(4));
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Senha incorreta! Tente novamente!");
                    return false;
                }
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

    public boolean validaUsuario(LoginModel loginM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT login FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, loginM.getLogin());
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            JOptionPane.showMessageDialog(null, "Usuário inválido! Tente novamente!");
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

    public boolean validaStatus(LoginModel loginM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT ativo FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, loginM.getLogin());
            rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Acesso negado! Usuário inativo!");
                    return false;
                }
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
