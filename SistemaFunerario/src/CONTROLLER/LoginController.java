/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import MODEL.AcessoModel;
import MODEL.LoginModel;
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
    private LoginModel loginM;
    private AcessoModel acessoM;

    public LoginController(LoginView loginV, LoginModel loginM, AcessoModel acessoM) {
        this.loginV = loginV;
        this.loginM = loginM;
        this.acessoM = acessoM;
        this.loginV.btnLogin.addActionListener(this);
    }

    public void iniciar() {
        loginV.setTitle("Login");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String login = loginV.txtUsuario.getText();
        String senha = loginV.pswSenha.getText();

        if (e.getSource() == loginV.btnLogin) {
            if (loginM.validarCampos(login, senha)) {
                acessoM.setLogin(login);
                acessoM.setSenha(senha);

                if (loginM.login(acessoM)) {
                    loginV.dispose();
                    MenuView menuV = new MenuView(acessoM);
                    menuV.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Bem Vindo " + login + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "Acesso Negado!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor preencha todos os campos!");
            }
        }
    }

}
