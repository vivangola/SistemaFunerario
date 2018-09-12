/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.FuncionarioDAO;
import MODEL.FuncionarioModel;
import VIEW.FuncionariosView;
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
        this.funcV.btnExcluir.addActionListener(this);
    }

    public void iniciar() {
        funcV.setTitle("Acessos");
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
        
        String nascDia = funcV.txtNascimento.getText().substring(0,2);
        String nascMes = funcV.txtNascimento.getText().substring(3,5);
        String nascAno = funcV.txtNascimento.getText().substring(6);
        String nascSQL = nascAno+"-"+nascMes+"-"+nascDia;
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
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
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
        if (cpf.isEmpty() || rg.isEmpty() || nome.isEmpty() || telefone.isEmpty() || sexo.equals(padrao) || estadoCivil.equals(padrao) || cargo.equals(padrao) || endereco.isEmpty() || bairro.isEmpty() || cep.isEmpty() || cidade.isEmpty()) {
           return "Por favor preencha todos os campos!";
        }
        if(cpf.length() < 11){
            return "CPF inválido!";
        }
        if(nasc.length() < 10){
            return "Data de nascimento inválido!";
        }
        return null;
    }


}
