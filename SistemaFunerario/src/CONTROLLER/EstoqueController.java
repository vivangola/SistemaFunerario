/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MaterialDAO;
import MODEL.MaterialModel;
import VIEW.ControleEstoqueView;
import VIEW.MenuView;
import VIEW.PesqMaterialView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class EstoqueController implements ActionListener {

    private ControleEstoqueView estoqueV;
    private MaterialModel materialM;
    private MaterialDAO materialD;

    public EstoqueController(ControleEstoqueView estoqueV, MaterialModel materialM, MaterialDAO materialD) {
        this.estoqueV = estoqueV;
        this.materialM = materialM;
        this.materialD = materialD;
        this.estoqueV.btnConcluir.addActionListener(this);
        this.estoqueV.btnVoltar.addActionListener(this);
        this.estoqueV.btnBuscarMate.addActionListener(this);
    }

    public void iniciar() {
        estoqueV.setTitle("Controle de Estoque");
        estoqueV.txtQuantidade.setDocument(new NumericoController());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int codigo = 0;
        int quantidade = 0;
        int estoque = 0;
        int operacao = estoqueV.cmbOperacao.getSelectedIndex();
        String retorno = null;
        if (!"".equals(estoqueV.txtCodigo.getText().trim())) {
            codigo = Integer.valueOf(estoqueV.txtCodigo.getText());
        }
        if (!"".equals(estoqueV.txtQuantidade.getText().trim())) {
            quantidade = Integer.parseInt(estoqueV.txtQuantidade.getText());
        }
        if (!"".equals(estoqueV.txtEstoque.getText().trim())) {
            estoque = Integer.parseInt(estoqueV.txtEstoque.getText());
        }

        if (e.getSource() == estoqueV.btnConcluir) {
        //    retorno = validarCampos(nome, modelo, quantidade, qtdMinima, tamanho);
            if (retorno == null) {
                if(operacao == 1){
                    estoque = estoque + quantidade;
                }else if(operacao == 2){
                    estoque = estoque - quantidade;
                }
                
                materialM.setCodigo(codigo);
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
        if (e.getSource() == estoqueV.btnBuscarMate) {
            PesqMaterialView materialP = new PesqMaterialView(0);
            materialP.setVisible(true);
            estoqueV.dispose();
        }
        if (e.getSource() == estoqueV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            estoqueV.dispose();
        }

    }

    public void limparCampos() {
        estoqueV.txtMaterial.setText(null);
        estoqueV.txtCodigo.setText(null);
        estoqueV.txtQuantidade.setText(null);
        estoqueV.txtEstoque.setText(null);
        estoqueV.cmbOperacao.setSelectedIndex(0);
    }

//    public String validarCampos(String nome, String modelo, int categoria, int qtdMinima, double tamanho) {
//        String msg = null;
//        if (nome.isEmpty() || modelo.isEmpty() || categoria == 0 || qtdMinima == 0 || tamanho == 0) {
//            msg = "Por favor preencha todos os campos!";
//        }
//        return msg;
//    }

}
