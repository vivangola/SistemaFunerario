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
public class ObitoModel {
    
    private int codigo; 
    private String fk_cpf;
    private int fk_conta;
    private String localObito;
    private String dtObito;
    private String horaObito;
    private String localVel;
    private String dtVel;
    private String horaVel;
    private String localEnt;
    private String dtEnt;
    private String horaEnt;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFk_cpf() {
        return fk_cpf;
    }

    public void setFk_cpf(String fk_cpf) {
        this.fk_cpf = fk_cpf;
    }

    public int getFk_conta() {
        return fk_conta;
    }

    public void setFk_conta(int fk_conta) {
        this.fk_conta = fk_conta;
    }

    public String getLocalObito() {
        return localObito;
    }

    public void setLocalObito(String localObito) {
        this.localObito = localObito;
    }

    public String getDtObito() {
        return dtObito;
    }

    public void setDtObito(String dtObito) {
        this.dtObito = dtObito;
    }

    public String getHoraObito() {
        return horaObito;
    }

    public void setHoraObito(String horaObito) {
        this.horaObito = horaObito;
    }

    public String getLocalVel() {
        return localVel;
    }

    public void setLocalVel(String localVel) {
        this.localVel = localVel;
    }

    public String getDtVel() {
        return dtVel;
    }

    public void setDtVel(String dtVel) {
        this.dtVel = dtVel;
    }

    public String getHoraVel() {
        return horaVel;
    }

    public void setHoraVel(String horaVel) {
        this.horaVel = horaVel;
    }

    public String getLocalEnt() {
        return localEnt;
    }

    public void setLocalEnt(String localEnt) {
        this.localEnt = localEnt;
    }

    public String getDtEnt() {
        return dtEnt;
    }

    public void setDtEnt(String dtEnt) {
        this.dtEnt = dtEnt;
    }

    public String getHoraEnt() {
        return horaEnt;
    }

    public void setHoraEnt(String horaEnt) {
        this.horaEnt = horaEnt;
    }
    
}
