/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.AcessoModel;
import MODEL.ContaModel;
import MODEL.FuncionarioModel;
import MODEL.PlanosModel;
import VIEW.PesqContaView;
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

public class ContaDAO extends ConnectionDAO {

    public boolean incluir(ContaModel contaM, PlanosModel planoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO conta (codigo, dataInclusao, situacao, vencimentoMensalidade, fk_plano) VALUES(?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            ps.setString(2, contaM.getDtInclusao());
            ps.setInt(3, contaM.getSituacao());
            ps.setInt(4, contaM.getVencimento());
            ps.setInt(5, planoM.getCodigo());
            System.err.println(ps);
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Conta já cadastrado, por favor tente outro!");
            return false;
        } catch(MysqlDataTruncation dt){
            JOptionPane.showMessageDialog(null, "Data de inclusão inválida!");
            System.err.println(dt);
            return false;
        }catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir conta!");
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

    public boolean buscar(PesqContaView contaP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaConta_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            contaP.tblConta.setModel(tModel);
            contaP.tblConta.setDefaultEditor(Object.class, null);
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Codigo");
            tModel.addColumn("Titular");
            tModel.addColumn("Inclusão");
            tModel.addColumn("Situação");
            tModel.addColumn("Dia Vencimento");
            tModel.addColumn("Plano");

            int[] tamanhos = {5, 50, 50, 50, 50, 50};

            for (int x = 0; x < qtdColunas; x++) {
                contaP.tblConta.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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
    
    public boolean buscarSelecionado(ContaModel contaM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "call listaConta_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            rs = ps.executeQuery();

            while (rs.next()) {
                
                contaM.setCodigo(rs.getInt("codigo"));
                contaM.setDtInclusao(rs.getString("dataInclusao"));
                contaM.setSituacao(rs.getInt("situacao"));
                contaM.setVencimento(rs.getInt("vencimentoMensalidade"));
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
    
    public boolean inserirConta() {

        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "INSERT INTO conta (codigo, dataInclusao, situacao, vencimentoMensalidade) VALUES (0,'1900-01-01',0,0)";

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
    
    public boolean buscarCodigo(ContaModel contaM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT codigo+1 AS codigo FROM conta ORDER BY codigo DESC LIMIT 1";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                contaM.setCodigo(rs.getInt("codigo"));
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
}
