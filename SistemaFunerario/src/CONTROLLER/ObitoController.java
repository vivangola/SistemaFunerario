/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.AlterarTitularDAO;
import DAO.ContaDAO;
import DAO.ObitoDAO;
import MODEL.ContaModel;
import MODEL.ObitoModel;
import VIEW.AlterarTitularView;
import VIEW.MenuView;
import VIEW.ObitosView;
import VIEW.PesqContaView;
import VIEW.PesqObitoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class ObitoController implements ActionListener {

    private ObitosView obitoV;
    private ObitoDAO obitoD;
    private ObitoModel obitoM;
    private ContaModel contaM;

    public ObitoController(ObitosView obitoV, ObitoModel obitoM, ObitoDAO obitoD, ContaModel contaM) {
        this.obitoV = obitoV;
        this.obitoD = obitoD;
        this.obitoM = obitoM;
        this.contaM = contaM;
        this.obitoV.btnIncluir.addActionListener(this);
        this.obitoV.btnAlterar.addActionListener(this);
        this.obitoV.btnVoltar.addActionListener(this);
        this.obitoV.btnPesqConta.addActionListener(this);
        this.obitoV.btnPesqObito.addActionListener(this);
    }

    public void iniciar() {

        obitoV.setTitle("Obitos");

        java.util.Date d = new Date();
        obitoV.txtDataEnt.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
        obitoV.txtDataVel.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
        obitoV.txtDataObt.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
        obitoV.btnAlterar.setEnabled(false);
        if (obitoD.buscarCodigo(obitoM)) {
            obitoV.txtCodObito.setText(String.valueOf(obitoM.getCodigo()));
        } else {
            obitoD.inserirObito();
            iniciar();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //CONTA
        String[] nomeSplit = String.valueOf(obitoV.cmbFalecido.getSelectedItem()).split("-");
        String nome = nomeSplit[0];

        //OBITO
        int codigo = Integer.parseInt(obitoV.txtCodObito.getText());
        int index = obitoV.cmbFalecido.getSelectedIndex();
        String horaObt = obitoV.txtHoraObt.getText();
        String horaVel = obitoV.txtHoraVel.getText();
        String horaEnt = obitoV.txtHoraEnt.getText();
        String localObt = obitoV.txtLocalObt.getText();
        String localVel = obitoV.txtLocalVel.getText();
        String localEnt = obitoV.txtLocalEnt.getText();
        String dataObt = obitoV.txtDataObt.getText();
        String dataVel = obitoV.txtDataVel.getText();
        String dataEnt = obitoV.txtDataEnt.getText();
        String dataObtSQL = setDataSql(dataObt);
        String dataVelSQL = setDataSql(dataEnt);
        String dataEntSQL = setDataSql(dataVel);
        String retorno;

        if (e.getSource() == obitoV.btnIncluir) {

            retorno = validarCampos(nome,horaObt,horaVel,horaEnt,localObt,localVel,localEnt,dataObt,dataVel,dataEnt);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente registrar óbito?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {
                    contaM.setCodigo(Integer.parseInt(obitoV.txtCodConta.getText()));

                    //OBITO
                    obitoM.setCodigo(codigo);
                    obitoM.setFk_cpf(nome);
                    obitoM.setDtObito(dataObtSQL);
                    obitoM.setHoraObito(horaObt);
                    obitoM.setLocalObito(localObt);
                    obitoM.setDtVel(dataVelSQL);
                    obitoM.setHoraVel(horaVel);
                    obitoM.setLocalVel(localVel);
                    obitoM.setDtEnt(dataEntSQL);
                    obitoM.setHoraEnt(horaEnt);
                    obitoM.setLocalEnt(localEnt);

                    if (obitoD.incluir(contaM, obitoM)) {
                        if (!obitoD.falecido(contaM, nome, index)) {
                            JOptionPane.showMessageDialog(null, "Erro ao incluir falecido!");
                            obitoD.excluir(obitoM);
                        }
                        if (index != 1) {
                            JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                            limparCampos();
                            iniciar();
                        } else {
                            if (obitoD.buscarDepend(contaM)) {
                                AlterarTitularView altV = new AlterarTitularView(contaM);
                                altV.setVisible(true);
                                obitoV.dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Nenhum dependente está apto a ser titular!");
                                contaM.setSituacao(1);
                                ContaDAO contaD = new ContaDAO();
                                if (contaD.situacaoConta(contaM)) {
                                    JOptionPane.showMessageDialog(null, "Conta Inativada!");
                                    MenuView menuV = new MenuView();
                                    menuV.setVisible(true);
                                    obitoV.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erro ao inativar a conta!");
                                }
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == obitoV.btnAlterar) {
            retorno = validarCampos(nome,horaObt,horaVel,horaEnt,localObt,localVel,localEnt,dataObt,dataVel,dataEnt);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {

                    obitoM.setCodigo(codigo);
                    obitoM.setDtObito(dataObtSQL);
                    obitoM.setHoraObito(horaObt);
                    obitoM.setLocalObito(localObt);
                    obitoM.setDtVel(dataVelSQL);
                    obitoM.setHoraVel(horaVel);
                    obitoM.setLocalVel(localVel);
                    obitoM.setDtEnt(dataEntSQL);
                    obitoM.setHoraEnt(horaEnt);
                    obitoM.setLocalEnt(localEnt);

                    if (obitoD.alterar(obitoM)) {
                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                        limparCampos();
                        iniciar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao alterar conta!");
                    }

                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == obitoV.btnPesqConta) {
            PesqContaView contaP = new PesqContaView(0);
            contaP.setVisible(true);
            obitoV.dispose();
        }

        if (e.getSource() == obitoV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            obitoV.dispose();
        }

        if (e.getSource() == obitoV.btnPesqObito) {
            PesqObitoView obitoP = new PesqObitoView();
            obitoP.setVisible(true);
            obitoV.dispose();
        }

    }

    public void limparCampos() {
        obitoV.txtCodConta.setText(null);
        obitoV.txtNome.setText(null);
        obitoV.cmbFalecido.removeAllItems();
        obitoV.cmbFalecido.addItem("Selecione");
        obitoV.txtHoraObt.setText(null);
        obitoV.txtHoraVel.setText(null);
        obitoV.txtHoraEnt.setText(null);
        obitoV.txtLocalObt.setText(null);
        obitoV.txtLocalVel.setText(null);
        obitoV.txtLocalEnt.setText(null);
        obitoV.txtDataObt.setText(null);
        obitoV.txtDataVel.setText(null);
        obitoV.txtDataEnt.setText(null);
    }

    public String validarCampos(String nome,String horaObt,String horaVel,String horaEnt,String localObt,String localVel,String localEnt,String dataObt,String dataVel,String dataEnt) {
        String padrao = "Selecione";
        if (nome.equals(padrao) || horaObt.isEmpty() || horaVel.isEmpty() || horaEnt.isEmpty() || localObt.isEmpty() || localVel.isEmpty() || localEnt.isEmpty() || dataObt.isEmpty() || dataVel.isEmpty() || dataEnt.isEmpty()) {
            return "Por favor preencha todos os campos!";
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

};
