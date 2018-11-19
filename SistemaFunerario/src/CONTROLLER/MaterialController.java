/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MaterialDAO;
import MODEL.MaterialModel;
import VIEW.MaterialView;
import VIEW.MenuView;
import VIEW.PesqMaterialView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class MaterialController implements ActionListener {

    private MaterialView materialV;
    private MaterialModel materialM;
    private MaterialDAO materialD;

    public MaterialController(MaterialView materialV, MaterialModel materialM, MaterialDAO materialD) {
        this.materialV = materialV;
        this.materialM = materialM;
        this.materialD = materialD;
        this.materialV.btnIncluir.addActionListener(this);
        this.materialV.btnAlterar.addActionListener(this);
        this.materialV.btnPesqMate.addActionListener(this);
        this.materialV.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        materialV.setTitle("Materials");
        materialV.txtMinimo.setDocument(new NumericoController());
        materialV.btnAlterar.setEnabled(false);
        if (materialD.buscarCodigo(materialM)) {
            materialV.txtCodigo.setText(String.valueOf(materialM.getCodigo()));
        } else {
            materialD.inserirMaterial();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int codigo = Integer.valueOf(materialV.txtCodigo.getText());
        String nome = materialV.txtNome.getText();
        String modelo = materialV.txtModelo.getText();
        int categoria = materialV.cmbCategoria.getSelectedIndex();
        int qtdMinima = 0;
        String tamanho = materialV.txtTamanho.getText();
        String retorno;
        if (!"".equals(materialV.txtMinimo.getText().trim()) || !"".equals(materialV.txtTamanho.getText().trim())) {
            qtdMinima = Integer.valueOf(materialV.txtMinimo.getText());
        }

        if (e.getSource() == materialV.btnIncluir) {
            retorno = validarCampos(nome, modelo, categoria, qtdMinima, tamanho);
            if (retorno == null) {
                materialM.setCodigo(codigo);
                materialM.setNome(nome);
                materialM.setModelo(modelo);
                materialM.setTamanho(tamanho);
                materialM.setQtdMinima(qtdMinima);
                materialM.setCategoria(categoria);
                materialM.setEstoque(0);

                if (materialD.incluir(materialM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                    iniciar();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == materialV.btnAlterar) {
            retorno = validarCampos(nome, modelo, categoria, qtdMinima, tamanho);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {
                    materialM.setCodigo(codigo);
                    materialM.setNome(nome);
                    materialM.setModelo(modelo);
                    materialM.setTamanho(tamanho);
                    materialM.setQtdMinima(qtdMinima);
                    materialM.setCategoria(categoria);

                    if (materialD.alterar(materialM)) {
                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                        limparCampos();
                        iniciar();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == materialV.btnPesqMate) {
            PesqMaterialView materialP = new PesqMaterialView();
            materialP.setVisible(true);
            materialV.dispose();
        }

        if (e.getSource() == materialV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            materialV.dispose();
        }

    }

    public void limparCampos() {
        materialV.txtNome.setText(null);
        materialV.txtCodigo.setText(null);
        materialV.txtMinimo.setText(null);
        materialV.txtModelo.setText(null);
        materialV.txtTamanho.setText(null);
        materialV.cmbCategoria.setSelectedIndex(0);
    }

    public String validarCampos(String nome, String modelo, int categoria, int qtdMinima, String tamanho) {
        String msg = null;
        if (nome.isEmpty() || modelo.isEmpty() || categoria == 0 || qtdMinima == 0 || tamanho.isEmpty()) {
            msg = "Por favor preencha todos os campos!";
        }
        return msg;
    }

}
