/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import VIEW.PesqAcessoView;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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

    public boolean buscar(PesqAcessoView acessoP, String txtBusca, String campo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        String where = "";

        if (!campo.equals("")) {
            where = "WHERE " + campo + " like '%" + txtBusca + "%'";
        }
        String sql = " SELECT 'DENILSON', login, case tipo when 1 then 'Administrador' else 'Funcionário' end, case ativo when 1 then 'Ativo' else 'Inativo' end FROM acesso " + where;

        try {
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            acessoP.tblAcesso.setModel(tModel);
            acessoP.tblAcesso.setDefaultEditor(Object.class, null);
            //acessoP.tblAcesso.setRowSelectionAllowed(true);
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Funcionário");
            tModel.addColumn("Usuário");
            tModel.addColumn("Tipo");
            tModel.addColumn("Status");

            int[] anchos = {200, 100, 50, 20};

            for (int x = 0; x < qtdColunas; x++) {
                acessoP.tblAcesso.getColumnModel().getColumn(x).setPreferredWidth(anchos[x]);
            }

            while (rs.next()) {

                Object[] linhas = new Object[qtdColunas];
                
                for (int i = 0; i < qtdColunas; i++) {
                    linhas[i] = rs.getObject(i + 1);
                }
                tModel.addRow(linhas);
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

    public boolean buscarSelecionado(AcessoModel acessoM, String loginSelect) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = " SELECT 'DENILSON', login, tipo, ativo FROM acesso WHERE login = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, loginSelect);
            rs = ps.executeQuery();

            while (rs.next()) {
                
                //acessoM.setFuncionario(rs.getString("funcionario"));
                acessoM.setLogin(rs.getString("login"));
                acessoM.setAtivo(rs.getInt("ativo"));
                acessoM.setTipo(rs.getInt("tipo"));
                
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
