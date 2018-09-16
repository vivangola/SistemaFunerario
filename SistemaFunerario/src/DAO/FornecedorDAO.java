/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.FornecedorModel;
import VIEW.PesqFornecedorView;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FornecedorDAO extends ConnectionDAO {

    public boolean incluir(FornecedorModel forncM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO fornecedor (cnpj, nome, telefone, endereco, bairro, estado, cidade, cep, email, inscricaoEstadual) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, forncM.getCnpj());
            ps.setString(2, forncM.getNome());
            ps.setString(3, forncM.getTelefone());
            ps.setString(4, forncM.getEndereco());
            ps.setString(5, forncM.getBairro());
            ps.setString(6, forncM.getEstado());
            ps.setString(7, forncM.getCidade());
            ps.setString(8, forncM.getCep());
            ps.setString(9, forncM.getEmail());
            ps.setString(10, forncM.getInscricaoEstadual());
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "CNPJ já cadastrado, por favor tente outro!");
            return false;
        }catch (SQLException e) {
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

    public boolean alterar(FornecedorModel forncM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE fornecedor SET cnpj=?, nome=?, telefone=?, endereco=?, bairro=?, estado=?, cidade=?, cep=?, email=?, inscricaoEstadual=? WHERE cnpj = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, forncM.getCnpj());
            ps.setString(2, forncM.getNome());
            ps.setString(3, forncM.getTelefone());
            ps.setString(4, forncM.getEndereco());
            ps.setString(5, forncM.getBairro());
            ps.setString(6, forncM.getEstado());
            ps.setString(7, forncM.getCidade());
            ps.setString(8, forncM.getCep());
            ps.setString(9, forncM.getEmail());
            ps.setString(10, forncM.getInscricaoEstadual());
            ps.setString(11, forncM.getCnpj());
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

    public boolean excluir(FornecedorModel forncM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM fornecedor WHERE cnpj=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, forncM.getCnpj());
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
    
    public boolean buscar(PesqFornecedorView forncP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaFornecedor_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            forncP.tblFornecedor.setModel(tModel);
            forncP.tblFornecedor.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Nome");
            tModel.addColumn("CNPJ");
            tModel.addColumn("Telefone");
            tModel.addColumn("Email");
            tModel.addColumn("Endereço");
            tModel.addColumn("Bairro");
            tModel.addColumn("Cidade");
            tModel.addColumn("Estado");
            tModel.addColumn("CEP");
            tModel.addColumn("Inscrição Estadual");

            int[] tamanhos = {100, 50, 50, 50, 50, 50, 50, 5, 50, 5};

            for (int x = 0; x < qtdColunas; x++) {
                forncP.tblFornecedor.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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
    
    public boolean buscarSelecionado(FornecedorModel forncM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "call listaFornecedor_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, forncM.getCnpj());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                forncM.setCnpj(rs.getString("cnpj"));
                forncM.setNome(rs.getString("nome"));
                forncM.setTelefone(rs.getString("telefone"));
                forncM.setEmail(rs.getString("email"));
                forncM.setEndereco(rs.getString("endereco"));
                forncM.setBairro(rs.getString("bairro"));
                forncM.setCidade(rs.getString("cidade"));
                forncM.setEstado(rs.getString("estado"));
                forncM.setCep(rs.getString("cep"));
                forncM.setInscricaoEstadual(rs.getString("inscricaoEstadual"));
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
