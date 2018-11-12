/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ContaDAO;
import DAO.EmprestimoDAO;
import DAO.MaterialDAO;
import MODEL.ContaModel;
import MODEL.EmprestimoModel;
import MODEL.MaterialModel;
import MODEL.TitularModel;
import VIEW.EmprestimoView;
import VIEW.MenuView;
import VIEW.PesqContaView;
import VIEW.PesqEmprestimoView;
import VIEW.PesqMaterialView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class EmprestimoController implements ActionListener {

    private EmprestimoView emprestV;
    private EmprestimoDAO emprestD;
    private EmprestimoModel emprestM;
    private ContaModel contaM;
    private TitularModel titularM;
    private MaterialDAO materialD;
    private MaterialModel materialM;

    public EmprestimoController(EmprestimoView emprestV, EmprestimoDAO emprestD, EmprestimoModel emprestM, ContaModel contaM, TitularModel titularM, MaterialDAO materialD, MaterialModel materialM) {
        this.emprestV = emprestV;
        this.emprestM = emprestM;
        this.emprestD = emprestD;
        this.contaM = contaM;
        this.titularM = titularM;
        this.materialM = materialM;
        this.materialD = materialD;
        this.emprestV.btnConcluir.addActionListener(this);
        this.emprestV.btnPesqConta.addActionListener(this);
        this.emprestV.btnPesqEmp.addActionListener(this);
        this.emprestV.btnPesqMate.addActionListener(this);
        this.emprestV.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        emprestV.setTitle("Emprestimos/Devoluções");
        emprestV.txtQuantidade.setDocument(new NumericoController());
        java.util.Date d = new Date();
        if (emprestD.buscarCodigo(emprestM)) {
            emprestV.txtCodEmp.setText(String.valueOf(emprestM.getCodigo()));
            emprestV.txtEntra.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
            emprestV.cmbOperacao.removeAllItems();
            emprestV.cmbOperacao.addItem("Empréstimo");
        } else {
            emprestD.inserirEmprest();
            iniciar();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int codigo = 0;
        int codMaterial = materialM.getCodigo();
        int codConta = contaM.getCodigo();
        int quantidade = 0;
        int estoque = 0;
        String devolvSQL = null;
        String operacao = String.valueOf(emprestV.cmbOperacao.getSelectedItem());
        String entrada = emprestV.txtEntra.getText();
        String entradaSQL = setDataSql(entrada);

        String retorno = null;
        if (!"".equals(emprestV.txtCodEmp.getText().trim())) {
            codigo = Integer.valueOf(emprestV.txtCodEmp.getText());
        }
        if (!"".equals(emprestV.txtCodMaterial.getText().trim())) {
            codMaterial = Integer.valueOf(emprestV.txtCodMaterial.getText());
        }
        if (!"".equals(emprestV.txtCodConta.getText().trim())) {
            codConta = Integer.valueOf(emprestV.txtCodConta.getText());
        }
        if (!"".equals(emprestV.txtQuantidade.getText().trim())) {
            quantidade = Integer.parseInt(emprestV.txtQuantidade.getText());
        }
        if (!"".equals(emprestV.txtEstoque.getText().trim())) {
            estoque = Integer.parseInt(emprestV.txtEstoque.getText());
        }

        if (e.getSource() == emprestV.btnConcluir) {
            //  retorno = validarCampos(nome, modelo, categoria, qtdMinima, tamanho);
            if (retorno == null) {

                emprestM.setCodigo(codigo);
                emprestM.setQuantidade(quantidade);
                emprestM.setDataEntrada(entradaSQL);
                emprestM.setOperacao(operacao);
                contaM.setCodigo(codConta);
                materialM.setCodigo(codMaterial);

                if ("Empréstimo".equals(operacao)) {
                    if (quantidade <= estoque) {
                        if (emprestD.incluir(emprestM, contaM, materialM)) {
                            estoque = estoque - quantidade;
                            JOptionPane.showMessageDialog(null, "Empréstimo registrado com sucesso!");

                            materialM.setCodigo(codMaterial);
                            materialM.setEstoque(estoque);

                            if (materialD.atualizarEstoque(materialM)) {
                                JOptionPane.showMessageDialog(null, "Estoque Atualizado!");
                                limparCampos();
                                iniciar();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao registrar empréstimo!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Estoque indisponível!");
                    }
                } else {
                    String devolv = emprestV.txtdevolv.getText();
                    devolvSQL = setDataSql(devolv);
                    emprestM.setDataDevolv(devolvSQL);

                    if (emprestD.devolucao(emprestM)) {
                        estoque = estoque + quantidade;
                        JOptionPane.showMessageDialog(null, "Devolução registrado com sucesso!");

                        materialM.setCodigo(codMaterial);
                        materialM.setEstoque(estoque);

                        if (materialD.atualizarEstoque(materialM)) {
                            JOptionPane.showMessageDialog(null, "Estoque Atualizado!");
                            limparCampos();
                            iniciar();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, retorno);
                    }
                }
            }
        }

//        if (e.getSource() == emprestV.btnAlterar) {
//            retorno = validarCampos(nome, modelo, categoria, qtdMinima, tamanho);
//            if (retorno == null) {
//                Object[] options = {"Sim", "Não"};
//                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//                if (resposta == JOptionPane.YES_OPTION) {
//                    materialM.setCodigo(codigo);
//                    materialM.setNome(nome);
//                    materialM.setModelo(modelo);
//                    materialM.setTamanho(tamanho);
//                    materialM.setQtdMinima(qtdMinima);
//                    materialM.setCategoria(categoria);
//
//                    if (materialD.alterar(materialM)) {
//                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
//                        limparCampos();
//                        iniciar();
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, retorno);
//            }
//        }
        if (e.getSource() == emprestV.btnPesqConta) {
            PesqContaView contaP = new PesqContaView(2);
            contaP.setVisible(true);
            emprestV.dispose();
        }

        if (e.getSource() == emprestV.btnPesqEmp) {
            PesqEmprestimoView emprestP = new PesqEmprestimoView();
            emprestP.setVisible(true);
            emprestV.dispose();
        }

        if (e.getSource() == emprestV.btnPesqMate) {

            emprestM.setCodigo(codigo);
            emprestM.setDataEntrada(entrada);
            emprestM.setOperacao(operacao);
            contaM.setCodigo(codConta);
            materialM.setCodigo(codMaterial);

            PesqMaterialView materialP = new PesqMaterialView(2, contaM, titularM, emprestM);
            materialP.setVisible(true);
            emprestV.dispose();
        }

        if (e.getSource() == emprestV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            emprestV.dispose();
        }

    }

    public void limparCampos() {
        emprestV.txtNome.setText(null);
        emprestV.txtCodEmp.setText(null);
        emprestV.txtCodConta.setText(null);
        emprestV.txtCodMaterial.setText(null);
        emprestV.txtEntra.setText(null);
        emprestV.txtdevolv.setText(null);
        emprestV.txtEstoque.setText(null);
        emprestV.txtMaterial.setText(null);
        emprestV.txtQuantidade.setText(null);
        emprestV.txtModelo.setText(null);
        emprestV.txtTamanho.setText(null);
        emprestV.cmbOperacao.setSelectedIndex(0);
    }

    public String validarCampos(String nome, String modelo, int categoria, int qtdMinima, double tamanho) {
        String msg = null;
        if (nome.isEmpty() || modelo.isEmpty() || categoria == 0 || qtdMinima == 0 || tamanho == 0) {
            msg = "Por favor preencha todos os campos!";
        }
        return msg;
    }

    public String setDataSql(String data) {
        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6);
        String dataSQL = ano + "-" + mes + "-" + dia;
        return dataSQL;
    }

}
