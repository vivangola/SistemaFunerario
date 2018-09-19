/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ContaDAO;
import MODEL.ContaModel;
import VIEW.AcessoView;
import VIEW.ContaView;
import VIEW.PesqContaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqContaController implements ActionListener {

    private PesqContaView contaP;
    private ContaDAO contaD;
    private ContaModel contaM;

    public PesqContaController(PesqContaView contaP, ContaDAO contaD, ContaModel contaM) {
        this.contaP = contaP;
        this.contaD = contaD;
        this.contaM = contaM;
        this.contaP.btnBuscar.addActionListener(this);
        this.contaP.btnContinuar.addActionListener(this);
        this.contaP.btnVoltar.addActionListener(this);
        this.contaP.cmbOpcao.addActionListener(this);
    }

    public void iniciar() {
        contaP.setTitle("Pesquisar Funcionários");
        contaP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = contaP.txtBuscar.getText();
        int cmbBusca = contaP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == contaP.btnBuscar) {
            if (!((!contaP.txtBuscar.isEditValid() || txtBusca.isEmpty()) && cmbBusca != 0)) {
                contaD.buscar(contaP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

//        if (e.getSource() == contaP.btnContinuar) {
//            AcessoModel acessoM = new AcessoModel();
//            int linha = contaP.tblConta.getSelectedRow();
//
//            if (linha > -1) {
//                int codigo = (int) contaP.tblConta.getValueAt(linha, 0);
//                contaM.setCodigo(codigo);
//                if (contaD.buscarSelecionado(contaM)) {
//                    contaP.dispose();
//                        ContasView contaV = new ContasView();
//                        contaV.txtNome.setText(contaM.getNome());
//                        contaV.txtCPF.setText(contaM.getCpf());
//                        contaV.txtRG.setText(contaM.getRg());
//                        contaV.txtEndereco.setText(contaM.getEndereco());
//                        contaV.txtBairro.setText(contaM.getBairro());
//                        contaV.txtCidade.setText(contaM.getCidade());
//                        contaV.txtCEP.setText(contaM.getCep());
//                        String data = contaM.getNascimento();
//                        contaV.txtTelefone.setText(contaM.getTelefone());
//                        contaV.cmbCargo.setSelectedItem(contaM.getCargo());
//                        contaV.cmbCivil.setSelectedItem(contaM.getEstadoCivil());
//                        contaV.cmbEstado.setSelectedItem(contaM.getEstado());
//                        if (contaM.getSexo() == "M") {
//                            contaV.cmbSexo.setSelectedItem("Masculinho");
//                        } else {
//                            contaV.cmbSexo.setSelectedItem("Feminino");
//                        }
//                        String nascDia = data.substring(8);
//                        String nascMes = data.substring(5, 7);
//                        String nascAno = data.substring(0, 4);
//                        String nascimento = nascDia + "/" + nascMes + "/" + nascAno;
//                        contaV.txtNascimento.setText(nascimento);
//                        contaV.setVisible(true);
//                    
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
//            }
//
//        }
//
//        if (e.getSource() == contaP.btnExcluir) {
//            Object[] options = {"Sim","Não"};
//            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?","Alerta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
//            if (resposta == JOptionPane.YES_OPTION) {
//                int linha = contaP.tblConta.getSelectedRow();
//
//                if (linha > -1) {
//                    String cpf = (String) contaP.tblConta.getValueAt(linha, 2);
//                    contaM.setCpf(cpf);
//
//                    if (contaD.excluir(contaM)) {
//                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
//                        contaP.btnBuscar.doClick();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
//                }
//            }
//
//        }

        if (e.getSource() == contaP.cmbOpcao) {
            contaP.txtBuscar.setValue(null);
            contaP.txtBuscar.setText(null);
            if (contaP.cmbOpcao.getSelectedIndex() != 0) {
                contaP.txtBuscar.setEnabled(true);
                if (contaP.cmbOpcao.getSelectedIndex() == 1) {
                    try {
                        contaP.txtBuscar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PesqContaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    contaP.txtBuscar.setFormatterFactory(null);
                    contaP.txtBuscar.setText(null);
                }
            } else {
                contaP.txtBuscar.setEnabled(false);
                contaP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == contaP.btnVoltar) {
                ContaView contaV = new ContaView();
                contaV.setVisible(true);
                contaP.dispose();
            
        }

    }
}
