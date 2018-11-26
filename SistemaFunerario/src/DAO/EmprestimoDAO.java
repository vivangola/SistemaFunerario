/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.ContaModel;
import MODEL.EmprestimoModel;
import MODEL.MaterialModel;
import MODEL.TitularModel;
import VIEW.PesqEmprestimoView;
import VIEW.RelEmprestimoView;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EmprestimoDAO extends ConnectionDAO {

    public boolean incluir(EmprestimoModel emprestM, ContaModel contaM, MaterialModel materialM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        
        String sql = "INSERT INTO emprestimo (codigo, quantidade, dataEntrada, dataDevolucao, fk_conta, fk_material) VALUES(?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, emprestM.getCodigo());
            ps.setInt(2, emprestM.getQuantidade());
            ps.setString(3, emprestM.getDataEntrada());
            ps.setString(4, emprestM.getDataDevolv());
            ps.setInt(5, contaM.getCodigo());
            ps.setInt(6, materialM.getCodigo());
            System.err.println(ps);
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Este empréstimo já existe, por favor tente novamente!");
            System.err.println(ps);
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir material!");
            }
        }
    }
    
    public boolean devolucao(EmprestimoModel emprestM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        
        String sql = "UPDATE emprestimo set dataDevolucao = ?, quantidade = ? WHERE codigo = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, emprestM.getDataDevolv());
            ps.setInt(2, emprestM.getQuantidade());
            ps.setInt(3, emprestM.getCodigo());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir material!");
            }
        }
    }
    
    public boolean buscarCodigo(EmprestimoModel emprestM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT codigo+1 AS codigo FROM emprestimo ORDER BY codigo DESC LIMIT 1";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                emprestM.setCodigo(rs.getInt("codigo"));
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
//
//    public boolean alterar(MaterialModel materialM) {
//        PreparedStatement ps = null;
//        Connection con = getConnection();
//
//        String sql = "UPDATE material SET nome=?, modelo=?, qtdMinima=?, categoria=?, tamanho=? WHERE codigo=? ";
//
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setString(1, materialM.getNome());
//            ps.setString(2, materialM.getModelo());
//            ps.setInt(3, materialM.getQtdMinima());
//            ps.setInt(4,materialM.getCategoria());
//            ps.setDouble(5, materialM.getTamanho());
//            ps.setInt(6, materialM.getCodigo());
//            System.err.println(ps);
//            ps.execute();
//            return true;
//        } catch (SQLException e) {
//            System.err.println(e);
//            return false;
//        } finally {
//            try {
//                con.close();
//            } catch (SQLException e) {
//                System.err.println(e);
//            }
//        }
//    }
    
//    public boolean excluir(MaterialModel materialM) {;
//        PreparedStatement ps = null;
//        Connection con = getConnection();
//
//        String sql = "DELETE FROM material WHERE codigo=? ";
//
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, materialM.getCodigo());
//            ps.execute();
//            return true;
//        } catch (SQLException e) {
//            System.err.println(e);
//            return false;
//        } finally {
//            try {
//                con.close();
//            } catch (SQLException e) {
//                System.err.println(e);
//            }
//        }
//    }

    public boolean buscar(PesqEmprestimoView emprestP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "CALL listaEmprestimo_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            emprestP.tblEmprest.setModel(tModel);
            emprestP.tblEmprest.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Código");
            tModel.addColumn("Material");
            tModel.addColumn("Quantidade");
            tModel.addColumn("Data Empréstimo");
            tModel.addColumn("Titular");
            

            int[] tamanhos = {30, 50, 10, 20, 50};

            for (int x = 0; x < qtdColunas; x++) {
                emprestP.tblEmprest.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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
    
    public boolean buscar(RelEmprestimoView emprestP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "CALL listaEmprestimo_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            emprestP.tblEmprest.setModel(tModel);
            emprestP.tblEmprest.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Codigo");
            tModel.addColumn("Material");
            tModel.addColumn("Quantidade");
            tModel.addColumn("Data Emprestimo");
            tModel.addColumn("Titular");
            

            int[] tamanhos = {30, 50, 10, 20, 50};

            for (int x = 0; x < qtdColunas; x++) {
                emprestP.tblEmprest.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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

    public boolean buscarSelecionado(EmprestimoModel emprestM, TitularModel titularM, MaterialModel materialM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "CALL listaEmprestimo_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, emprestM.getCodigo());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                emprestM.setCodigo(rs.getInt("codigo"));
                emprestM.setQuantidade(rs.getInt("quantidade"));
                emprestM.setDataEntrada(rs.getString("dataEntrada"));
                titularM.setFk_conta(rs.getInt("fk_conta"));
                titularM.setNome(rs.getString("nome"));
                materialM.setCodigo(rs.getInt("codMaterial"));
                materialM.setNome(rs.getString("material"));
                materialM.setModelo(rs.getString("modelo"));
                materialM.setTamanho(rs.getString("tamanho"));
                materialM.setCategoria(rs.getInt("categoria"));
                materialM.setEstoque(rs.getInt("estoque"));
                
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
    
    public boolean inserirEmprest() {

        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "INSERT INTO emprestimo (codigo) VALUES (0)";

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
