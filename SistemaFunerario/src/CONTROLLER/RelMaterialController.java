/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.MaterialDAO;
import MODEL.ContaModel;
import MODEL.EmprestimoModel;
import MODEL.MaterialModel;
import MODEL.TitularModel;
import VIEW.ControleEstoqueView;
import VIEW.EmprestimoView;
import VIEW.MaterialView;
import VIEW.MenuView;
import VIEW.RelMaterialView;
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
public class RelMaterialController implements ActionListener {

    private RelMaterialView materialP;
    private MaterialDAO materialD;
    private MaterialModel materialM;
    private ContaModel contaM;
    private TitularModel titularM;
    private EmprestimoModel emprestM;

    public RelMaterialController(RelMaterialView materialP, MaterialDAO materialD, MaterialModel materialM, ContaModel contaM, TitularModel titularM, EmprestimoModel emprestM) {
        this.materialP = materialP;
        this.materialD = materialD;
        this.materialM = materialM;
        this.contaM = contaM;
        this.titularM = titularM;
        this.emprestM = emprestM;
        this.materialP.btnBuscar.addActionListener(this);
        this.materialP.btnContinuar.addActionListener(this);
        this.materialP.cmbOpcao.addActionListener(this);
        this.materialP.btnVoltar.addActionListener(this);
    }

    public void iniciar() {
        materialP.setTitle("Relatorio Materiais em Estoque");
        materialP.btnBuscar.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int cmbBusca = materialP.cmbOpcao.getSelectedIndex();

        if (e.getSource() == materialP.btnBuscar) {
            materialD.buscar(materialP, cmbBusca);
        }

        if (e.getSource() == materialP.btnContinuar) {

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
                toExcel(materialP.tblMaterial, new File(file));
                JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!");
            }

        }

        if (e.getSource() == materialP.btnVoltar) {
            MenuView menuV = new MenuView();
            menuV.setVisible(true);
            materialP.dispose();

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
