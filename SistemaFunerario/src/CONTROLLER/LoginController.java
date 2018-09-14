/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import MODEL.LoginModel;
import DAO.LoginDAO;
import VIEW.LoginView;
import VIEW.MenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*
 * @author junio
 */
public class LoginController implements ActionListener {

    private LoginView loginV;
    private LoginDAO loginD;
    private LoginModel loginM;

    public LoginController(LoginView loginV, LoginDAO loginD, LoginModel loginM) {
        this.loginV = loginV;
        this.loginD = loginD;
        this.loginM = loginM;
        this.loginV.btnLogin.addActionListener(this);
        this.loginV.btnCancelar.addActionListener(this);
    }

    public void iniciar() {
        loginV.setTitle("Login");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String login = loginV.txtUsuario.getText();
        String senha = loginV.pswSenha.getText();

        if (e.getSource() == loginV.btnLogin) {
            if (validarCampos(login, senha)) {
                loginM.setLogin(login);
                loginM.setSenha(senha);

                if (loginD.login(loginM)) {
                    loginV.dispose();
                    MenuView menuV = new MenuView();
                    menuV.setVisible(true);
                    //JOptionPane.showMessageDialog(null, "Bem Vindo " + login + "!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor preencha todos os campos!");
            }
        }

        if (e.getSource() == loginV.btnCancelar) {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                loginV.dispose();
            }
        }

    }

    public boolean validarCampos(String login, String senha) {
        if (login.isEmpty() || senha.isEmpty()) {
            return false;
        }
        return true;
    }

}
