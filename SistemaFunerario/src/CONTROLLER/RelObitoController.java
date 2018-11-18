/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.ObitoDAO;
import MODEL.ContaModel;
import MODEL.ObitoModel;
import MODEL.TitularModel;
import VIEW.MenuView;
import VIEW.ObitosView;
import VIEW.RelObitoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/*
 * @author junio
 */
public class RelObitoController implements ActionListener {

    private RelObitoView obitoP;
    private ObitoDAO obitoD;
    private ObitoModel obitoM;
    private ContaModel contaM;
    private TitularModel titularM;

    public RelObitoController(RelObitoView obitoP, ObitoDAO obitoD, ObitoModel obitoM, ContaModel contaM, TitularModel titularM) {
        this.obitoP = obitoP;
        this.obitoD = obitoD;
        this.obitoM = obitoM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.obitoP.btnBuscar.addActionListener(this);
        this.obitoP.btnContinuar.addActionListener(this);
        this.obitoP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        obitoP.setTitle("Relatório de Obitos");

        java.util.Date d = new Date();
        int mes = d.getMonth() + 1;
        int ano = d.getYear() + 1900;
        String dataIni = "01" + mes + ano;
        obitoP.txtIni.setText(dataIni);
        obitoP.txtFim.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
        obitoP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtIni = obitoP.txtIni.getText();
        String txtFim = obitoP.txtFim.getText();
        String iniSQL = setDataSql(txtIni);
        String fimSQL = setDataSql(txtFim);

        obitoD.buscar(obitoP, iniSQL, fimSQL);

        if (e.getSource() == obitoP.btnContinuar) {

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
                toExcel(obitoP.tblObitos, new File(file));

                JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!");
            }

        }

        if (e.getSource() == obitoP.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            obitoP.dispose();
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

    public String setDataSql(String data) {
        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6);
        String dataSQL = ano + "-" + mes + "-" + dia;
        return dataSQL;
    }
}
