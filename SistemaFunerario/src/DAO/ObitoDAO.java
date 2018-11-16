/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import CONTROLLER.AlterarTitularController;
import MODEL.ContaModel;
import MODEL.ObitoModel;
import MODEL.TitularModel;
import VIEW.AlterarTitularView;
import VIEW.ObitosView;
import VIEW.PesqObitoView;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ObitoDAO extends ConnectionDAO {

    public boolean incluir(ContaModel contaM, ObitoModel obitoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "INSERT INTO obito (codigo, fk_conta, fk_cpf, localObito, dataObito, horaObito, localVelorio, dataVelorio, horaVelorio, localEntero, dataEntero, horaEntero) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, obitoM.getCodigo());
            ps.setInt(2, contaM.getCodigo());
            ps.setString(3, obitoM.getFk_cpf());
            ps.setString(4, obitoM.getLocalObito());
            ps.setString(5, obitoM.getDtObito());
            ps.setString(6, obitoM.getHoraObito());
            ps.setString(7, obitoM.getLocalVel());
            ps.setString(8, obitoM.getDtVel());
            ps.setString(9, obitoM.getHoraVel());
            ps.setString(10, obitoM.getLocalEnt());
            ps.setString(11, obitoM.getDtEnt());
            ps.setString(12, obitoM.getHoraEnt());
            System.err.println(ps);
            ps.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null, "Obito já cadastrado, por favor tente outro!");
            return false;
        } catch (MysqlDataTruncation dt) {
            JOptionPane.showMessageDialog(null, "Data de inclusão inválida!");
            System.err.println(dt);
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao incluir obito!");
            }
        }
    }

    public boolean alterar(ObitoModel obitoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE obito SET localObito = ?, dataObito = ?, horaObito = ?, localVelorio = ?, dataVelorio = ?, horaVelorio = ?, localEntero = ?, dataEntero = ?, horaEntero  = ? WHERE codigo = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, obitoM.getLocalObito());
            ps.setString(2, obitoM.getDtObito());
            ps.setString(3, obitoM.getHoraObito());
            ps.setString(4, obitoM.getLocalVel());
            ps.setString(5, obitoM.getDtVel());
            ps.setString(6, obitoM.getHoraVel());
            ps.setString(7, obitoM.getLocalEnt());
            ps.setString(8, obitoM.getDtEnt());
            ps.setString(9, obitoM.getHoraEnt());
            ps.setInt(10, obitoM.getCodigo());
            ps.execute();
            return true;
        } catch (MysqlDataTruncation dt) {
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

    public boolean excluir(ObitoModel obitoM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "DELETE FROM obito WHERE codigo=? ";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, obitoM.getCodigo());
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

    public boolean buscar(PesqObitoView obitoP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaObito_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            System.err.println(ps);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            obitoP.tblObitos.setModel(tModel);
            obitoP.tblObitos.setDefaultEditor(Object.class, null);

            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Codigo");
            tModel.addColumn("Falecido");
            tModel.addColumn("Data Enterro");
            tModel.addColumn("Hora Enterro");
            tModel.addColumn("Local Enterro");

            int[] tamanhos = {5, 50, 50, 50, 50};

            for (int x = 0; x < qtdColunas; x++) {
                obitoP.tblObitos.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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

    public boolean buscarSelecionado(ContaModel contaM, TitularModel titularM, ObitoModel obitoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaObito_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, obitoM.getCodigo());
            System.err.println(ps);
            rs = ps.executeQuery();

            while (rs.next()) {

                contaM.setCodigo(rs.getInt("fk_conta"));
                titularM.setNome(rs.getString("nome"));
                obitoM.setCodigo(rs.getInt("codigo"));
                obitoM.setDtEnt(rs.getString("dataEntero"));
                obitoM.setHoraEnt(rs.getString("horaEntero"));
                obitoM.setLocalEnt(rs.getString("localEntero"));
                obitoM.setDtObito(rs.getString("dataObito"));
                obitoM.setHoraObito(rs.getString("horaObito"));
                obitoM.setLocalObito(rs.getString("localObito"));
                obitoM.setDtVel(rs.getString("dataVelorio"));
                obitoM.setHoraVel(rs.getString("horaVelorio"));
                obitoM.setLocalVel(rs.getString("localVelorio"));
                obitoM.setFk_cpf(rs.getString("fk_cpf"));

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
    
    public boolean falecido(ContaModel contaM, String nome, int index) {
        PreparedStatement ps = null;
        Connection con = getConnection();
        String sql = "";
        if(index == 1){
            sql = "UPDATE titular SET falecido=1 WHERE fk_conta =? AND nome = ?";
        }else{
            sql = "UPDATE dependente SET falecido=1 WHERE fk_conta =? AND nome = ?";
        }

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            ps.setString(2, nome);
            System.err.println(ps);
            ps.execute();
            return true;
        }  catch (SQLException e) {
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

    public boolean inserirObito() {

        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "INSERT INTO obito (codigo) VALUES (0)";

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

    public boolean buscarCodigo(ObitoModel obitoM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "SELECT codigo+1 AS codigo FROM obito ORDER BY codigo DESC LIMIT 1";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                obitoM.setCodigo(rs.getInt("codigo"));
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
    
    public boolean buscarPessoas(ContaModel contaM, ObitoModel obitoM, ObitosView obitoV){
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaPessoas_sp(?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, obitoM.getCodigo());
            ps.setInt(2, contaM.getCodigo());
            System.err.println(ps);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                obitoV.cmbFalecido.addItem(rs.getString("nome"));
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
    
    public boolean buscarDepend(ContaModel contaM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaPessoas_sp(-1,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            System.err.println(ps);
            rs = ps.executeQuery();

            return rs.next();
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
