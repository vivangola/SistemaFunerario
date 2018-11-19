/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.FornecedorDAO;
import MODEL.FornecedorModel;
import VIEW.FornecedorView;
import VIEW.MenuView;
import VIEW.PesqFornecedorView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class FornecedorController implements ActionListener {

    private FornecedorView forncV;
    private FornecedorModel forncM;
    private FornecedorDAO forncD;

    public FornecedorController(FornecedorView forncV, FornecedorModel forncM, FornecedorDAO forncD) {
        this.forncV = forncV;
        this.forncM = forncM;
        this.forncD = forncD;
        this.forncV.btnIncluir.addActionListener(this);
        this.forncV.btnAlterar.addActionListener(this);
        this.forncV.btnVoltar.addActionListener(this);
        this.forncV.btnPesqFornc.addActionListener(this);
    }

    public void iniciar() {
        forncV.setTitle("Fornecedor");
        forncV.btnAlterar.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cnpj = forncV.txtCNPJ.getText();
        String nome = forncV.txtNome.getText();
        String cep = forncV.txtCEP.getText();
        String endereco = forncV.txtEndereco.getText();
        String bairro = forncV.txtBairro.getText();
        String estado = String.valueOf(forncV.cmbEstado.getSelectedItem());
        String cidade = forncV.txtCidade.getText();
        String telefone = forncV.txtTelefone.getText();
        String email = forncV.txtEmail.getText();
        String inscricao = forncV.txtInscricao.getText();
        String retorno;

        if (e.getSource() == forncV.btnIncluir) {
            retorno = validarCampos(cnpj, nome, telefone, endereco, bairro, cep, cidade, email, inscricao);
            if (retorno == null) {
                forncM.setCnpj(cnpj);
                forncM.setNome(nome);
                forncM.setTelefone(telefone);
                forncM.setEndereco(endereco);
                forncM.setBairro(bairro);
                forncM.setEstado(estado);
                forncM.setCep(cep);
                forncM.setCidade(cidade);
                forncM.setEmail(email);
                forncM.setInscricaoEstadual(inscricao);

                if (forncD.incluir(forncM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                    iniciar();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == forncV.btnAlterar) {
            retorno = validarCampos(cnpj, nome, telefone, endereco, bairro, cep, cidade, email, inscricao);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {

                    forncM.setCnpj(cnpj);
                    forncM.setNome(nome);
                    forncM.setTelefone(telefone);
                    forncM.setEndereco(endereco);
                    forncM.setBairro(bairro);
                    forncM.setEstado(estado);
                    forncM.setCep(cep);
                    forncM.setCidade(cidade);
                    forncM.setEmail(email);
                    forncM.setInscricaoEstadual(inscricao);

                    if (forncD.alterar(forncM)) {
                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                        limparCampos();
                        iniciar();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == forncV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            forncV.dispose();
        }
        
        if (e.getSource() == forncV.btnPesqFornc) {
            PesqFornecedorView forncP = new PesqFornecedorView();
            forncP.setVisible(true);
            forncV.dispose();
        }

    }

    public void limparCampos() {
        forncV.txtCNPJ.setText(null);
        forncV.txtNome.setText(null);
        forncV.txtTelefone.setText(null);
        forncV.txtEndereco.setText(null);
        forncV.txtBairro.setText(null);
        forncV.txtCEP.setText(null);
        forncV.cmbEstado.setSelectedIndex(0);
        forncV.txtCidade.setText(null);
        forncV.txtEmail.setText(null);
        forncV.txtInscricao.setText(null);
    }

    public String validarCampos(String cnpj, String nome, String telefone, String endereco, String bairro, String cep, String cidade, String email, String inscricao) {

        if (nome.isEmpty() || telefone.isEmpty() || endereco.isEmpty() || bairro.isEmpty() || cep.isEmpty() || cidade.isEmpty() || email.isEmpty() || inscricao.isEmpty()) {
            return "Por favor preencha todos os campos!";
        }
        if (cnpj.trim().length() < 14) {
            return "CNPJ inválido!";
        }
        return null;
    }
    
    

}
