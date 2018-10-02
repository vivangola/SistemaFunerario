/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODEL;

/**
 *
 * @author junio
 */
public class ContaModel {
    
    private int codigo; 
    private String dtInclusao;
    private int situacao;
    private int vencimento;
    private int fk_plano;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(String dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public int getVencimento() {
        return vencimento;
    }

    public void setVencimento(int vencimento) {
        this.vencimento = vencimento;
    }

    public int getFk_plano() {
        return fk_plano;
    }

    public void setFk_plano(int fk_plano) {
        this.fk_plano = fk_plano;
    }
    
}
