/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.AcessoDAO;
import MODEL.AcessoModel;
import VIEW.AcessoView;
import VIEW.PesqAcessoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class AcessoController implements ActionListener {

    private AcessoView acessoV;
    private PesqAcessoView acessoP;
    private AcessoModel acessoM;
    private AcessoDAO acessoD;

    public AcessoController(AcessoView acessoV, AcessoModel acessoM, AcessoDAO acessoD) {
        this.acessoV = acessoV;
        this.acessoM = acessoM;
        this.acessoD = acessoD;
        this.acessoV.btnIncluir.addActionListener(this);
        this.acessoV.btnAlterar.addActionListener(this);
        this.acessoV.btnExcluir.addActionListener(this);
    }

    public void iniciar() {
        acessoV.setTitle("Acessos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String usuario = acessoV.txtUsuario.getText();
        String psw1 = acessoV.pswSenha.getText();
        String psw2 = acessoV.pswConfirma.getText();
        int tipo = acessoV.cmbTipo.getSelectedIndex();
        int status = acessoV.cmbStatus.getSelectedIndex();
        String retorno;

        if (e.getSource() == acessoV.btnIncluir) {
            retorno = validarCampos(psw1, psw2, tipo, status);
            if (retorno == null) {
                acessoM.setLogin(usuario);
                acessoM.setSenha(psw1);
                if (tipo == 1) {
                    acessoM.setTipo(1);
                } else if (tipo == 2) {
                    acessoM.setTipo(2);
                }
                if (status == 1) {
                    acessoM.setAtivo(1);
                } else if (status == 2) {
                    acessoM.setAtivo(2);
                }

                if (acessoD.incluir(acessoM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == acessoV.btnAlterar) {

            retorno = validarCampos(psw1, psw2, tipo, status);
            if (retorno == null) {
                acessoM.setLogin(usuario);
                acessoM.setSenha(psw1);

                if (tipo == 1) {
                    acessoM.setTipo(1);
                } else if (tipo == 2) {
                    acessoM.setTipo(2);
                }
                if (status == 1) {
                    acessoM.setAtivo(1);
                } else if (status == 2) {
                    acessoM.setAtivo(2);
                }

                if (acessoD.alterar(acessoM)) {
                    JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == acessoV.btnExcluir) {

            retorno = validarCampos(psw1, psw2, tipo, status);
            if (retorno == null) {
                acessoM.setLogin(usuario);
                if (acessoD.excluir(acessoM)) {
                    JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                    limparCampos();
                }

            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }
    }
    
    public void limparCampos() {
        acessoV.txtUsuario.setText(null);
        acessoV.pswSenha.setText(null);
        acessoV.pswConfirma.setText(null);
        acessoV.cmbTipo.setSelectedIndex(0);
    }

    public String validarCampos(String psw1, String psw2, int tipo, int status) {
        String msg = null;
        if (psw1.isEmpty() || psw2.isEmpty() || tipo == 0 || status == 0) {
            msg = "Por favor preencha todos os campos!";
        }
        if (!psw1.equals(psw2)) {
            msg = "Os campos de senha não conferem! Por favor tente novamente!";
        }
        return msg;
    }


}
