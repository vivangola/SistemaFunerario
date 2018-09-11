/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.AcessoDAO;
import VIEW.PesqAcessoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class PesqAcessoController implements ActionListener {

    private PesqAcessoView acessoP;
    private AcessoDAO acessoD;

    public PesqAcessoController(PesqAcessoView acessoP, AcessoDAO acessoD) {
        this.acessoP = acessoP;
        this.acessoD = acessoD;
        this.acessoP.btnBuscar.addActionListener(this);
    }

    public void iniciar() {
        acessoP.setTitle("Pesquisar Acessos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String txtBusca = acessoP.txtBuscar.getText();
        int cmbBusca = acessoP.cmbOpcao.getSelectedIndex();
        
        if (e.getSource() == acessoP.btnBuscar) {
            if(!(txtBusca.equals("") && cmbBusca != 0)){
            String campo = "";
            if (cmbBusca == 1) {
                campo = "login";
            } else if (cmbBusca == 2) {
                campo = "funcionario";
            }
            acessoD.buscar(acessoP, txtBusca, campo);
            }else{
                JOptionPane.showMessageDialog(null, "Por favor digite um valor!");
            }
        }

    }
}
