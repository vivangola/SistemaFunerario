/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import MODEL.ContaModel;
import MODEL.DependenteModel;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DependenteDAO extends ConnectionDAO {

    public boolean incluir(DependenteModel dependM, ContaModel contaM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO dependente (nome, cpf, rg, sexo, dataNascimento, parentesco, fk_conta) VALUES(?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dependM.getNome());
            ps.setString(2, dependM.getCpf());
            ps.setString(3, dependM.getRg());
            ps.setString(4, dependM.getSexo());
            ps.setString(5, dependM.getNascimento());
            ps.setString(6, dependM.getParentesco());
            ps.setInt(7, contaM.getCodigo());
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

    public boolean alterar(DependenteModel dependM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE titular SET cpf=?, rg=?, nome=?, telefone=?, sexo=?, estadoCivil=?, cargo=?, endereco=?, bairro=?, estado=?, cidade=?, cep=?, dataNascimento=? WHERE cpf = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dependM.getCpf());
            ps.setString(2, dependM.getRg());
            ps.setString(3, dependM.getNome());
            ps.setString(5, dependM.getSexo());
            ps.setString(13, dependM.getNascimento());
            ps.setString(14, dependM.getCpf());
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

    public boolean excluir(DependenteModel dependM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM titular WHERE cpf=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dependM.getCpf());
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
    
    public boolean buscarSelecionado(DependenteModel dependM, AcessoModel acessoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "call listaTitular_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dependM.getCpf());
            rs = ps.executeQuery();

            while (rs.next()) {
                
//                dependM.setCpf(rs.getString("cpf"));
//                dependM.setRg(rs.getString("rg"));
//                dependM.setNome(rs.getString("nome"));
//                dependM.setCargo(rs.getString("cargo"));
//                dependM.setTelefone(rs.getString("telefone"));
//                dependM.setSexo(rs.getString("sexo"));
//                dependM.setEstadoCivil(rs.getString("estadoCivil"));
//                dependM.setNascimento(rs.getString("dataNascimento"));
//                dependM.setEndereco(rs.getString("endereco"));
//                dependM.setBairro(rs.getString("bairro"));
//                dependM.setCidade(rs.getString("cidade"));
//                dependM.setEstado(rs.getString("estado"));
//                dependM.setCep(rs.getString("cep"));
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