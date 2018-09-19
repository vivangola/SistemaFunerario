/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.PlanosDAO;
import MODEL.ContaModel;
import MODEL.PlanosModel;
import VIEW.ContaView;
import VIEW.PlanosView;
import VIEW.PesqPlanosView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqPlanoController implements ActionListener {

    private PesqPlanosView planoP;
    private PlanosDAO planoD;
    private PlanosModel planoM;
    private ContaModel contaM;
    private int tela;

    public PesqPlanoController(PesqPlanosView planoP, PlanosDAO planoD, PlanosModel planoM, int tela, ContaModel contaM) {
        this.planoP = planoP;
        this.planoD = planoD;
        this.planoM = planoM;
        this.contaM = contaM;
        this.tela = tela;
        this.planoP.btnBuscar.addActionListener(this);
        this.planoP.btnContinuar.addActionListener(this);
        this.planoP.btnExcluir.addActionListener(this);
        this.planoP.cmbOpcao.addActionListener(this);
        this.planoP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        planoP.setTitle("Pesquisar Planos");
        planoP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = planoP.txtBuscar.getText();
        int cmbBusca = planoP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == planoP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                planoD.buscar(planoP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == planoP.btnContinuar) {

            int linha = planoP.tblPlanos.getSelectedRow();

            if (linha > -1) {
                int codigo = (int) planoP.tblPlanos.getValueAt(linha, 0);
                planoM.setCodigo(codigo);
                if (planoD.buscarSelecionado(planoM)) {
                    planoP.dispose();
                    if (tela == 1) {
                        PlanosView planoV = new PlanosView();

                        planoV.txtCodigo.setText(String.valueOf(planoM.getCodigo()));
                        planoV.txtNome.setText(planoM.getNome());
                        planoV.txtDependentes.setText(String.valueOf(planoM.getQtdDependente()));
                        planoV.txtMensalidade.setText(String.valueOf(planoM.getMensalidade()));
                        planoV.cmbCarencia.setSelectedIndex(planoM.getCarencia());
                        planoV.setVisible(true);
                    } else {
                        ContaView contaV = new ContaView();
                        contaV.txtCodPlano.setText(String.valueOf(planoM.getCodigo()));
                        contaV.txtPlano.setText(planoM.getNome());
                        contaV.txtQtdDepend.setText(String.valueOf(planoM.getQtdDependente()));
                        contaV.txtMensalidade.setText(String.valueOf(planoM.getMensalidade()));
                        contaV.txtCarencia.setText(String.valueOf(planoM.getCarencia()));
                        contaV.setVisible(true);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == planoP.btnExcluir) {
            Object[] options = {"Sim", "Não"};
            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (resposta == JOptionPane.YES_OPTION) {
                int linha = planoP.tblPlanos.getSelectedRow();

                if (linha > -1) {
                    int codigo = (int) planoP.tblPlanos.getValueAt(linha, 0);
                    planoM.setCodigo(codigo);
                    if (planoD.excluir(planoM)) {
                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                        planoP.btnBuscar.doClick();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
                }
            }
        }

        if (e.getSource() == planoP.cmbOpcao) {
            if (planoP.cmbOpcao.getSelectedIndex() != 0) {
                planoP.txtBuscar.setEnabled(true);
            } else {
                planoP.txtBuscar.setEnabled(false);
                planoP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == planoP.btnVoltar) {
            if (tela == 1) {
                PlanosView planoV = new PlanosView();
                planoV.setVisible(true);
                planoP.dispose();
            } else {
                ContaView contaV = new ContaView();
                contaV.setVisible(true);
                planoP.dispose();
            }
        }

    }
}
