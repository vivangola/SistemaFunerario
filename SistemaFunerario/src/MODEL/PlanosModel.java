/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODEL;

/**
 *
 * @author Daiane Camargo
 */
public class PlanosModel {
    
    private int codigo;
    private String nome;
    private double mensalidade;
    private int carencia;
    private int qtdDependente;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(double mensalidade) {
        this.mensalidade = mensalidade;
    }

    public int getCarencia() {
        return carencia;
    }

    public void setCarencia(int carencia) {
        this.carencia = carencia;
    }

    public int getQtdDependente() {
        return qtdDependente;
    }

    public void setQtdDependente(int qtdDependente) {
        this.qtdDependente = qtdDependente;
    }
    
}
