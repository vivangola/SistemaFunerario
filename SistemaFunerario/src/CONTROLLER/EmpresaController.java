/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.EmpresaDAO;
import MODEL.EmpresaModel;
import VIEW.EmpresaView;
import VIEW.MenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class EmpresaController implements ActionListener {

    private EmpresaView empresaV;
    private EmpresaModel empresaM;
    private EmpresaDAO empresaD;

    public EmpresaController(EmpresaView empresaV, EmpresaModel empresaM, EmpresaDAO empresaD) {
        this.empresaV = empresaV;
        this.empresaM = empresaM;
        this.empresaD = empresaD;
        this.empresaV.btnIncluir.addActionListener(this);
        this.empresaV.btnAlterar.addActionListener(this);
        this.empresaV.btnExcluir.addActionListener(this);
        this.empresaV.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        empresaV.setTitle("Empresa");
        empresaV.txtRaio.setText("0");
        if (empresaD.validaEmpresa(empresaM)) {
        empresaD.buscar(empresaM);
        empresaV.txtCNPJ.setText(empresaM.getCnpj());
        empresaV.txtNome.setText(empresaM.getNome());
        empresaV.txtTelefone.setText(empresaM.getTelefone());
        empresaV.txtEndereco.setText(empresaM.getEndereco());
        empresaV.txtBairro.setText(empresaM.getBairro());
        empresaV.txtCEP.setText(empresaM.getCep());
        empresaV.cmbEstado.setSelectedItem(empresaM.getEstado());
        empresaV.txtCidade.setText(empresaM.getCidade());
        empresaV.txtEmail.setText(empresaM.getEmail());
        empresaV.txtRaio.setText(String.valueOf(empresaM.getRaioAtuacao()));
        empresaV.btnIncluir.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cnpj = empresaV.txtCNPJ.getText();
        String nome = empresaV.txtNome.getText();
        String cep = empresaV.txtCEP.getText();
        String endereco = empresaV.txtEndereco.getText();
        String bairro = empresaV.txtBairro.getText();
        String estado = String.valueOf(empresaV.cmbEstado.getSelectedItem());
        String cidade = empresaV.txtCidade.getText();
        String telefone = empresaV.txtTelefone.getText();
        String email = empresaV.txtEmail.getText();
        int raio = Integer.valueOf(empresaV.txtRaio.getText().trim());
        String retorno;

        if (e.getSource() == empresaV.btnIncluir) {
            retorno = validarCampos(cnpj, nome, telefone, endereco, bairro, cep, cidade, email, raio);
            if (retorno == null) {
                empresaM.setCnpj(cnpj);
                empresaM.setNome(nome);
                empresaM.setTelefone(telefone);
                empresaM.setEndereco(endereco);
                empresaM.setBairro(bairro);
                empresaM.setEstado(estado);
                empresaM.setCep(cep);
                empresaM.setCidade(cidade);
                empresaM.setEmail(email);
                empresaM.setRaioAtuacao(raio);

                if (empresaD.incluir(empresaM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    empresaV.btnIncluir.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == empresaV.btnAlterar) {
            if (cnpj.trim().length() < 14) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {

                    empresaM.setCnpj(cnpj);
                    empresaM.setNome(nome);
                    empresaM.setTelefone(telefone);
                    empresaM.setEndereco(endereco);
                    empresaM.setBairro(bairro);
                    empresaM.setEstado(estado);
                    empresaM.setCep(cep);
                    empresaM.setCidade(cidade);
                    empresaM.setEmail(email);
                    empresaM.setRaioAtuacao(raio);

                    if (empresaD.alterar(empresaM)) {
                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma empresa selecionada!");
            }
        }

        if (e.getSource() == empresaV.btnExcluir) {
           
            if (cnpj.trim().length() < 14) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (resposta == JOptionPane.YES_OPTION) {
                    empresaM.setCnpj(cnpj);
                    if (empresaD.excluir(empresaM)) {
                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                        limparCampos();
                        empresaV.btnIncluir.setEnabled(true);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma empresa selecionada!");
            }
        }

        if (e.getSource() == empresaV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            empresaV.dispose();
        }

    }

    public void limparCampos() {
        empresaV.txtCNPJ.setText(null);
        empresaV.txtNome.setText(null);
        empresaV.txtTelefone.setText(null);
        empresaV.txtEndereco.setText(null);
        empresaV.txtBairro.setText(null);
        empresaV.txtCEP.setText(null);
        empresaV.cmbEstado.setSelectedIndex(0);
        empresaV.txtCidade.setText(null);
        empresaV.txtEmail.setText(null);
        empresaV.txtRaio.setText(null);
    }

    public String validarCampos(String cnpj, String nome, String telefone, String endereco, String bairro, String cep, String cidade, String email, Integer raio) {

        if (nome.isEmpty() || telefone.isEmpty() || endereco.isEmpty() || bairro.isEmpty() || cep.isEmpty() || cidade.isEmpty() || email.isEmpty() || raio == null) {
            return "Por favor preencha todos os campos!";
        }
        if (cnpj.trim().length() < 14) {
            return "CPF inválido!";
        }
        return null;
    }
    
    

}
