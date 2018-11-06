/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ContaDAO;
import DAO.DependenteDAO;
import DAO.ObitoDAO;
import MODEL.ContaModel;
import MODEL.DependenteModel;
import MODEL.ObitoModel;
import MODEL.PlanosModel;
import MODEL.TitularModel;
import VIEW.ContaView;
import VIEW.ObitosView;
import VIEW.PesqContaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * @author junio
 */
public class PesqContaController implements ActionListener {

    private PesqContaView contaP;
    private ContaDAO contaD;
    private ContaModel contaM;
    private DefaultTableModel tModel;
    private int tela;

    public PesqContaController(PesqContaView contaP, ContaDAO contaD, ContaModel contaM, DefaultTableModel tModel, int tela) {
        this.contaP = contaP;
        this.contaD = contaD;
        this.contaM = contaM;
        this.tModel = tModel;
        this.tela = tela;
        this.contaP.btnBuscar.addActionListener(this);
        this.contaP.btnContinuar.addActionListener(this);
        this.contaP.btnVoltar.addActionListener(this);
        this.contaP.cmbOpcao.addActionListener(this);
    }

    public void iniciar() {
        contaP.setTitle("Pesquisar Contas");
        contaP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = contaP.txtBuscar.getText();
        int cmbBusca = contaP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == contaP.btnBuscar) {
            if (!(txtBusca.isEmpty() && cmbBusca != 0)) {
                contaD.buscar(contaP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == contaP.btnContinuar) {
           
            TitularModel titularM = new TitularModel();
            PlanosModel planoM = new PlanosModel();
            DependenteModel dependM = new DependenteModel();
            DependenteDAO dependD = new DependenteDAO();
            
            int linha = contaP.tblConta.getSelectedRow();

            if (linha > -1) {
                int codigo = (int) contaP.tblConta.getValueAt(linha, 0);
                contaM.setCodigo(codigo);
                if (contaD.buscarSelecionado(contaM, titularM, planoM)) {
                    contaP.dispose();
                     if(tela == 1){
                        ContaView contaV = new ContaView(tModel);
                        contaV.txtCodigo.setText(String.valueOf(contaM.getCodigo()));
                        contaV.txtInclusao.setText(contaM.getDtInclusao());
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
                        
                        contaV.setVisible(true);
                        
                        if(!dependD.buscarSelecionado(contaM, dependM, contaV, tModel)){
                            JOptionPane.showMessageDialog(null, "Erro ao buscar dependentes!");
                        }
                     }else{
                        ObitosView obitoV = new ObitosView();
                        ObitoDAO obitoD = new ObitoDAO();
                        ObitoModel obitoM = new ObitoModel();
                        obitoV.txtCodConta.setText(String.valueOf(contaM.getCodigo()));
                        obitoV.txtNome.setText(titularM.getNome());
                        obitoV.setVisible(true);
                        obitoM.setCodigo(0);
                        if(!obitoD.buscarPessoas(contaM, obitoM, obitoV)){
                            JOptionPane.showMessageDialog(null, "Erro ao buscar pessoas!");
                        }
                        
                     }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }

//        if (e.getSource() == contaP.btnExcluir) {
//            Object[] options = {"Sim","Não"};
//            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?","Alerta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
//            if (resposta == JOptionPane.YES_OPTION) {
//                int linha = contaP.tblConta.getSelectedRow();
//
//                if (linha > -1) {
//                    int codigo = (Integer) contaP.tblConta.getValueAt(linha, 0);
//                    contaM.setCodigo(codigo);
//
//                    if (contaD.excluir(contaM)) {
//                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
//                        contaP.btnBuscar.doClick();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
//                }
//            }
//
//        }
//        

        if (e.getSource() == contaP.cmbOpcao) {

            contaP.txtBuscar.setText(null);
            if (contaP.cmbOpcao.getSelectedIndex() != 0) {
                contaP.txtBuscar.setEnabled(true);
            } else {
                contaP.txtBuscar.setEnabled(false);
                contaP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == contaP.btnVoltar) {
            if (tela == 1){
                ContaView contaV = new ContaView(contaM);
                contaV.setVisible(true);
                contaP.dispose();
            }else{
                ObitosView obitoV = new ObitosView();
                obitoV.setVisible(true);
                contaP.dispose();
            }
        }

    }
}
