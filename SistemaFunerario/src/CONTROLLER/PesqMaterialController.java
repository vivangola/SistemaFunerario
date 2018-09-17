/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MaterialDAO;
import MODEL.MaterialModel;
import VIEW.MaterialView;
import VIEW.PesqMaterialView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqMaterialController implements ActionListener {

    private PesqMaterialView materialP;
    private MaterialDAO materialD;
    private MaterialModel materialM;

    public PesqMaterialController(PesqMaterialView materialP, MaterialDAO materialD, MaterialModel materialM) {
        this.materialP = materialP;
        this.materialD = materialD;
        this.materialM = materialM;
        this.materialP.btnBuscar.addActionListener(this);
        this.materialP.btnContinuar.addActionListener(this);
        this.materialP.btnExcluir.addActionListener(this);
        this.materialP.cmbOpcao.addActionListener(this);
        this.materialP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        materialP.setTitle("Pesquisar Materiais");
        materialP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = materialP.txtBuscar.getText();
        int cmbBusca = materialP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == materialP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                materialD.buscar(materialP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == materialP.btnContinuar) {

            int linha = materialP.tblMaterial.getSelectedRow();

            if (linha > -1) {
                String codigo = (String) materialP.tblMaterial.getValueAt(linha, 0);
                materialM.setCodigo(Integer.valueOf(codigo));
                if (materialD.buscarSelecionado(materialM)) {
                    materialP.dispose();
                    MaterialView materialV = new MaterialView();

                    materialV.txtCodigo.setText(String.valueOf(materialM.getCodigo()));
                    materialV.txtNome.setText(materialM.getNome());
                    materialV.txtModelo.setText(materialM.getModelo());
                    materialV.txtTamanho.setText(String.valueOf(materialM.getTamanho()));
                    materialV.txtMinimo.setText(String.valueOf(materialM.getQtdMinima()));
                    materialV.cmbCategoria.setSelectedIndex(materialM.getCategoria());
                    materialV.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == materialP.btnExcluir) {
            Object[] options = {"Sim", "Não"};
            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (resposta == JOptionPane.YES_OPTION) {
                int linha = materialP.tblMaterial.getSelectedRow();

                if (linha > -1) {
                    String codigo = (String) materialP.tblMaterial.getValueAt(linha, 0);
                    materialM.setCodigo(Integer.valueOf(codigo));
                    if (materialD.excluir(materialM)) {
                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                        materialP.btnBuscar.doClick();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
                }
            }
        }

        if (e.getSource() == materialP.cmbOpcao) {
            if (materialP.cmbOpcao.getSelectedIndex() != 0) {
                materialP.txtBuscar.setEnabled(true);
            } else {
                materialP.txtBuscar.setEnabled(false);
                materialP.txtBuscar.setText(null);
            }
        }
        
        if (e.getSource() == materialP.btnVoltar){
            MaterialView materialV = new MaterialView();
            materialV.setVisible(true);
            materialP.dispose();
        }

    }
}
