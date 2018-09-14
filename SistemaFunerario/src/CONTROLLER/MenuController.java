/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import VIEW.LoginView;
import VIEW.MenuView;
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
        this.menuV.menuSair.addActionListener(this);
    }

    public void iniciar() {
        menuV.setTitle("Menu Principal");
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

    }

}
