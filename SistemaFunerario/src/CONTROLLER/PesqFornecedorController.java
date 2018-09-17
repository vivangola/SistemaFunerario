/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.FornecedorDAO;
import MODEL.FornecedorModel;
import VIEW.AcessoView;
import VIEW.FornecedorView;
import VIEW.PesqFornecedorView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqFornecedorController implements ActionListener {

    private PesqFornecedorView forncP;
    private FornecedorDAO forncD;
    private FornecedorModel forncM;
    private int tela;

    public PesqFornecedorController(PesqFornecedorView forncP, FornecedorDAO forncD, FornecedorModel forncM, int tela) {
        this.forncP = forncP;
        this.forncD = forncD;
        this.forncM = forncM;
        this.tela = tela;
        this.forncP.btnBuscar.addActionListener(this);
        this.forncP.btnContinuar.addActionListener(this);
        this.forncP.btnExcluir.addActionListener(this);
        this.forncP.btnVoltar.addActionListener(this);
        this.forncP.cmbOpcao.addActionListener(this);
    }

    public void iniciar() {
        forncP.setTitle("Pesquisar Fornecedores");
        forncP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = forncP.txtBuscar.getText();
        int cmbBusca = forncP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == forncP.btnBuscar) {
            if (!((!forncP.txtBuscar.isEditValid() || txtBusca.isEmpty()) && cmbBusca != 0)) {
                forncD.buscar(forncP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == forncP.btnContinuar) {
            int linha = forncP.tblFornecedor.getSelectedRow();

            if (linha > -1) {
                String cnpj = (String) forncP.tblFornecedor.getValueAt(linha, 1);
                forncM.setCnpj(cnpj);
                if (forncD.buscarSelecionado(forncM)) {
                    forncP.dispose();
                    if (tela == 0) {
//                        MaterialView mateV = new MaterialView(forncM);
//                        mateV.txtFornecedor.setText(forncM.getNome());
//                        mateV.setVisible(true);
                    } else {
                        FornecedorView forncV = new FornecedorView();
                        forncV.txtNome.setText(forncM.getNome());
                        forncV.txtCNPJ.setText(forncM.getCnpj());
                        forncV.txtEndereco.setText(forncM.getEndereco());
                        forncV.txtBairro.setText(forncM.getBairro());
                        forncV.txtCidade.setText(forncM.getCidade());
                        forncV.txtCEP.setText(forncM.getCep());
                        forncV.txtTelefone.setText(forncM.getTelefone());
                        forncV.cmbEstado.setSelectedItem(forncM.getEstado());
                        forncV.txtEmail.setText(forncM.getEmail());
                        forncV.txtInscricao.setText(forncM.getInscricaoEstadual());
                        forncV.setVisible(true);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == forncP.btnExcluir) {
            Object[] options = {"Sim","Não"};
            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?","Alerta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
            if (resposta == JOptionPane.YES_OPTION) {
                int linha = forncP.tblFornecedor.getSelectedRow();

                if (linha > -1) {
                    String cnpj = (String) forncP.tblFornecedor.getValueAt(linha, 1);
                    forncM.setCnpj(cnpj);

                    if (forncD.excluir(forncM)) {
                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                        forncP.btnBuscar.doClick();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
                }
            }

        }

        if (e.getSource() == forncP.cmbOpcao) {
            forncP.txtBuscar.setValue(null);
            forncP.txtBuscar.setText(null);
            if (forncP.cmbOpcao.getSelectedIndex() != 0) {
                forncP.txtBuscar.setEnabled(true);
                if (forncP.cmbOpcao.getSelectedIndex() == 1) {
                    try {
                        forncP.txtBuscar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PesqFornecedorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    forncP.txtBuscar.setFormatterFactory(null);
                    forncP.txtBuscar.setText(null);
                }
            } else {
                forncP.txtBuscar.setEnabled(false);
                forncP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == forncP.btnVoltar) {
            if (tela == 0) {
                AcessoView acessoV = new AcessoView();
                acessoV.setVisible(true);
                forncP.dispose();
            } else {
                FornecedorView forncV = new FornecedorView();
                forncV.setVisible(true);
                forncP.dispose();
            }
        }

    }
}
