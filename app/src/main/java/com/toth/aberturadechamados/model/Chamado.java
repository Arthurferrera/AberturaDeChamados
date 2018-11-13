package com.toth.aberturadechamados.model;

public class Chamado {

    private int id;
    private String titulo;
    private String mensagem;
    private Boolean status;
    private String data;
    private String nomeUsuario;
    private String nomeEmpresa;
    private String observacao;
    private String dataObservacao;
    private String imagem;
    private int idUsuarioChamado;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getObservacao() { return observacao; }

    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getDataObservacao() { return dataObservacao; }

    public void setDataObservacao(String dataObservacao) { this.dataObservacao = dataObservacao; }

    public int getIdUsuarioChamado() { return idUsuarioChamado; }

    public void setIdUsuarioChamado(int idUsuarioChamado) { this.idUsuarioChamado = idUsuarioChamado; }

    public String getImagem() { return imagem; }

    public void setImagem(String imagem) { this.imagem = imagem; }
}
