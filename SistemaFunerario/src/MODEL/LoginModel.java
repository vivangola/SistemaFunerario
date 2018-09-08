/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODEL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author junio
 */
public class LoginModel extends ConnectionModel {

    public boolean login(AcessoModel acessoM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        if (!validaUsuario(acessoM)) {
            return false;
        }

        if (!validaStatus(acessoM)) {
            return false;
        }

        String sql = "SELECT login, senha, tipo, ativo FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            rs = ps.executeQuery();

            if (rs.next()) {
                if (acessoM.getSenha().equals(rs.getString(2))) {
                    acessoM.setLogin(rs.getString(1));
                    acessoM.setTipo(rs.getInt(3));
                    acessoM.setAtivo(rs.getInt(4));
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

    public boolean validaUsuario(AcessoModel acessoM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT login FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
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

    public boolean validaStatus(AcessoModel acessoM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT ativo FROM acesso WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
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

    public boolean validarCampos(String login, String senha) {
        if (login.isEmpty() || senha.isEmpty()) {
            return false;
        }
        return true;
    }
}
