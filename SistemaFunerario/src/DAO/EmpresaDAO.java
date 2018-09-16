/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.EmpresaModel;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EmpresaDAO extends ConnectionDAO {

    public boolean incluir(EmpresaModel empresaM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO empresa (cnpj, nome, telefone, endereco, bairro, estado, cidade, cep, email, raioAtuacao) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, empresaM.getCnpj());
            ps.setString(2, empresaM.getNome());
            ps.setString(3, empresaM.getTelefone());
            ps.setString(4, empresaM.getEndereco());
            ps.setString(5, empresaM.getBairro());
            ps.setString(6, empresaM.getEstado());
            ps.setString(7, empresaM.getCidade());
            ps.setString(8, empresaM.getCep());
            ps.setString(9, empresaM.getEmail());
            ps.setInt(10, empresaM.getRaioAtuacao());
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

    public boolean alterar(EmpresaModel empresaM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE empresa SET cnpj=?, nome=?, telefone=?, endereco=?, bairro=?, estado=?, cidade=?, cep=?, email=?, raioAtuacao=? WHERE cnpj = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, empresaM.getCnpj());
            ps.setString(2, empresaM.getNome());
            ps.setString(3, empresaM.getTelefone());
            ps.setString(4, empresaM.getEndereco());
            ps.setString(5, empresaM.getBairro());
            ps.setString(6, empresaM.getEstado());
            ps.setString(7, empresaM.getCidade());
            ps.setString(8, empresaM.getCep());
            ps.setString(9, empresaM.getEmail());
            ps.setInt(10, empresaM.getRaioAtuacao());
            ps.setString(11, empresaM.getCnpj());
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

    public boolean excluir(EmpresaModel empresaM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM empresa WHERE cnpj=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, empresaM.getCnpj());
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
    
    public boolean buscar(EmpresaModel empresaM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM EMPRESA";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                
                empresaM.setCnpj(rs.getString("cnpj"));
                empresaM.setNome(rs.getString("nome"));
                empresaM.setTelefone(rs.getString("telefone"));
                empresaM.setEndereco(rs.getString("endereco"));
                empresaM.setBairro(rs.getString("bairro"));
                empresaM.setCidade(rs.getString("cidade"));
                empresaM.setEstado(rs.getString("estado"));
                empresaM.setCep(rs.getString("cep"));
                empresaM.setEmail(rs.getString("email"));
                empresaM.setRaioAtuacao(rs.getInt("raioAtuacao"));
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
    
    public boolean validaEmpresa(EmpresaModel empresaM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT * FROM EMPRESA";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
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
