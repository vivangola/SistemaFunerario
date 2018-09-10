/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author junio
 */
public class NumericoController extends PlainDocument{
    
    @Override
    public void insertString(int i, String string, AttributeSet as) throws BadLocationException {
        int tamanho = this.getLength() + string.length();
        if (tamanho <= 11){
            super.insertString(i, string.replaceAll("[aA-zZ]",""), as); 
        }else{
            super.insertString(i, string.replaceAll("[aA0-zZ9]",""), as); 
        }
    }
    
}
