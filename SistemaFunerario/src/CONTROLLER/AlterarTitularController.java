/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.AlterarTitularDAO;
import DAO.ContaDAO;
import DAO.DependenteDAO;
import MODEL.ContaModel;
import MODEL.DependenteModel;
import MODEL.PlanosModel;
import MODEL.TitularModel;
import VIEW.AlterarTitularView;
import VIEW.ContaView;
import VIEW.MenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * @author junio
 */
public class AlterarTitularController implements ActionListener {

    private AlterarTitularView altV;
    private AlterarTitularDAO altD;
    private DependenteModel dependM;
    private ContaModel contaM;

    public AlterarTitularController(AlterarTitularView altV, DependenteModel dependM, AlterarTitularDAO altD, ContaModel contaM) {
        this.altV = altV;
        this.altD = altD;
        this.dependM = dependM;
        this.contaM = contaM;
        this.altV.btnIncluir.addActionListener(this);
        this.altV.btnInativar.addActionListener(this);
    }

    public void iniciar() {

        altV.setTitle("Novo Titular");
        altV.txtCodConta.setText(String.valueOf(contaM.getCodigo()));
        if (!altD.buscarPessoas(contaM, altV)) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar dependentes");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String[] nomeSplit = String.valueOf(altV.cmbDepend.getSelectedItem()).split("-");
        String nome = nomeSplit[0];
        int codigo = Integer.parseInt(altV.txtCodConta.getText());
        String retorno;

        if (e.getSource() == altV.btnIncluir) {
            retorno = validarCampos(nome);
        
            if (retorno == null) {
                contaM.setCodigo(codigo);
                dependM.setNome(nome);

                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja mesmo colocar " + nome + " como novo titular da conta?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {
                    contaM.setCodigo(Integer.parseInt(altV.txtCodConta.getText()));

                    if (altD.concluir(contaM, dependM)) {

                        JOptionPane.showMessageDialog(null, "Titular atualizado com sucesso!");
                        int resposta2 = JOptionPane.showOptionDialog(null, "Deseja atualizar alterar algum dado do novo titular?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (resposta2 == JOptionPane.YES_OPTION) {

                            TitularModel titularM = new TitularModel();
                            DependenteModel dependM = new DependenteModel();
                            DependenteDAO dependD = new DependenteDAO();
                            PlanosModel planoM = new PlanosModel();
                            ContaDAO contaD = new ContaDAO();

                            DefaultTableModel tModel = new DefaultTableModel();

                            tModel.addColumn("Nome");
                            tModel.addColumn("CPF");
                            tModel.addColumn("RG");
                            tModel.addColumn("Sexo");
                            tModel.addColumn("Nascimento");
                            tModel.addColumn("Parentesco");
                            tModel.setNumRows(0);

                            if (contaD.buscarSelecionado(contaM, titularM, planoM)) {
                                ContaView contaV = new ContaView(tModel);
                                contaV.txtCodigo.setText(String.valueOf(contaM.getCodigo()));
                                contaV.txtInclusao.setText(contaM.getDtInclusao());
                                contaV.cmbSituacao.addItem("Em Débito");
                                contaV.cmbSituacao.setSelectedIndex(contaM.getSituacao());
                                contaV.txtVencimento.setText(String.valueOf(contaM.getVencimento()));

                                contaV.txtCodPlano.setText(String.valueOf(planoM.getCodigo()));
                                contaV.txtPlano.setText(planoM.getNome());
                                contaV.txtCarencia.setText(String.valueOf(planoM.getCarencia()));
                                contaV.txtMensalidade.setText(String.valueOf(planoM.getMensalidade()));
                                contaV.txtQtdDepend.setText(String.valueOf(planoM.getQtdDependente()));

                                contaV.txtNome.setText(titularM.getNome());
                                contaV.txtCPF.setText(titularM.getCpf());
                                contaV.txtRG.setText(titularM.getRg());
                                contaV.txtEndereco.setText(titularM.getEndereco());
                                contaV.txtBairro.setText(titularM.getBairro());
                                contaV.txtCidade.setText(titularM.getCidade());
                                contaV.txtCEP.setText(titularM.getCep());
                                contaV.txtNascimento.setText(titularM.getNascimento());
                                contaV.txtTelefone.setText(titularM.getTelefone());
                                contaV.txtCargo.setText(titularM.getCargo());
                                contaV.cmbCivil.setSelectedItem(titularM.getEstadoCivil());
                                contaV.cmbEstado.setSelectedItem(titularM.getEstado());
                                if (titularM.getSexo() == "M") {
                                    contaV.cmbSexo.setSelectedItem("Masculinho");
                                } else {
                                    contaV.cmbSexo.setSelectedItem("Feminino");
                                }

                                if (!dependD.buscarSelecionado(contaM, dependM, contaV, tModel)) {
                                    JOptionPane.showMessageDialog(null, "Erro ao buscar dependentes!");
                                }
                                contaV.setVisible(true);
                                contaV.btnAlterar.setEnabled(true);
                                altV.dispose();
                            }
                        } else {
                            MenuView menuV = new MenuView();
                            menuV.setVisible(true);
                            altV.dispose();
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == altV.btnInativar) {

            contaM.setCodigo(codigo);
            contaM.setSituacao(1);

            Object[] options = {"Sim", "Não"};
            int resposta = JOptionPane.showOptionDialog(null, "Deseja mesmo inativar a conta?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (resposta == JOptionPane.YES_OPTION) {
                ContaDAO contaD = new ContaDAO();
                if (contaD.situacaoConta(contaM)) {
                    JOptionPane.showMessageDialog(null, "Conta Inativada!");
                    MenuView menuV = new MenuView();
                    menuV.setVisible(true);
                    altV.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao inativar a conta!");
                }
            }
        }
    }

    public String validarCampos(String nome) {
        JOptionPane.showMessageDialog(null, nome);
        String padrao = "Selecione";
        if (nome.equals(padrao)) {
            JOptionPane.showMessageDialog(null, nome);
            return "Por favor selecione um novo titular!";
        }
        return null;
    }

};
