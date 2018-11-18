/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import VIEW.LoginView;
import VIEW.MenuView;
import VIEW.RelEmprestimoView;
import VIEW.RelMaterialView;
import VIEW.RelContaView;
import VIEW.RelObitoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class MenuController implements ActionListener {

    private MenuView menuV;

    public MenuController(MenuView menuV) {
        this.menuV = menuV;
        this.menuV.mnRelEmprestimo.addActionListener(this);
        this.menuV.mnRelObito.addActionListener(this);
        this.menuV.mnRelEstoque.addActionListener(this);
        this.menuV.mnRelMensalidade.addActionListener(this);
        this.menuV.menuSair.addActionListener(this);
        
    }

    public void iniciar() {
        menuV.setTitle("Menu Principal");
        menuV.lblUsuario.setText(System.getProperty("login"));
        if (!"1".equals(System.getProperty("tipo"))) {
            menuV.menuAcesso.setVisible(false);
            menuV.menuEmpresa.setVisible(false);
            menuV.menuFornecedores.setVisible(false);
            menuV.menuFuncionarios.setVisible(false);
            menuV.menuMateriais.setVisible(false);
            menuV.menuPlanos.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == menuV.menuSair) {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                LoginView loginV = new LoginView();
                loginV.setVisible(true);
                menuV.dispose();
            }
        }

        if (e.getSource() == menuV.mnRelMensalidade) {
            RelContaView mensalRV = new RelContaView();
            mensalRV.setVisible(true);
            menuV.dispose();
        }

        if (e.getSource() == menuV.mnRelEmprestimo) {
            RelEmprestimoView emprestRV = new RelEmprestimoView();
            emprestRV.setVisible(true);
            menuV.dispose();
        }
        
        if (e.getSource() == menuV.mnRelObito) {
            RelObitoView obitoRV = new RelObitoView();
            obitoRV.setVisible(true);
            menuV.dispose();
        }
        
        if (e.getSource() == menuV.mnRelEstoque) {
            RelMaterialView mateRV = new RelMaterialView();
            mateRV.setVisible(true);
            menuV.dispose();
        }

    }

}
