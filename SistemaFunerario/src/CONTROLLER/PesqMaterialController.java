/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MaterialDAO;
import MODEL.ContaModel;
import MODEL.EmprestimoModel;
import MODEL.MaterialModel;
import MODEL.TitularModel;
import VIEW.ControleEstoqueView;
import VIEW.EmprestimoView;
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
    private ContaModel contaM;
    private TitularModel titularM;
    private EmprestimoModel emprestM;
    private int tela;

    public PesqMaterialController(PesqMaterialView materialP, MaterialDAO materialD, MaterialModel materialM, ContaModel contaM, TitularModel titularM, EmprestimoModel emprestM, int tela) {
        this.materialP = materialP;
        this.materialD = materialD;
        this.materialM = materialM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.emprestM = emprestM;
        this.tela = tela;
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
                int codigo = (int) materialP.tblMaterial.getValueAt(linha, 0);
                materialM.setCodigo(codigo);
                if (materialD.buscarSelecionado(materialM)) {
                    materialP.dispose();
                    if (tela == 1) {
                        MaterialView materialV = new MaterialView();

                        materialV.txtCodigo.setText(String.valueOf(materialM.getCodigo()));
                        materialV.txtNome.setText(materialM.getNome());
                        materialV.txtModelo.setText(materialM.getModelo());
                        materialV.txtTamanho.setText(String.valueOf(materialM.getTamanho()));
                        materialV.txtMinimo.setText(String.valueOf(materialM.getQtdMinima()));
                        materialV.cmbCategoria.setSelectedIndex(materialM.getCategoria());
                        materialV.btnAlterar.setEnabled(true);
                        materialV.setVisible(true);
                    } else if(tela == 2){
                        EmprestimoView emprestV = new EmprestimoView(emprestM,contaM,titularM);
                        emprestV.txtCodConta.setText(String.valueOf(contaM.getCodigo()));
                        emprestV.txtNome.setText(titularM.getNome());
                        emprestV.txtCodMaterial.setText(String.valueOf(materialM.getCodigo()));
                        emprestV.txtMaterial.setText(materialM.getNome());
                        emprestV.txtModelo.setText(materialM.getModelo());
                        emprestV.txtEstoque.setText(String.valueOf(materialM.getEstoque()));
                        emprestV.txtTamanho.setText(String.valueOf(materialM.getTamanho()));
                        emprestV.cmbOperacao.setSelectedItem(String.valueOf(emprestM.getOperacao()));
                        emprestV.txtEntra.setText(emprestM.getDataEntrada());
                        emprestV.setVisible(true);
                    }else {
                        ControleEstoqueView estoqueV = new ControleEstoqueView(materialM);

                        estoqueV.txtCodigo.setText(String.valueOf(materialM.getCodigo()));
                        estoqueV.txtMaterial.setText(String.valueOf(materialM.getNome()));
                        estoqueV.txtEstoque.setText(String.valueOf(materialM.getEstoque()));
                        if (materialM.getEstoque() < materialM.getQtdMinima()) {
                            JOptionPane.showMessageDialog(null, "Atenção seu estoque está abaixo do mínimo!");
                        }
                        estoqueV.setVisible(true);
                    }
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

        if (e.getSource() == materialP.btnVoltar) {
            if (tela == 1) {
                MaterialView materialV = new MaterialView();
                materialV.setVisible(true);
                materialP.dispose();
            } else if(tela == 2){
                EmprestimoView emprestV = new EmprestimoView();
                emprestV.setVisible(true);
                materialP.dispose();
            }else {
                ControleEstoqueView estoqueV = new ControleEstoqueView();
                estoqueV.setVisible(true);
                materialP.dispose();
            }
        }

    }
}
