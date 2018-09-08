/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import MODEL.AcessoMetodos;
import MODEL.AcessoModel;
import VIEW.AcessoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class AcessoController implements ActionListener {

    private AcessoView acessoV;
    private AcessoModel acessoM;
    private AcessoMetodos metodos;

    public AcessoController(AcessoView acessoV, AcessoModel acessoM, AcessoMetodos metodos) {
        this.acessoV = acessoV;
        this.acessoM = acessoM;
        this.metodos = metodos;
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
        String retorno;

        if (e.getSource() == acessoV.btnIncluir) {
            retorno = metodos.validarCampos(psw1, psw2, tipo);
            if (retorno == null) {
                acessoM.setLogin(usuario);
                acessoM.setSenha(psw1);

                if (tipo == 1) {
                    acessoM.setTipo(1);
                } else if (tipo == 2) {
                    acessoM.setTipo(2);
                }

                if (metodos.incluir(acessoM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == acessoV.btnAlterar) {

            retorno = metodos.validarCampos(psw1, psw2, tipo);
            if (retorno == null) {
                acessoM.setLogin(usuario);
                acessoM.setSenha(psw1);
                
                if (tipo == 1) {
                    acessoM.setTipo(1);
                } else if (tipo == 2) {
                    acessoM.setTipo(2);
                }

                if (metodos.alterar(acessoM)) {
                    JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == acessoV.btnExcluir) {

            retorno = metodos.validarCampos(psw1, psw2, tipo);
            if (retorno == null) {
                acessoM.setLogin(usuario);
                if (metodos.excluir(acessoM)) {
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
}
