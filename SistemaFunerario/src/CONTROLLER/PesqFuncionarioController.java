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
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.funcP.btnVoltar.addActionListener(this);
        this.funcP.cmbOpcao.addActionListener(this);
    }

    public void iniciar() {
        funcP.setTitle("Pesquisar Funcionários");
        funcP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = funcP.txtBuscar.getText();
        int cmbBusca = funcP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == funcP.btnBuscar) {
            if (!((!funcP.txtBuscar.isEditValid() || txtBusca.isEmpty()) && cmbBusca != 0)) {
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
                        String nascAno = data.substring(0, 4);
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
            Object[] options = {"Sim","Não"};
            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?","Alerta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
            if (resposta == JOptionPane.YES_OPTION) {
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

        }

        if (e.getSource() == funcP.cmbOpcao) {
            funcP.txtBuscar.setValue(null);
            funcP.txtBuscar.setText(null);
            if (funcP.cmbOpcao.getSelectedIndex() != 0) {
                funcP.txtBuscar.setEnabled(true);
                if (funcP.cmbOpcao.getSelectedIndex() == 1) {
                    try {
                        funcP.txtBuscar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
                    } catch (ParseException ex) {
                        Logger.getLogger(PesqFuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    funcP.txtBuscar.setFormatterFactory(null);
                    funcP.txtBuscar.setText(null);
                }
            } else {
                funcP.txtBuscar.setEnabled(false);
                funcP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == funcP.btnVoltar) {
            if (tela == 0) {
                AcessoView acessoV = new AcessoView();
                acessoV.setVisible(true);
                funcP.dispose();
            } else {
                FuncionariosView funcV = new FuncionariosView();
                funcV.setVisible(true);
                funcP.dispose();
            }
        }

    }
}
