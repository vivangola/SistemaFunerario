/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.EmprestimoDAO;
import MODEL.ContaModel;
import MODEL.EmprestimoModel;
import MODEL.MaterialModel;
import MODEL.TitularModel;
import VIEW.ControleEstoqueView;
import VIEW.EmprestimoView;
import VIEW.PesqEmprestimoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqEmprestimoController implements ActionListener {

    private PesqEmprestimoView emprestP;
    private EmprestimoDAO emprestD;
    private MaterialModel materialM;
    private ContaModel contaM;
    private TitularModel titularM;
    private EmprestimoModel emprestM;

    public PesqEmprestimoController(PesqEmprestimoView emprestP, EmprestimoDAO emprestD, MaterialModel materialM, ContaModel contaM, TitularModel titularM, EmprestimoModel emprestM) {
        this.emprestP = emprestP;
        this.emprestD = emprestD;
        this.materialM = materialM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.emprestM = emprestM;
        this.emprestP.btnBuscar.addActionListener(this);
        this.emprestP.btnContinuar.addActionListener(this);
        this.emprestP.btnExcluir.addActionListener(this);
        this.emprestP.cmbOpcao.addActionListener(this);
        this.emprestP.btnVoltar.addActionListener(this);
    }
    
    public void iniciar() {
        emprestP.setTitle("Pesquisar Empréstimos");
        emprestP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = emprestP.txtBuscar.getText();
        int cmbBusca = emprestP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == emprestP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                emprestD.buscar(emprestP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == emprestP.btnContinuar) {

            int linha = emprestP.tblEmprest.getSelectedRow();

            if (linha > -1) {
                int codigo = (int) emprestP.tblEmprest.getValueAt(linha, 0);
                emprestM.setCodigo(codigo);
                if (emprestD.buscarSelecionado(emprestM, titularM, materialM)) {
                    emprestP.dispose();
                        EmprestimoView emprestV = new EmprestimoView();
                        emprestV.txtCodEmp.setText(String.valueOf(emprestM.getCodigo()));
                        emprestV.txtEntra.setText(emprestM.getDataEntrada());
                        emprestV.txtQuantidade.setText(String.valueOf(emprestM.getQuantidade()));
                        emprestV.txtOriginal.setText(String.valueOf(emprestM.getQuantidade()));
                        emprestV.cmbOperacao.removeItemAt(0);
                        emprestV.cmbOperacao.addItem("Devolução");
                        emprestV.txtCodConta.setText(String.valueOf(titularM.getFk_conta()));
                        emprestV.txtNome.setText(titularM.getNome());
                        emprestV.txtCodMaterial.setText(String.valueOf(materialM.getCodigo()));
                        emprestV.txtMaterial.setText(String.valueOf(materialM.getNome()));
                        emprestV.txtEstoque.setText(String.valueOf(materialM.getEstoque()));
                        emprestV.txtModelo.setText(materialM.getModelo());
                        emprestV.txtTamanho.setText(String.valueOf(materialM.getTamanho()));
                        emprestV.txtdevolv.setEnabled(true);
                        java.util.Date d = new Date();
                        emprestV.txtdevolv.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
                        emprestV.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == emprestP.cmbOpcao) {
            if (emprestP.cmbOpcao.getSelectedIndex() != 0) {
                emprestP.txtBuscar.setEnabled(true);
            } else {
                emprestP.txtBuscar.setEnabled(false);
                emprestP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == emprestP.btnVoltar) {
                EmprestimoView emprestV = new EmprestimoView();
                emprestV.setVisible(true);
                emprestP.dispose();
            
        }

    }
}
