package br.com.alexandre_salgueirinho.ique_fome;

public class Usuario {
    private String usuarioId, nome, sobrenome, telefone, celular, email, tipoUsuario, indicado, CEP, cidade, rua, complemento, cargo;

    public Usuario(){
    }

    public Usuario(String usuarioId, String nome, String sobrenome, String telefone, String celular, String email, String tipoUsuario, String indicado, String CEP, String cidade, String rua, String complemento, String cargo) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.celular = celular;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.indicado = indicado;
        this.CEP = CEP;
        this.cidade = cidade;
        this.rua = rua;
        this.complemento = complemento;
        this.cargo = cargo;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getIndicado() {
        return indicado;
    }

    public void setIndicado(String indicado) {
        this.indicado = indicado;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
