/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MensalidadeDAO;
import MODEL.ContaModel;
import MODEL.MensalidadeModel;
import VIEW.MenuView;
import VIEW.PagamentoMensalidadeView;
import VIEW.PesqMensalidadeView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class MensalidadeController implements ActionListener {

    private PagamentoMensalidadeView mensalV;
    private MensalidadeDAO mensalD;
    private MensalidadeModel mensalM;
    private ContaModel contaM;

    public MensalidadeController(PagamentoMensalidadeView mensalV, MensalidadeModel mensalM, MensalidadeDAO mensalD, ContaModel contaM) {
        this.mensalV = mensalV;
        this.mensalD = mensalD;
        this.mensalM = mensalM;
        this.contaM = contaM;
        this.mensalV.btnConcluir.addActionListener(this);
        this.mensalV.btnConcluir.addActionListener(this);
        this.mensalV.btnVoltar.addActionListener(this);
    }

    public void iniciar() {

        mensalV.setTitle("Obitos");

        java.util.Date d = new Date();
        mensalV.txtDataPag.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int numero = Integer.parseInt(mensalV.txtNumero.getText());
        int tipoPag = mensalV.cmbPagamento.getSelectedIndex();
        String dataPag = mensalV.txtDataPag.getText();
        String dataPagSQL = setDataSql(dataPag);

        String retorno = null;

        if (e.getSource() == mensalV.btnConcluir) {

            // retorno = validarCampos(codigo, inclusao, vencimento, fk_plano);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente efetuar pagamento?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {
                    contaM.setCodigo(Integer.parseInt(mensalV.txtCodConta.getText()));

                    //OBITO
                    mensalM.setCodigo(numero);
                    mensalM.setTipoPagamento(tipoPag);
                    mensalM.setDataPagamento(dataPagSQL);

                    if (mensalD.concluir(mensalM)) {
                        JOptionPane.showMessageDialog(null, "Pagamento efetuada com sucesso!");
                        if (mensalD.atualizarSituacao(contaM)) {
                            JOptionPane.showMessageDialog(null, "Situação da Conta Atualizada!");
                            PesqMensalidadeView mensalPV = new PesqMensalidadeView();
                            mensalPV.setVisible(true);
                            mensalV.dispose();
                        }

                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == mensalV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            mensalV.dispose();
        }

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

};
