/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.FuncionarioModel;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

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

    public boolean buscar(FuncionarioModel funcM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT * FROM funcionario WHERE cpf=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, funcM.getCpf());
            rs = ps.executeQuery();

            if (rs.next()) {
//                funcM.setLogin(rs.getString("login"));
//                funcM.setSenha(rs.getString("senha"));
//                funcM.setTipo(Integer.parseInt(rs.getString("tipo")));
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
