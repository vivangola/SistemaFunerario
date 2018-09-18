/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.PlanosModel;
import VIEW.PesqPlanosView;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PlanosDAO extends ConnectionDAO {

    public boolean incluir(PlanosModel planoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        
        String sql = "INSERT INTO plano (codigo, nome, valorMensalidade, qtdDependente, carencia) VALUES(?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, planoM.getCodigo());
            ps.setString(2, planoM.getNome());
            ps.setDouble(3, planoM.getMensalidade());
            ps.setInt(4, planoM.getQtdDependente());
            ps.setInt(5,planoM.getCarencia());
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Este plano já existe, por favor tente novamente!");
            System.err.println(ps);
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir plano!");
            }
        }
    }
    
    public boolean buscarCodigo(PlanosModel planoM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT codigo+1 AS codigo FROM plano ORDER BY codigo DESC LIMIT 1";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                planoM.setCodigo(rs.getInt("codigo"));
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

    public boolean alterar(PlanosModel planoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE plano SET nome=?, valorMensalidade=?, qtdDependente=?, carencia=? WHERE codigo=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, planoM.getNome());
            ps.setDouble(2, planoM.getMensalidade());
            ps.setInt(3, planoM.getQtdDependente());
            ps.setInt(4,planoM.getCarencia());
            ps.setInt(5, planoM.getCodigo());
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
                System.err.println(e);
            }
        }
    }

    public boolean excluir(PlanosModel planoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM plano WHERE codigo=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, planoM.getCodigo());
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

    public boolean buscar(PesqPlanosView planoP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "CALL listaPlanos_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            planoP.tblPlanos.setModel(tModel);
            planoP.tblPlanos.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Código");
            tModel.addColumn("Plano");
            tModel.addColumn("Carencia");
            tModel.addColumn("Valor Mensalidade");
            tModel.addColumn("Qtd Dependentes");

            int[] tamanhos = {30, 100, 20, 30, 20};

            for (int x = 0; x < qtdColunas; x++) {
                planoP.tblPlanos.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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

    public boolean buscarSelecionado(PlanosModel planoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "CALL listaPlanos_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, planoM.getCodigo());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                planoM.setCodigo(rs.getInt("codigo"));
                planoM.setNome(rs.getString("nome"));
                planoM.setQtdDependente(rs.getInt("qtdDependente"));
                planoM.setMensalidade(rs.getDouble("valorMensalidade"));
                planoM.setCarencia(rs.getInt("carencia"));
                
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
    
    public boolean inserirPlanos() {

        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "INSERT INTO plano (codigo) VALUES (0)";

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
