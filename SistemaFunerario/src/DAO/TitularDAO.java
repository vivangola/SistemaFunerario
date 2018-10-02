/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import MODEL.ContaModel;
import MODEL.TitularModel;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class TitularDAO extends ConnectionDAO {

    public boolean incluir(TitularModel titularM, ContaModel contaM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO titular (cpf, rg, nome, telefone, sexo, estadoCivil, cargo, endereco, bairro, estado, cidade, cep, dataNascimento, fk_conta) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, titularM.getCpf());
            ps.setString(2, titularM.getRg());
            ps.setString(3, titularM.getNome());
            ps.setString(4, titularM.getTelefone());
            ps.setString(5, titularM.getSexo());
            ps.setString(6, titularM.getEstadoCivil());
            ps.setString(7, titularM.getCargo());
            ps.setString(8, titularM.getEndereco());
            ps.setString(9, titularM.getBairro());
            ps.setString(10, titularM.getEstado());
            ps.setString(11, titularM.getCidade());
            ps.setString(12, titularM.getCep());
            ps.setString(13, titularM.getNascimento());
            ps.setInt(14, contaM.getCodigo());
            System.err.println(ps);
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "CPF já cadastrado, por favor tente outro!");
            return false;
        } catch(MysqlDataTruncation dt){
            JOptionPane.showMessageDialog(null, "Data de nascimento inválida!");
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

    public boolean alterar(TitularModel titularM, ContaModel contaM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE titular SET cpf=?, rg=?, nome=?, telefone=?, sexo=?, estadoCivil=?, cargo=?, endereco=?, bairro=?, estado=?, cidade=?, cep=?, dataNascimento=? WHERE fk_conta = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, titularM.getCpf());
            ps.setString(2, titularM.getRg());
            ps.setString(3, titularM.getNome());
            ps.setString(4, titularM.getTelefone());
            ps.setString(5, titularM.getSexo());
            ps.setString(6, titularM.getEstadoCivil());
            ps.setString(7, titularM.getCargo());
            ps.setString(8, titularM.getEndereco());
            ps.setString(9, titularM.getBairro());
            ps.setString(10, titularM.getEstado());
            ps.setString(11, titularM.getCidade());
            ps.setString(12, titularM.getCep());
            ps.setString(13, titularM.getNascimento());
            ps.setInt(14, contaM.getCodigo());
            ps.execute();
            return true;
        } catch(MysqlDataTruncation dt){
            JOptionPane.showMessageDialog(null, "Data de nascimento inválida!");
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

    public boolean excluir(TitularModel titularM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM titular WHERE cpf=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, titularM.getCpf());
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

//    public boolean buscar(PesqTitularView titularP, String txtBusca, int cmbBusca) {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection con = getConnection();
//
//        String sql = "call listaTitular_sp (?,?,0)";
//
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setString(1, txtBusca);
//            ps.setInt(2, cmbBusca);
//            rs = ps.executeQuery();
//
//            DefaultTableModel tModel = new DefaultTableModel();
//            titularP.tblTitular.setModel(tModel);
//            titularP.tblTitular.setDefaultEditor(Object.class, null);
//            
//            ResultSetMetaData rsMD = rs.getMetaData();
//            int qtdColunas = rsMD.getColumnCount();
//
//            tModel.addColumn("Nome");
//            tModel.addColumn("Cargo");
//            tModel.addColumn("CPF");
//            tModel.addColumn("RG");
//            tModel.addColumn("Telefone");
//            tModel.addColumn("Sexo");
//            tModel.addColumn("Civil");
//            tModel.addColumn("Nascimento");
//            tModel.addColumn("Endereço");
//            tModel.addColumn("Bairro");
//            tModel.addColumn("Cidade");
//            tModel.addColumn("Estado");
//            tModel.addColumn("CEP");
//
//            int[] tamanhos = {100, 50, 50, 50, 50, 3, 50, 50, 100, 50, 50, 3, 30};
//
//            for (int x = 0; x < qtdColunas; x++) {
//                titularP.tblTitular.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
//            }
//
//            while (rs.next()) {
//
//                Object[] linhas = new Object[qtdColunas];
//                
//                for (int i = 0; i < qtdColunas; i++) {
//                    linhas[i] = rs.getObject(i + 1);
//                }
//                tModel.addRow(linhas);
//            }
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
    
    public boolean buscarSelecionado(TitularModel titularM, AcessoModel acessoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "call listaTitular_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, titularM.getCpf());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                titularM.setCpf(rs.getString("cpf"));
                titularM.setRg(rs.getString("rg"));
                titularM.setNome(rs.getString("nome"));
                titularM.setCargo(rs.getString("cargo"));
                titularM.setTelefone(rs.getString("telefone"));
                titularM.setSexo(rs.getString("sexo"));
                titularM.setEstadoCivil(rs.getString("estadoCivil"));
                titularM.setNascimento(rs.getString("dataNascimento"));
                titularM.setEndereco(rs.getString("endereco"));
                titularM.setBairro(rs.getString("bairro"));
                titularM.setCidade(rs.getString("cidade"));
                titularM.setEstado(rs.getString("estado"));
                titularM.setCep(rs.getString("cep"));
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
