/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.FuncionarioDAO;
import MODEL.FuncionarioModel;
import VIEW.FuncionariosView;
import VIEW.MenuView;
import VIEW.PesqFuncionariosView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class FuncionarioController implements ActionListener {

    private FuncionariosView funcV;
    private FuncionarioModel funcM;
    private FuncionarioDAO funcD;

    public FuncionarioController(FuncionariosView funcV, FuncionarioModel funcM, FuncionarioDAO funcD) {
        this.funcV = funcV;
        this.funcM = funcM;
        this.funcD = funcD;
        this.funcV.btnIncluir.addActionListener(this);
        this.funcV.btnAlterar.addActionListener(this);
        this.funcV.btnPesqFunc.addActionListener(this);
        this.funcV.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        funcV.setTitle("Acessos");
        funcV.txtRG.setDocument(new NumericoController());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cpf = funcV.txtCPF.getText();
        String rg = funcV.txtRG.getText();
        String nome = funcV.txtNome.getText();
        String telefone = funcV.txtTelefone.getText();
        String sexo = String.valueOf(funcV.cmbSexo.getSelectedItem());
        int sexoIndice = funcV.cmbSexo.getSelectedIndex();
        String estadoCivil = String.valueOf(funcV.cmbCivil.getSelectedItem());
        String cargo = String.valueOf(funcV.cmbCargo.getSelectedItem());
        String endereco = funcV.txtEndereco.getText();
        String bairro = funcV.txtBairro.getText();
        String cep = funcV.txtCEP.getText();
        String estado = String.valueOf(funcV.cmbEstado.getSelectedItem());
        String cidade = funcV.txtCidade.getText();
        String nasc = funcV.txtNascimento.getText();
        String nascSQL = setDataSql(nasc);
        String retorno;

        if (e.getSource() == funcV.btnIncluir) {
            retorno = validarCampos(cpf, rg, nome, telefone, sexo, estadoCivil, cargo, endereco, bairro, cep, cidade, nasc);
            if (retorno == null) {
                funcM.setCpf(cpf);
                funcM.setRg(rg);
                funcM.setNome(nome);
                funcM.setTelefone(telefone);
                if (sexoIndice == 1) {
                    funcM.setSexo("M");
                } else if (sexoIndice == 2) {
                    funcM.setSexo("F");
                }
                funcM.setEstadoCivil(estadoCivil);
                funcM.setCargo(cargo);
                funcM.setEndereco(endereco);
                funcM.setBairro(bairro);
                funcM.setEstado(estado);
                funcM.setCep(cep);
                funcM.setCidade(cidade);
                funcM.setNascimento(nascSQL);

                if (funcD.incluir(funcM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == funcV.btnAlterar) {
            retorno = validarCampos(cpf, rg, nome, telefone, sexo, estadoCivil, cargo, endereco, bairro, cep, cidade, nasc);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {

                    funcM.setCpf(cpf);
                    funcM.setRg(rg);
                    funcM.setNome(nome);
                    funcM.setTelefone(telefone);
                    if (sexoIndice == 1) {
                        funcM.setSexo("M");
                    } else if (sexoIndice == 2) {
                        funcM.setSexo("F");
                    }
                    funcM.setEstadoCivil(estadoCivil);
                    funcM.setCargo(cargo);
                    funcM.setEndereco(endereco);
                    funcM.setBairro(bairro);
                    funcM.setEstado(estado);
                    funcM.setCep(cep);
                    funcM.setCidade(cidade);
                    funcM.setNascimento(nascSQL);

                    if (funcD.alterar(funcM)) {
                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                        limparCampos();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == funcV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            funcV.dispose();
        }

        if (e.getSource() == funcV.btnPesqFunc) {
            PesqFuncionariosView funcP = new PesqFuncionariosView(1);
            funcP.setVisible(true);
            funcV.dispose();
        }

    }

    public void limparCampos() {
        funcV.txtCPF.setText(null);
        funcV.txtRG.setText(null);
        funcV.txtNome.setText(null);
        funcV.txtTelefone.setText(null);
        funcV.cmbSexo.setSelectedIndex(0);
        funcV.cmbCivil.setSelectedIndex(0);
        funcV.cmbCargo.setSelectedIndex(0);
        funcV.txtEndereco.setText(null);
        funcV.txtBairro.setText(null);
        funcV.txtCEP.setText(null);
        funcV.cmbEstado.setSelectedIndex(0);
        funcV.txtCidade.setText(null);
        funcV.txtNascimento.setText(null);
    }

    public String validarCampos(String cpf, String rg, String nome, String telefone, String sexo, String estadoCivil, String cargo, String endereco, String bairro, String cep, String cidade, String nasc) {
        String padrao = "Selecione";
        if (rg.isEmpty() || nome.isEmpty() || telefone.isEmpty() || sexo.equals(padrao) || estadoCivil.equals(padrao) || cargo.equals(padrao) || endereco.isEmpty() || bairro.isEmpty() || cep.isEmpty() || cidade.isEmpty()) {
            return "Por favor preencha todos os campos!";
        }
        if (cpf.trim().length() == 9) {
            return "CPF inválido!";
        }
        return null;
    }
    
    public String setDataSql(String data) {
        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6);
        String dataSQL = ano + "-" + mes + "-" + dia;
        return dataSQL;
    }

}
