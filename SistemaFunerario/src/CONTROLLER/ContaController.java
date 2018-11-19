/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ContaDAO;
import DAO.DependenteDAO;
import DAO.MensalidadeDAO;
import DAO.TitularDAO;
import MODEL.ContaModel;
import MODEL.DependenteModel;
import MODEL.PlanosModel;
import MODEL.TitularModel;
import VIEW.ContaView;
import VIEW.MenuView;
import VIEW.PesqContaView;
import VIEW.PesqPlanosView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * @author junio
 */
public class ContaController implements ActionListener {

    private ContaView contaV;
    private ContaModel contaM;
    private ContaDAO contaD;
    private TitularModel titularM;
    private TitularDAO titularD;
    private DependenteModel dependM;
    private DependenteDAO dependD;
    private PlanosModel planoM;
    private DefaultTableModel tModel;

    public ContaController(ContaView contaV, ContaModel contaM, ContaDAO contaD, TitularModel titularM, TitularDAO titularD, DependenteModel dependM, DependenteDAO dependD, PlanosModel planoM, DefaultTableModel tModel) {
        this.contaV = contaV;
        this.contaM = contaM;
        this.contaD = contaD;
        this.titularD = titularD;
        this.titularM = titularM;
        this.dependM = dependM;
        this.dependD = dependD;
        this.planoM = planoM;
        this.tModel = tModel;
        this.contaV.btnIncluir.addActionListener(this);
        this.contaV.btnAlterar.addActionListener(this);
        this.contaV.btnPesqConta.addActionListener(this);
        this.contaV.btnVoltar.addActionListener(this);
        this.contaV.btnPesqPlan.addActionListener(this);
        this.contaV.btnAdicionar.addActionListener(this);
        this.contaV.btnRemover.addActionListener(this);
        this.contaV.btnContrato.addActionListener(this);
    }

    public void iniciar() {

        contaV.setTitle("Contas");
        contaV.txtRG.setDocument(new NumericoController());
        contaV.txtDependenteRG.setDocument(new NumericoController());
        contaV.txtVencimento.setDocument(new NumericoController());
        contaV.cmbSituacao.removeItem("Em Débito");
        contaV.btnAlterar.setEnabled(false);

        java.util.Date d = new Date();
        contaV.txtInclusao.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));

        if (contaD.buscarCodigo(contaM)) {
            contaV.txtCodigo.setText(String.valueOf(contaM.getCodigo()));
        } else {
            contaD.inserirConta();
            contaD.buscarCodigo(contaM);
            contaV.txtCodigo.setText(String.valueOf(contaM.getCodigo()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //CONTA
        int codigo = 0;
        double mensalidade = 0;
        int vencimento = 0;
        int fk_plano = 0;
        int qtdDependente = 0;

        String inclusao = contaV.txtInclusao.getText();
        String inclusaoSQL = setDataSql(inclusao);
        int situacao = contaV.cmbSituacao.getSelectedIndex();
        if (!"".equals(contaV.txtMensalidade.getText().trim()) && !"".equals(contaV.txtQtdDepend.getText().trim()) && !"".equals(contaV.txtVencimento.getText().trim()) && !"".equals(contaV.txtCodPlano.getText().trim())) {
            codigo = Integer.parseInt(contaV.txtCodigo.getText());
            qtdDependente = Integer.parseInt(contaV.txtQtdDepend.getText());
            mensalidade = Double.parseDouble(contaV.txtMensalidade.getText().replaceAll(",", "."));
            vencimento = Integer.parseInt(contaV.txtVencimento.getText());
            fk_plano = Integer.parseInt(contaV.txtCodPlano.getText());
        }
        //TITULAR
        String cpf = contaV.txtCPF.getText();
        String rg = contaV.txtRG.getText();
        String nome = contaV.txtNome.getText();
        String telefone = contaV.txtTelefone.getText();
        String sexo = String.valueOf(contaV.cmbSexo.getSelectedItem());
        int sexoIndice = contaV.cmbSexo.getSelectedIndex();
        String estadoCivil = String.valueOf(contaV.cmbCivil.getSelectedItem());
        int civilIndice = contaV.cmbCivil.getSelectedIndex();
        String cargo = contaV.txtCargo.getText();
        String endereco = contaV.txtEndereco.getText();
        String bairro = contaV.txtBairro.getText();
        String cep = contaV.txtCEP.getText();
        String estado = String.valueOf(contaV.cmbEstado.getSelectedItem());
        String cidade = contaV.txtCidade.getText();
        String nasc = contaV.txtNascimento.getText();
        String nascSQL = setDataSql(nasc);
        //DEPENDENTE
        String cpfD = contaV.txtDependenteCPF.getText();
        String rgD = contaV.txtDependenteRG.getText();
        String nomeD = contaV.txtDependente.getText();
        String sexoD = String.valueOf(contaV.cmbSexo.getSelectedItem());
        int sexoIndiceD = 0;
        String parentesco = String.valueOf(contaV.cmbParentesco.getSelectedItem());
        String nascD = contaV.txtDependenteNasc.getText();

        String retorno = null;

        if (e.getSource() == contaV.btnIncluir) {

            retorno = validarCampos(fk_plano, vencimento, cpf, rg, nome, telefone, sexoIndice, civilIndice, cargo, endereco, bairro, cep, cidade);
            if (retorno == null) {

                //CONTA
                contaM.setCodigo(codigo);
                contaM.setDtInclusao(inclusaoSQL);
                contaM.setSituacao(situacao);
                contaM.setVencimento(vencimento);
                planoM.setCodigo(fk_plano);
                //TITULAR
                titularM.setCpf(cpf);
                titularM.setRg(rg);
                titularM.setNome(nome);
                titularM.setTelefone(telefone);
                if (sexoIndice == 1) {
                    titularM.setSexo("M");
                } else if (sexoIndice == 2) {
                    titularM.setSexo("F");
                }
                titularM.setEstadoCivil(estadoCivil);
                titularM.setCargo(cargo);
                titularM.setEndereco(endereco);
                titularM.setBairro(bairro);
                titularM.setEstado(estado);
                titularM.setCep(cep);
                titularM.setCidade(cidade);
                titularM.setNascimento(nascSQL);
                titularM.setFk_conta(codigo);

                if (contaD.incluir(contaM, planoM)) {
                    if (titularD.incluir(titularM, contaM)) {
                        int qtdLinha = tModel.getRowCount();
                        if (qtdLinha > 0) {
                            for (int i = 0; i < qtdLinha; i++) {

                                dependM.setNome(String.valueOf(tModel.getValueAt(i, 0)));
                                dependM.setCpf(String.valueOf(tModel.getValueAt(i, 1)));
                                dependM.setRg(String.valueOf(tModel.getValueAt(i, 2)));
                                String nascD2 = String.valueOf(tModel.getValueAt(i, 3));
                                dependM.setNascimento(setDataSql(nascD2));
                                dependM.setParentesco(String.valueOf(tModel.getValueAt(i, 4)));

                                if (dependD.incluir(dependM, contaM)) {
                                    if (i == qtdLinha - 1) {
                                        JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                                        limparCampos();
                                        limparCamposD();
                                        MensalidadeDAO mensalD = new MensalidadeDAO();
                                        mensalD.gerarMensalidade(contaM);
                                        mensalD.atualizarSituacao(contaM);
                                        iniciar();
                                    }
                                } else {
                                    contaD.excluir(contaM);
                                    titularD.excluir(titularM);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                            limparCampos();
                            limparCamposD();
                            MensalidadeDAO mensalD = new MensalidadeDAO();
                            mensalD.gerarMensalidade(contaM);
                            mensalD.atualizarSituacao(contaM);
                            iniciar();
                        }
                    } else {
                        contaD.excluir(contaM);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == contaV.btnAlterar) {
            //  retorno = validarCampos(codigo, inclusao, vencimento, fk_plano);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {

                    //CONTA
                    contaM.setCodigo(codigo);
                    contaM.setDtInclusao(inclusaoSQL);
                    contaM.setSituacao(situacao);
                    contaM.setVencimento(vencimento);
                    planoM.setCodigo(fk_plano);
                    //TITULAR
                    titularM.setCpf(cpf);
                    titularM.setRg(rg);
                    titularM.setNome(nome);
                    titularM.setTelefone(telefone);
                    if (sexoIndice == 1) {
                        titularM.setSexo("M");
                    } else if (sexoIndice == 2) {
                        titularM.setSexo("F");
                    }
                    titularM.setEstadoCivil(estadoCivil);
                    titularM.setCargo(cargo);
                    titularM.setEndereco(endereco);
                    titularM.setBairro(bairro);
                    titularM.setEstado(estado);
                    titularM.setCep(cep);
                    titularM.setCidade(cidade);
                    titularM.setNascimento(nascSQL);
                    titularM.setFk_conta(codigo);

                    if (contaD.alterar(contaM, planoM)) {
                        if (titularD.alterar(titularM, contaM)) {

                            int qtdLinha = tModel.getRowCount();
                            if (qtdLinha > 0) {

                                dependD.excluir(contaM);

                                for (int i = 0; i < qtdLinha; i++) {

                                    dependM.setNome(String.valueOf(tModel.getValueAt(i, 0)));
                                    dependM.setCpf(String.valueOf(tModel.getValueAt(i, 1)));
                                    dependM.setRg(String.valueOf(tModel.getValueAt(i, 2)));
                                    System.err.println(String.valueOf(tModel.getValueAt(i, 3)));
                                    String nascD2 = String.valueOf(tModel.getValueAt(i, 3));
                                    dependM.setNascimento(setDataSql(nascD2));
                                    dependM.setParentesco(String.valueOf(tModel.getValueAt(i, 4)));

                                    if (dependD.incluir(dependM, contaM)) {
                                        if (i == qtdLinha - 1) {
                                            JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                                            limparCampos();
                                            limparCamposD();
                                            iniciar();
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Erro alterar dependentes!");
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                                limparCampos();
                                limparCamposD();
                                iniciar();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro alterar titular!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro alterar conta!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == contaV.btnContrato) {
            String caminho = getClass().getResource("/CONTRATO/Contrato.docx").toString();
            try {
                Runtime.getRuntime().exec("cmd.exe /C start WINWORD.exe "+caminho);
            } catch (IOException ex) {
                Logger.getLogger(ContaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (e.getSource() == contaV.btnAdicionar) {

            int qtdLinha = tModel.getRowCount();
            int aux = 0;
            String retornoD;
            retornoD = validarCamposD(nomeD, rgD, cpfD);
            if (retornoD == null) {
                if (qtdLinha < qtdDependente) {
                    for (int i = 0; i < qtdLinha; i++) {
                        if (String.valueOf(tModel.getValueAt(i, 1)).equals(cpfD)) {
                            aux = 1;
                        }
                    }
                    if (aux != 1) {
                        tModel.addRow(new Object[]{nomeD, cpfD, rgD, nascD, parentesco});
                        limparCamposD();
                    } else {
                        JOptionPane.showMessageDialog(null, "CPF do dependente já cadastrado, por favor tente outro!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Limite de dependentes atingido de acordo com seu plano!");
                }
            } else {
                JOptionPane.showMessageDialog(null, retornoD);
            }
        }

        if (e.getSource() == contaV.btnRemover) {

            int linha = contaV.tblDependentes.getSelectedRow();
            if (linha != -1) {
                tModel.removeRow(linha);
            }
        }

        if (e.getSource() == contaV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            contaV.dispose();
        }

        if (e.getSource() == contaV.btnPesqPlan) {
            PesqPlanosView planoP = new PesqPlanosView(0, contaM);
            planoP.setVisible(true);
            contaV.dispose();
        }

        if (e.getSource() == contaV.btnPesqConta) {
            PesqContaView contaP = new PesqContaView(tModel);
            contaP.setVisible(true);
            contaV.dispose();
        }

    }

    public void limparCampos() {
        //CONTA
        contaV.txtCodigo.setText(null);
        contaV.txtInclusao.setText(null);
        contaV.cmbSituacao.setSelectedIndex(0);
        contaV.txtVencimento.setText(null);
        //PLANO
        contaV.txtCodPlano.setText(null);
        contaV.txtPlano.setText(null);
        contaV.txtCarencia.setText(null);
        contaV.txtMensalidade.setText(null);
        contaV.txtQtdDepend.setText(null);
        //TITULAR
        contaV.txtCPF.setText(null);
        contaV.txtRG.setText(null);
        contaV.txtNome.setText(null);
        contaV.txtTelefone.setText(null);
        contaV.cmbSexo.setSelectedIndex(0);
        contaV.cmbCivil.setSelectedIndex(0);
        contaV.txtCargo.setText(null);
        contaV.txtEndereco.setText(null);
        contaV.txtBairro.setText(null);
        contaV.txtCEP.setText(null);
        contaV.cmbEstado.setSelectedIndex(0);
        contaV.txtCidade.setText(null);
        contaV.txtNascimento.setText(null);
        tModel.setNumRows(0);
    }

    public void limparCamposD() {
        //DEPENDENTE
        contaV.txtDependenteCPF.setText(null);
        contaV.txtDependenteRG.setText(null);
        contaV.txtDependente.setText(null);
        contaV.txtDependenteNasc.setText(null);
        contaV.cmbParentesco.setSelectedIndex(0);
    }

    public String validarCampos(int fk_plano, int vencimento, String cpf, String rg, String nome, String telefone, int sexoIndice, int estadoCivil, String cargo, String endereco, String bairro, String cep, String cidade) {
        if (fk_plano == 0) {
            return "Por favor selecione um plano";
        }
        if (vencimento > 31) {
            return "Dia de vencimento inválido!";
        }
        if (cpf.trim().length() == 9) {
            return "CPF inválido!";
        }
        if (fk_plano == 0 || vencimento == 0 || rg.isEmpty() || nome.isEmpty() || telefone.isEmpty() || sexoIndice == 0 || estadoCivil == 0 || cargo.isEmpty() || endereco.isEmpty() || bairro.isEmpty() || cep.isEmpty() || cidade.isEmpty()) {
            return "Por favor preencha todos os campos!";
        }

        return null;
    }

    public String validarCamposD(String nome, String rg, String cpf) {
        if (cpf.trim().length() == 9) {
            return "CPF do dependente inválido!";
        }
        if (rg.isEmpty() || nome.isEmpty()) {
            return "Por favor preencha todos os campos de dependentes!";
        }
        return null;
    }

    public String setDataSql(String data) {
        System.err.println(data);
        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6);
        String dataSQL = ano + "-" + mes + "-" + dia;
        return dataSQL;
    }

}
