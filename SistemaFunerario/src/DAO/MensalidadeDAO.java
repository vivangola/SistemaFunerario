/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MODEL.ContaModel;
import MODEL.MensalidadeModel;
import MODEL.PlanosModel;
import MODEL.TitularModel;
import VIEW.PesqMensalidadeView;
import com.mysql.jdbc.MysqlDataTruncation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MensalidadeDAO extends ConnectionDAO {

    public boolean concluir(MensalidadeModel mensalM) {
        PreparedStatement ps = null;
        Connection con = getConnection();

        String sql = "UPDATE mensalidade SET tipoPagamento = ?, dataPagamento = ?, valor = ? WHERE numeroPagamento = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, mensalM.getTipoPagamento());
            ps.setString(2, mensalM.getDataPagamento());
            ps.setDouble(3, mensalM.getValor());
            ps.setInt(4, mensalM.getCodigo());
            ps.execute();
            return true;
        } catch (MysqlDataTruncation dt) {
            JOptionPane.showMessageDialog(null, "Data de Pagamento inválida!");
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

    public boolean buscar(PesqMensalidadeView mensalP, String txtBusca, int cmbBusca) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaMensalidade_sp (?,?,0)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, txtBusca);
            ps.setInt(2, cmbBusca);
            System.err.println(ps);
            rs = ps.executeQuery();

            DefaultTableModel tModel = new DefaultTableModel();
            mensalP.tblMensalidade.setModel(tModel);
            mensalP.tblMensalidade.setDefaultEditor(Object.class, null);

            ResultSetMetaData rsMD = rs.getMetaData();
            int qtdColunas = rsMD.getColumnCount();

            tModel.addColumn("Numero");
            tModel.addColumn("Período");
            tModel.addColumn("Vencimento");
            tModel.addColumn("Conta");
            tModel.addColumn("Titular");
            tModel.addColumn("Situação");
            tModel.addColumn("Plano");
            tModel.addColumn("Valor");

            int[] tamanhos = {5, 50, 50, 20, 80, 20, 50, 50};

            for (int x = 0; x < qtdColunas; x++) {
                mensalP.tblMensalidade.getColumnModel().getColumn(x).setPreferredWidth(tamanhos[x]);
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
    
    public boolean atualizarSituacao(ContaModel contaM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call atualizaDebito_sp()";

        try {
            ps = con.prepareStatement(sql);
            //ps.setInt(1, contaM.getCodigo());
            rs = ps.executeQuery();

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
    
     public boolean gerarMensalidade(ContaModel contaM) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call geraMensalidade_sp(?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, contaM.getCodigo());
            rs = ps.executeQuery();

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

    public boolean buscarSelecionado(MensalidadeModel mensalM, ContaModel contaM,TitularModel titularM, PlanosModel planoM) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();

        String sql = "call listaMensalidade_sp (?,0,1)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, mensalM.getCodigo());
            System.err.println(ps);
            rs = ps.executeQuery();

            while (rs.next()) {

                mensalM.setCodigo(rs.getInt("numeroPagamento"));
                mensalM.setPeriodo(rs.getString("periodo"));
                mensalM.setVencimento(rs.getString("vencimento"));
                contaM.setCodigo(rs.getInt("codConta"));
                titularM.setNome(rs.getString("titular"));
                planoM.setNome(rs.getString("plano"));
                planoM.setMensalidade(rs.getDouble("valorMensalidade"));

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
