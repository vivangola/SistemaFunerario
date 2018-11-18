/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ContaDAO;
import MODEL.ContaModel;
import MODEL.PlanosModel;
import MODEL.MensalidadeModel;
import MODEL.TitularModel;
import VIEW.MenuView;
import VIEW.RelContaView;
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
public class RelContaController implements ActionListener {

    private RelContaView mensalP;
    private ContaDAO contaD;
    private MensalidadeModel mensalM;
    private PlanosModel planoM;
    private ContaModel contaM;
    private TitularModel titularM;

    public RelContaController(RelContaView mensalP, ContaDAO contaD) {
        this.mensalP = mensalP;
        this.contaD = contaD;
        this.mensalM = mensalM;
        this.planoM = planoM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.mensalP.btnBuscar.addActionListener(this);
        this.mensalP.btnContinuar.addActionListener(this);
        this.mensalP.cmbOpcao.addActionListener(this);
        this.mensalP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        mensalP.setTitle("Relatório de Contas por Situação");
        mensalP.cmbOpcao.setSelectedIndex(3);
        mensalP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int cmbBusca = mensalP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == mensalP.btnBuscar) {
            contaD.buscar(mensalP, cmbBusca);
        }

        if (e.getSource() == mensalP.btnContinuar) {

            JFileChooser fc = new JFileChooser();
            int option = fc.showSaveDialog(fc);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filename = fc.getSelectedFile().getName();
                String path = fc.getSelectedFile().getParentFile().getPath();

                int len = filename.length();
                String ext = "";
                String file = "";

                if (len > 4) {
                    ext = filename.substring(len - 4, len);
                }

                if (ext.equals(".xls")) {
                    file = path + "\\" + filename;
                } else {
                    file = path + "\\" + filename + ".xls";
                }
                toExcel(mensalP.tblConta, new File(file));
                JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!");
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
