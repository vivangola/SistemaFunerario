/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VIEW;

import CONTROLLER.LoginController;
import MODEL.AcessoModel;
import MODEL.LoginModel;

/**
 *
 * @author junio
 */
public class main {
    public static void main(String[] args) {
        LoginView loginV = new LoginView();
        LoginModel loginM = new LoginModel();
        AcessoModel acessoM = new AcessoModel();
        LoginController controller = new LoginController(loginV, loginM, acessoM);
        controller.iniciar();
        loginV.setVisible(true);
        
    }
}
