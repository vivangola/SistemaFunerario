/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ContaDAO;
import MODEL.ContaModel;
import MODEL.DependenteModel;
import MODEL.TitularModel;
import VIEW.ContaView;
import VIEW.MenuView;
import VIEW.PesqContaView;
import VIEW.PesqPlanosView;
//import VIEW.PesqContaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class ContaController implements ActionListener {

    private ContaView contaV;
    private ContaModel contaM;
    private ContaDAO contaD;
    private TitularModel titularM;
    private DependenteModel dependM;

    public ContaController(ContaView contaV, ContaModel contaM, ContaDAO contaD, TitularModel titularM, DependenteModel dependM) {
        this.contaV = contaV;
        this.contaM = contaM;
        this.contaD = contaD;
        this.titularM = titularM;
        this.dependM = dependM;
        this.contaV.btnIncluir.addActionListener(this);
        this.contaV.btnAlterar.addActionListener(this);
        this.contaV.btnPesqConta.addActionListener(this);
        this.contaV.btnVoltar.addActionListener(this);
        this.contaV.btnPesqPlan.addActionListener(this);
    }

    public void iniciar() {
        contaV.setTitle("Acessos");
        contaV.txtRG.setDocument(new NumericoController());
        contaV.txtVencimento.setDocument(new NumericoController());
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
        int pk_plano = 0;
        int qtdDependente = 0;

        String inclusao = contaV.txtInclusao.getText();
        String inclusaoSQL = setDataSql(inclusao);
        int situacao = contaV.cmbSituacao.getSelectedIndex();
        if (!"".equals(contaV.txtMensalidade.getText().trim()) || !"".equals(contaV.txtQtdDepend.getText().trim()) || !"".equals(contaV.txtVencimento.getText().trim()) || !"".equals(contaV.txtCodPlano.getText().trim())) {
            codigo = Integer.parseInt(contaV.txtCodigo.getText());
          //  qtdDependente = Integer.parseInt(contaV.txtQtdDepend.getText());
         //   mensalidade = Double.parseDouble(contaV.txtMensalidade.getText().replaceAll(",", "."));
            vencimento = Integer.parseInt(contaV.txtVencimento.getText());
            pk_plano = Integer.parseInt(contaV.txtCodPlano.getText());
        }
        //TITULAR
        String cpf = contaV.txtCPF.getText();
        String rg = contaV.txtRG.getText();
        String nome = contaV.txtNome.getText();
        String telefone = contaV.txtTelefone.getText();
        String sexo = String.valueOf(contaV.cmbSexo.getSelectedItem());
        int sexoIndice = contaV.cmbSexo.getSelectedIndex();
        String estadoCivil = String.valueOf(contaV.cmbCivil.getSelectedItem());
        String cargo = String.valueOf(contaV.cmbCargo.getSelectedItem());
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
        int sexoIndiceD = contaV.cmbSexo.getSelectedIndex();
        String parentesco = String.valueOf(contaV.cmbParentesco.getSelectedItem());
        String nascD = contaV.txtDependenteNasc.getText();
        String nascDSQL = setDataSql(nascD);

        String retorno;

        if (e.getSource() == contaV.btnIncluir) {
            retorno = validarCampos(codigo, inclusao, vencimento, pk_plano);
            if (retorno == null) {
                
                //CONTA
                contaM.setCodigo(codigo);
                contaM.setDtInclusao(inclusaoSQL);
                contaM.setSituacao(situacao);
                contaM.setVencimento(vencimento);
                contaM.setPk_plano(pk_plano);
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
                //DEPENDENTE
                dependM.setCpf(cpfD);
                dependM.setRg(rgD);
                dependM.setNome(nomeD);
                if (sexoIndiceD == 1) {
                    dependM.setSexo("M");
                } else if (sexoIndiceD == 2) {
                    dependM.setSexo("F");
                }
                dependM.setNascimento(nascDSQL);
                dependM.setParentesco(parentesco);
                dependM.setFk_conta(codigo);

                if (contaD.incluir(contaM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == contaV.btnAlterar) {
            retorno = validarCampos(codigo, inclusao, vencimento, pk_plano);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {

                    //CONTA
                    contaM.setCodigo(codigo);
                    contaM.setDtInclusao(inclusaoSQL);
                    contaM.setSituacao(situacao);
                    contaM.setVencimento(vencimento);
                    contaM.setPk_plano(pk_plano);
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
                    //DEPENDENTE
                    dependM.setCpf(cpfD);
                    dependM.setRg(rgD);
                    dependM.setNome(nomeD);
                    if (sexoIndiceD == 1) {
                        dependM.setSexo("M");
                    } else if (sexoIndiceD == 2) {
                        dependM.setSexo("F");
                    }
                    dependM.setNascimento(nascDSQL);
                    dependM.setParentesco(parentesco);
                    dependM.setFk_conta(codigo);

//                    if (contaD.alterar(contaM)) {
//                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
//                        limparCampos();
//                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
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
            PesqContaView contaP = new PesqContaView();
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
        contaV.cmbCargo.setSelectedIndex(0);
        contaV.txtEndereco.setText(null);
        contaV.txtBairro.setText(null);
        contaV.txtCEP.setText(null);
        contaV.cmbEstado.setSelectedIndex(0);
        contaV.txtCidade.setText(null);
        contaV.txtNascimento.setText(null);
        //DEPENDENTE
        contaV.txtDependenteCPF.setText(null);
        contaV.txtDependenteRG.setText(null);
        contaV.txtDependente.setText(null);
        contaV.txtDependenteNasc.setText(null);
        contaV.cmbParentesco.setSelectedIndex(0);
    }

    public String validarCampos(int codigo, String inclusao, int vencimento, int pk_plano) {
        String padrao = "Selecione";
        if (codigo == 0 || inclusao.isEmpty() || vencimento == 0 || pk_plano == 0) {
            return "Por favor preencha todos os campos!";
        }
//        if (cpf.trim().length() == 9) {
//            return "CPF inválido!";
//        }
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
