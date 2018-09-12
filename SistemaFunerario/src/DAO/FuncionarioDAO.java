/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import MODEL.FuncionarioModel;
import VIEW.PesqFuncionariosView;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FuncionarioDAO extends ConnectionDAO {

    public boolean incluir(FuncionarioModel funcM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO funcionario (cpf, rg, nome, telefone, sexo, estadoCivil, cargo, endereco, bairro, estado, cidade, cep, dataNascimento) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, funcM.getCpf());
            ps.setString(2, funcM.getRg());
            ps.setString(3, funcM.getNome());
            ps.setString(4, funcM.getTelefone());
            ps.setString(5, funcM.getSexo());
            ps.setString(6, funcM.getEstadoCivil());
            ps.setString(7, funcM.getCargo());
            ps.setString(8, funcM.getEndereco());
            ps.setString(9, funcM.getBairro());
            ps.setString(10, funcM.getEstado());
            ps.setString(11, funcM.getCidade());
            ps.setString(12, funcM.getCep());
            ps.setString(13, funcM.getNascimento());
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

    public boolean alterar(FuncionarioModel funcM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE funcionario SET cpf=?, rg=?, nome=?, telefone=?, sexo=?, estadoCivil=?, cargo=?, endereco=?, bairro=?, estado=?, cidade=?, cep=?, dataNascimento=? WHERE cpf = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, funcM.getCpf());
            ps.setString(2, funcM.getRg());
            ps.setString(3, funcM.getNome());
            ps.setString(4, funcM.getTelefone());
            ps.setString(5, funcM.getSexo());
            ps.setString(6, funcM.getEstadoCivil());
            ps.setString(7, funcM.getCargo());
            ps.setString(8, funcM.getEndereco());
            ps.setString(9, funcM.getBairro());
            ps.setString(10, funcM.getEstado());
            ps.setString(11, funcM.getCidade());
            ps.setString(12, funcM.getCep());
            ps.setString(13, funcM.getNascimento());
            ps.setString(14, funcM.getCpf());
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

    public boolean excluir(FuncionarioModel funcM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM funcionario WHERE cpf=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, funcM.getCpf());
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

    public boolean buscar(PesqFuncionariosView funcP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaFuncionario_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            funcP.tblAcesso.setModel(tModel);
            funcP.tblAcesso.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Nome");
            tModel.addColumn("Cargo");
            tModel.addColumn("CPF");
            tModel.addColumn("RG");
            tModel.addColumn("Telefone");
            tModel.addColumn("Sexo");
            tModel.addColumn("Civil");
            tModel.addColumn("Nascimento");
            tModel.addColumn("Endereço");
            tModel.addColumn("Bairro");
            tModel.addColumn("Cidade");
            tModel.addColumn("Estado");
            tModel.addColumn("CEP");

            int[] tamanhos = {100, 50, 50, 50, 50, 3, 50, 50, 100, 50, 50, 3, 30};

            for (int x = 0; x < qtdColunas; x++) {
                funcP.tblAcesso.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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
    
    public boolean buscarSelecionado(FuncionarioModel funcM, AcessoModel acessoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "call listaFuncionario_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, funcM.getCpf());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                funcM.setCpf(rs.getString("cpf"));
                funcM.setRg(rs.getString("rg"));
                funcM.setNome(rs.getString("nome"));
                funcM.setCargo(rs.getString("cargo"));
                funcM.setTelefone(rs.getString("telefone"));
                funcM.setSexo(rs.getString("sexo"));
                funcM.setEstadoCivil(rs.getString("estadoCivil"));
                funcM.setNascimento(rs.getString("dataNascimento"));
                funcM.setEndereco(rs.getString("endereco"));
                funcM.setBairro(rs.getString("bairro"));
                funcM.setCidade(rs.getString("cidade"));
                funcM.setEstado(rs.getString("estado"));
                funcM.setCep(rs.getString("cep"));
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
