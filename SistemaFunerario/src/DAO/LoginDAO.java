/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.FuncionarioModel;
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

    public boolean login(LoginModel loginM, FuncionarioModel funcM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        if (!validaUsuario(loginM)) {
            JOptionPane.showMessageDialog(null, "Usuário inválido! Tente novamente!");
            return false;
        }

        if (!validaStatus(loginM)) {
            return false;
        }
        
        String sql = "SELECT login,senha,tipo,nome FROM acesso INNER JOIN funcionario ON cpf = fk_cpf WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, loginM.getLogin());
            rs = ps.executeQuery();

            if (rs.next()) {
                if (loginM.getSenha().equals(rs.getString(2))) {
                    loginM.setLogin(rs.getString(1));
                    loginM.setTipo(rs.getInt(3));
                    funcM.setNome(rs.getString(4));
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
    
    public boolean atualizarSituacao() {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call atualizaDebito_sp()";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

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
    
    public boolean inserirAdmin() {

        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "call criarAdmin_sp()";

        try {
            ps = con.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                return false;
            }
        }
    }

}
