/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import MODEL.FuncionarioModel;
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

    public boolean incluir(AcessoModel acessoM, FuncionarioModel funcM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        if (validaVinculo(funcM)) {
            return false;
        }
        
        String sql = "INSERT INTO acesso (login, senha, tipo, ativo, fk_cpf) VALUES(?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            ps.setString(2, acessoM.getSenha());
            ps.setInt(3, acessoM.getTipo());
            ps.setInt(4, acessoM.getAtivo());
            ps.setString(5, funcM.getCpf());
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Este usuário já existe, por favor tente novamente!");
            System.err.println(ps);
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
    
    public boolean validaVinculo(FuncionarioModel funcM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT fk_cpf FROM acesso WHERE fk_cpf=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, funcM.getCpf());
            rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "O Funcionário selecionado já possui um acesso cadastrado!");
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

    public boolean alterar(AcessoModel acessoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE acesso SET senha=?, tipo=?, ativo=? WHERE login=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getSenha());
            ps.setInt(2, acessoM.getTipo());
            ps.setInt(3, acessoM.getAtivo());
            ps.setString(4, acessoM.getLogin());
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

    public boolean buscar(PesqAcessoView acessoP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaAcesso_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            acessoP.tblAcesso.setModel(tModel);
            acessoP.tblAcesso.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Funcionário");
            tModel.addColumn("Usuário");
            tModel.addColumn("Tipo");
            tModel.addColumn("Status");

            int[] tamanhos = {200, 100, 50, 20};

            for (int x = 0; x < qtdColunas; x++) {
                acessoP.tblAcesso.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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

    public boolean buscarSelecionado(AcessoModel acessoM, FuncionarioModel funcM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "call listaAcesso_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, acessoM.getLogin());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                funcM.setNome(rs.getString("nome"));
                acessoM.setLogin(rs.getString("login"));
                acessoM.setAtivo(rs.getInt("ativo"));
                acessoM.setTipo(rs.getInt("tipo"));
                acessoM.setSenha(rs.getString("senha"));
                
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
