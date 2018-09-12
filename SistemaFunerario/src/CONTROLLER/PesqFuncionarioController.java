/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.FuncionarioDAO;
import MODEL.AcessoModel;
import MODEL.FuncionarioModel;
import VIEW.AcessoView;
import VIEW.FuncionariosView;
import VIEW.PesqFuncionariosView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqFuncionarioController implements ActionListener {

    private PesqFuncionariosView funcP;
    private FuncionarioDAO funcD;
    private FuncionarioModel funcM;
    private int tela;

    public PesqFuncionarioController(PesqFuncionariosView funcP, FuncionarioDAO funcD, FuncionarioModel funcM, int tela) {
        this.funcP = funcP;
        this.funcD = funcD;
        this.funcM = funcM;
        this.tela = tela;
        this.funcP.btnBuscar.addActionListener(this);
        this.funcP.btnContinuar.addActionListener(this);
        this.funcP.btnExcluir.addActionListener(this);
        this.funcP.cmbOpcao.addActionListener(this);
    }

    public void iniciar() {
        funcP.setTitle("Pesquisar Funcionários");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = funcP.txtBuscar.getText();
        int cmbBusca = funcP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == funcP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                funcD.buscar(funcP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == funcP.btnContinuar) {
            AcessoModel acessoM = new AcessoModel();
            int linha = funcP.tblAcesso.getSelectedRow();

            if (linha > -1) {
                String cpf = (String) funcP.tblAcesso.getValueAt(linha, 2);
                funcM.setCpf(cpf);
                if (funcD.buscarSelecionado(funcM, acessoM)) {
                    funcP.dispose();
                    if (tela == 0) {
                        AcessoView acessoV = new AcessoView(funcM);
                        acessoV.txtFuncionario.setText(funcM.getNome());
                        acessoV.setVisible(true);
                    } else {
                        FuncionariosView funcV = new FuncionariosView();
                        funcV.txtNome.setText(funcM.getNome());
                        funcV.txtCPF.setText(funcM.getCpf());
                        funcV.txtRG.setText(funcM.getRg());
                        funcV.txtEndereco.setText(funcM.getEndereco());
                        funcV.txtBairro.setText(funcM.getBairro());
                        funcV.txtCidade.setText(funcM.getCidade());
                        funcV.txtCEP.setText(funcM.getCep());
                        String data = funcM.getNascimento();
                        funcV.txtTelefone.setText(funcM.getTelefone());
                        funcV.cmbCargo.setSelectedItem(funcM.getCargo());
                        funcV.cmbCivil.setSelectedItem(funcM.getEstadoCivil());
                        funcV.cmbEstado.setSelectedItem(funcM.getEstado());
                        if (funcM.getSexo() == "M") {
                            funcV.cmbSexo.setSelectedItem("Masculinho");
                        } else {
                            funcV.cmbSexo.setSelectedItem("Feminino");
                        }
                        String nascDia = data.substring(8);
                        String nascMes = data.substring(5, 7);
                        String nascAno = data.substring(0,4);
                        String nascimento = nascDia + "/" + nascMes + "/" + nascAno;
                        funcV.txtNascimento.setText(nascimento);
                        funcV.setVisible(true);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == funcP.btnExcluir) {

            int linha = funcP.tblAcesso.getSelectedRow();

            if (linha > -1) {
                String cpf = (String) funcP.tblAcesso.getValueAt(linha, 2);
                funcM.setCpf(cpf);
                if (funcD.excluir(funcM)) {
                    JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
                    funcP.btnBuscar.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

        if (e.getSource() == funcP.cmbOpcao) {
            if (funcP.cmbOpcao.getSelectedIndex() != 0) {
                funcP.txtBuscar.setEnabled(true);
            } else {
                funcP.txtBuscar.setEnabled(false);
                funcP.txtBuscar.setText(null);
            }
        }

    }
}
