/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MensalidadeDAO;
import MODEL.ContaModel;
import MODEL.PlanosModel;
import MODEL.MensalidadeModel;
import MODEL.TitularModel;
import VIEW.MenuView;
import VIEW.PagamentoMensalidadeView;
import VIEW.PesqMensalidadeView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/*
 * @author junio
 */
public class PesqMensalidadeController implements ActionListener {

    private PesqMensalidadeView mensalP;
    private MensalidadeDAO mensalD;
    private MensalidadeModel mensalM;
    private PlanosModel planoM;
    private ContaModel contaM;
    private TitularModel titularM;

    public PesqMensalidadeController(PesqMensalidadeView mensalP, MensalidadeDAO mensalD, MensalidadeModel mensalM, PlanosModel planoM, ContaModel contaM, TitularModel titularM) {
        this.mensalP = mensalP;
        this.mensalD = mensalD;
        this.mensalM = mensalM;
        this.planoM = planoM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.mensalP.btnBuscar.addActionListener(this);
        this.mensalP.btnContinuar.addActionListener(this);
        this.mensalP.btnGerar.addActionListener(this);
        this.mensalP.cmbOpcao.addActionListener(this);
        this.mensalP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        mensalP.setTitle("Pesquisar Mensalidades");
        mensalP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = mensalP.txtBuscar.getText();
        int cmbBusca = mensalP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == mensalP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                mensalD.buscar(mensalP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == mensalP.btnContinuar) {

            int linha = mensalP.tblMensalidade.getSelectedRow();

            if (linha > -1) {
                int codigo = (int) mensalP.tblMensalidade.getValueAt(linha, 0);
                mensalM.setCodigo(codigo);
                if (mensalD.buscarSelecionado(mensalM, contaM, titularM, planoM)) {
                        mensalP.dispose();
                        PagamentoMensalidadeView mensalV = new PagamentoMensalidadeView();
                        mensalV.txtNumero.setText(String.valueOf(mensalM.getCodigo()));
                        mensalV.txtPeriodo.setText(mensalM.getPeriodo());
                        mensalV.txtCodConta.setText(String.valueOf(contaM.getCodigo()));
                        mensalV.txtNome.setText(String.valueOf(titularM.getNome()));
                        mensalV.txtPlano.setText(planoM.getNome());
                        mensalV.txtValor.setText(String.valueOf(planoM.getMensalidade()));
                                        
                        mensalV.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
            }

        }
        
        if (e.getSource() == mensalP.btnGerar) {

            JFileChooser fc = new JFileChooser();
            int option = fc.showSaveDialog(fc);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filename = fc.getSelectedFile().getName();
                String path = fc.getSelectedFile().getParentFile().getPath();

                int len = filename.length();
                String ext = "";
                String file ;

                if (len > 4) {
                    ext = filename.substring(len - 4, len);
                }

                if (ext.equals(".xls")) {
                    file = path + "\\" + filename;
                } else {
                    file = path + "\\" + filename + ".xls";
                }
                toExcel(mensalP.tblMensalidade, new File(file));
                JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!");
            }

        }

//        if (e.getSource() == mensalP.btnExcluir) {
//            Object[] options = {"Sim", "Não"};
//            int resposta = JOptionPane.showOptionDialog(null, "Deseja realmente excluir?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//            if (resposta == JOptionPane.YES_OPTION) {
//                int linha = mensalP.tblObitos.getSelectedRow();
//
//                if (linha > -1) {
//                    int codigo = (int) mensalP.tblObitos.getValueAt(linha, 0);
//                    mensalM.setCodigo(codigo);
//                    if (mensalD.excluir(mensalM)) {
//                        JOptionPane.showMessageDialog(null, "Exclusão efetuada com sucesso!");
//                        mensalP.btnBuscar.doClick();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Por favor selecione um resultado!");
//                }
//            }
//        }

        if (e.getSource() == mensalP.cmbOpcao) {
            if (mensalP.cmbOpcao.getSelectedIndex() != 0) {
                mensalP.txtBuscar.setEnabled(true);
            } else {
                mensalP.txtBuscar.setEnabled(false);
                mensalP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == mensalP.btnVoltar) {
                MenuView menuV = new MenuView();
                menuV.setVisible(true);
                mensalP.dispose();
        }

    }
    
    public void toExcel(JTable table, File file) {
        try {
            TableModel model = table.getModel();
            FileWriter excel = new FileWriter(file);

            for (int i = 0; i < model.getColumnCount(); i++) {
                excel.write(model.getColumnName(i) + "\t");
            }

            excel.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    excel.write(model.getValueAt(i, j).toString() + "\t");
                }
                excel.write("\n");
            }

            excel.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
