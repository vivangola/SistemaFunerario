/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.MaterialModel;
import VIEW.PesqMaterialView;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MaterialDAO extends ConnectionDAO {

    public boolean incluir(MaterialModel materialM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        
        String sql = "INSERT INTO material (codigo, nome, modelo, qtdMinima, categoria, tamanho, estoque) VALUES(?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, materialM.getCodigo());
            ps.setString(2, materialM.getNome());
            ps.setString(3, materialM.getModelo());
            ps.setInt(4, materialM.getQtdMinima());
            ps.setInt(5,materialM.getCategoria());
            ps.setDouble(6, materialM.getTamanho());
            ps.setInt(7, materialM.getEstoque());
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Este material já existe, por favor tente novamente!");
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
    
    public boolean buscarCodigo(MaterialModel materialM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT codigo+1 AS codigo FROM material ORDER BY codigo DESC LIMIT 1";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                materialM.setCodigo(rs.getInt("codigo"));
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

    public boolean alterar(MaterialModel materialM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE nome=?, modelo=?, qtdMinima=?, categoria=?, tamanho=?, estoque=? WHERE codigo=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, materialM.getNome());
            ps.setString(2, materialM.getModelo());
            ps.setInt(3, materialM.getQtdMinima());
            ps.setInt(4,materialM.getCategoria());
            ps.setDouble(5, materialM.getTamanho());
            ps.setInt(6, materialM.getEstoque());
            ps.setInt(7, materialM.getCodigo());
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

    public boolean excluir(MaterialModel materialM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM material WHERE codigo=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, materialM.getCodigo());
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

    public boolean buscar(PesqMaterialView materialP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "CALL listaMateriais_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            materialP.tblMaterial.setModel(tModel);
            materialP.tblMaterial.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Código");
            tModel.addColumn("Material");
            tModel.addColumn("Modelo");
            tModel.addColumn("Tamanho");
            tModel.addColumn("Categoria");
            tModel.addColumn("Estoque Mínimo");
            tModel.addColumn("Estoque Atual");

            int[] tamanhos = {30, 100, 50, 20, 20, 10, 10};

            for (int x = 0; x < qtdColunas; x++) {
                materialP.tblMaterial.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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

    public boolean buscarSelecionado(MaterialModel materialM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "CALL listaMateriais_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, materialM.getCodigo());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                materialM.setCodigo(rs.getInt("codigo"));
                materialM.setNome(rs.getString("nome"));
                materialM.setModelo(rs.getString("modelo"));
                materialM.setTamanho(rs.getDouble("tamanho"));
                materialM.setQtdMinima(rs.getInt("qtdMinima"));
                materialM.setCategoria(rs.getInt("categoria"));
                
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
    
    public boolean inserirMaterial() {

        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "INSERT INTO material (codigo) VALUES (0)";

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
