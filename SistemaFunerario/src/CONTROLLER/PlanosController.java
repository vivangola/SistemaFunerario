/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.PlanosDAO;
import MODEL.PlanosModel;
import VIEW.PlanosView;
import VIEW.MenuView;
import VIEW.PesqPlanosView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PlanosController implements ActionListener {

    private PlanosView planoV;
    private PlanosModel planoM;
    private PlanosDAO planoD;

    public PlanosController(PlanosView planoV, PlanosModel planoM, PlanosDAO planoD) {
        this.planoV = planoV;
        this.planoM = planoM;
        this.planoD = planoD;
        this.planoV.btnIncluir.addActionListener(this);
        this.planoV.btnAlterar.addActionListener(this);
        this.planoV.btnPesqPlan.addActionListener(this);
        this.planoV.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        planoV.setTitle("Planos");
        planoV.txtMensalidade.setDocument(new NumericoController());
        planoV.txtDependentes.setDocument(new NumericoController());
        if (planoD.buscarCodigo(planoM)) {
            planoV.txtCodigo.setText(String.valueOf(planoM.getCodigo()));
        } else {
            planoD.inserirPlanos();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int codigo = Integer.valueOf(planoV.txtCodigo.getText());
        String nome = planoV.txtNome.getText();
        int carencia = planoV.cmbCarencia.getSelectedIndex();
        int qtdDependente = 0;
        double mensalidade = 0;
        String retorno;
        if (!"".equals(planoV.txtDependentes.getText().trim()) || !"".equals(planoV.txtMensalidade.getText().trim())) {
            qtdDependente = Integer.valueOf(planoV.txtDependentes.getText());
            mensalidade = Double.parseDouble(planoV.txtMensalidade.getText().replaceAll(",", "."));
        }

        if (e.getSource() == planoV.btnIncluir) {
            retorno = validarCampos(nome, carencia, qtdDependente, mensalidade);
            if (retorno == null) {
                planoM.setCodigo(codigo);
                planoM.setNome(nome);
                planoM.setCarencia(carencia);
                planoM.setQtdDependente(qtdDependente);
                planoM.setMensalidade(mensalidade);

                if (planoD.incluir(planoM)) {
                    JOptionPane.showMessageDialog(null, "Inclusão efetuada com sucesso!");
                    limparCampos();
                    iniciar();
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == planoV.btnAlterar) {
            retorno = validarCampos(nome, carencia, qtdDependente, mensalidade);
            if (retorno == null) {
                Object[] options = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente alterar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (resposta == JOptionPane.YES_OPTION) {
                    planoM.setCodigo(codigo);
                    planoM.setNome(nome);
                    planoM.setCarencia(carencia);
                    planoM.setQtdDependente(qtdDependente);
                    planoM.setMensalidade(mensalidade);

                    if (planoD.alterar(planoM)) {
                        JOptionPane.showMessageDialog(null, "Alteração efetuada com sucesso!");
                        limparCampos();
                        iniciar();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, retorno);
            }
        }

        if (e.getSource() == planoV.btnPesqPlan) {
            PesqPlanosView planoP = new PesqPlanosView();
            planoP.setVisible(true);
            planoV.dispose();
        }

        if (e.getSource() == planoV.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            planoV.dispose();
        }
    }

    public void limparCampos() {
        planoV.txtNome.setText(null);
        planoV.txtCodigo.setText(null);
        planoV.txtDependentes.setText(null);
        planoV.txtMensalidade.setText(null);
        planoV.cmbCarencia.setSelectedIndex(0);
    }

    public String validarCampos(String nome, int carencia, int qtdDependente, double mensalidade) {
        String msg = null;
        if (nome.isEmpty() || carencia == 0 || qtdDependente == 0 || mensalidade == 0) {
            msg = "Por favor preencha todos os campos!";
        }
        return msg;
    }

}
