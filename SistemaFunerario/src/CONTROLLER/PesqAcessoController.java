/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.AcessoDAO;
import MODEL.AcessoModel;
import MODEL.FuncionarioModel;
import VIEW.AcessoView;
import VIEW.PesqAcessoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqAcessoController implements ActionListener {

    private PesqAcessoView acessoP;
    private AcessoDAO acessoD;
    private AcessoModel acessoM;

    public PesqAcessoController(PesqAcessoView acessoP, AcessoDAO acessoD, AcessoModel acessoM) {
        this.acessoP = acessoP;
        this.acessoD = acessoD;
        this.acessoM = acessoM;
        this.acessoP.btnBuscar.addActionListener(this);
        this.acessoP.btnContinuar.addActionListener(this);
        this.acessoP.btnExcluir.addActionListener(this);
        this.acessoP.cmbOpcao.addActionListener(this);
    }

    public void iniciar() {
        acessoP.setTitle("Pesquisar Acessos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = acessoP.txtBuscar.getText();
        int cmbBusca = acessoP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == acessoP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                acessoD.buscar(acessoP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == acessoP.btnContinuar) {
            
            FuncionarioModel funcM = new FuncionarioModel();
            int linha = acessoP.tblAcesso.getSelectedRow();

            if (linha > -1) {
                String usuario = (String) acessoP.tblAcesso.getValueAt(linha, 1);
                acessoM.setLogin(usuario);
                if (acessoD.buscarSelecionado(acessoM, funcM)) {
                    acessoP.dispose();
                    AcessoView acessoV = new AcessoView();
                    
                    acessoV.txtFuncionario.setText(funcM.getNome());
                    acessoV.txtUsuario.setText(acessoM.getLogin());
                    acessoV.pswSenha.setText(acessoM.getSenha());
                    acessoV.pswConfirma.setText(acessoM.getSenha());
                    acessoV.cmbStatus.setSelectedIndex(acessoM.getAtivo());
                    acessoV.cmbTipo.setSelectedIndex(acessoM.getTipo());
                    acessoV.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == acessoP.btnExcluir) {

            int linha = acessoP.tblAcesso.getSelectedRow();
            
            if (linha > -1) {
                String usuario = (String) acessoP.tblAcesso.getValueAt(linha, 1);
                acessoM.setLogin(usuario);
                if (acessoD.excluir(acessoM)) {
                    JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                    acessoP.btnBuscar.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == acessoP.cmbOpcao) {
            if (acessoP.cmbOpcao.getSelectedIndex() != 0) {
                acessoP.txtBuscar.setEnabled(true);
            } else {
                acessoP.txtBuscar.setEnabled(false);
                acessoP.txtBuscar.setText(null);
            }
        }

    }
}
