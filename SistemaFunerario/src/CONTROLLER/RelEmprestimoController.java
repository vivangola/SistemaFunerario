/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.EmprestimoDAO;
import MODEL.ContaModel;
import MODEL.EmprestimoModel;
import MODEL.MaterialModel;
import MODEL.TitularModel;
import VIEW.EmprestimoView;
import VIEW.MenuView;
import VIEW.RelEmprestimoView;
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
public class RelEmprestimoController implements ActionListener {

    private RelEmprestimoView emprestP;
    private EmprestimoDAO emprestD;
    private MaterialModel materialM;
    private ContaModel contaM;
    private TitularModel titularM;
    private EmprestimoModel emprestM;

    public RelEmprestimoController(RelEmprestimoView emprestP, EmprestimoDAO emprestD, MaterialModel materialM, ContaModel contaM, TitularModel titularM, EmprestimoModel emprestM) {
        this.emprestP = emprestP;
        this.emprestD = emprestD;
        this.materialM = materialM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.emprestM = emprestM;
        this.emprestP.btnBuscar.addActionListener(this);
        this.emprestP.btnContinuar.addActionListener(this);
        this.emprestP.cmbOpcao.addActionListener(this);
        this.emprestP.btnVoltar.addActionListener(this);
    }
    
    public void iniciar() {
        emprestP.setTitle("Pesquisar Empréstimos");
        emprestP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = emprestP.txtBuscar.getText();
        int cmbBusca = emprestP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == emprestP.btnBuscar) {
            if (!(txtBusca.equals("") && cmbBusca != 0)) {
                emprestD.buscar(emprestP, txtBusca, cmbBusca);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

        if (e.getSource() == emprestP.btnContinuar) {

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
                toExcel(emprestP.tblEmprest, new File(file));
                JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!");
            }

        }

        if (e.getSource() == emprestP.cmbOpcao) {
            if (emprestP.cmbOpcao.getSelectedIndex() != 0) {
                emprestP.txtBuscar.setEnabled(true);
            } else {
                emprestP.txtBuscar.setEnabled(false);
                emprestP.txtBuscar.setText(null);
            }
        }

        if (e.getSource() == emprestP.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            emprestP.dispose();
            
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
