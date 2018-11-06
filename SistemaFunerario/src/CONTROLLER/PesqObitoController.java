/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ObitoDAO;
import MODEL.ContaModel;
import MODEL.ObitoModel;
import MODEL.TitularModel;
import VIEW.ObitosView;
import VIEW.PesqObitoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqObitoController implements ActionListener {

    private PesqObitoView obitoP;
    private ObitoDAO obitoD;
    private ObitoModel obitoM;
    private ContaModel contaM;
    private TitularModel titularM;

    public PesqObitoController(PesqObitoView obitoP, ObitoDAO obitoD, ObitoModel obitoM, ContaModel contaM, TitularModel titularM) {
        this.obitoP = obitoP;
        this.obitoD = obitoD;
        this.obitoM = obitoM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.obitoP.btnBuscar.addActionListener(this);
        this.obitoP.btnContinuar.addActionListener(this);
    //    this.obitoP.btnExcluir.addActionListener(this);
        this.obitoP.cmbOpcao.addActionListener(this);
        this.obitoP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        obitoP.setTitle("Pesquisar Obitos");
        obitoP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = obitoP.txtBuscar.getText();
        int cmbBusca = obitoP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == obitoP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                obitoD.buscar(obitoP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == obitoP.btnContinuar) {

            int linha = obitoP.tblObitos.getSelectedRow();

            if (linha > -1) {
                int codigo = (int) obitoP.tblObitos.getValueAt(linha, 0);
                obitoM.setCodigo(codigo);
                if (obitoD.buscarSelecionado(contaM, titularM, obitoM)) {
                        obitoP.dispose();
                        ObitosView obitoV = new ObitosView();
                        if(!obitoD.buscarPessoas(contaM, obitoM, obitoV)){
                            JOptionPane.showMessageDialog(null, "Erro ao buscar pessoas!");
                        }
                        obitoV.txtCodConta.setText(String.valueOf(contaM.getCodigo()));
                        obitoV.txtCodObito.setText(String.valueOf(obitoM.getCodigo()));
                        obitoV.txtNome.setText(titularM.getNome());
                        obitoV.cmbFalecido.removeItem("Selecione");
                        obitoV.cmbFalecido.setEditable(false);
                        obitoV.txtDataEnt.setText(obitoM.getDtEnt());
                        obitoV.txtDataObt.setText(obitoM.getDtObito());
                        obitoV.txtDataVel.setText(obitoM.getDtVel());
                        obitoV.txtHoraEnt.setText(obitoM.getHoraEnt());
                        obitoV.txtHoraObt.setText(obitoM.getHoraObito());
                        obitoV.txtHoraVel.setText(obitoM.getHoraVel());
                        obitoV.txtLocalEnt.setText(obitoM.getLocalEnt());
                        obitoV.txtLocalObt.setText(obitoM.getLocalObito());
                        obitoV.txtLocalVel.setText(obitoM.getLocalVel());
                        obitoV.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

//        if (e.getSource() == obitoP.btnExcluir) {
//            Object[] options = {"Sim", "Não"};
//            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//            if (resposta == JOptionPane.YES_OPTION) {
//                int linha = obitoP.tblObitos.getSelectedRow();
//
//                if (linha > -1) {
//                    int codigo = (int) obitoP.tblObitos.getValueAt(linha, 0);
//                    obitoM.setCodigo(codigo);
//                    if (obitoD.excluir(obitoM)) {
//                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
//                        obitoP.btnBuscar.doClick();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
//                }
//            }
//        }

        if (e.getSource() == obitoP.cmbOpcao) {
            if (obitoP.cmbOpcao.getSelectedIndex() != 0) {
                obitoP.txtBuscar.setEnabled(true);
            } else {
                obitoP.txtBuscar.setEnabled(false);
                obitoP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == obitoP.btnVoltar) {
                ObitosView obitoV = new ObitosView();
                obitoV.setVisible(true);
                obitoP.dispose();
        }

    }
}
