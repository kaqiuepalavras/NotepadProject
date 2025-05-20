// app/java/com.example.notepad/model/Nota.java
package com.example.notepad.model; // AJUSTE O NOME DO PACOTE SE FOR DIFERENTE

import java.io.Serializable; // Para poder passar objetos Nota entre Activities (opcional, mas útil)

public class Nota implements Serializable { // Implementar Serializable é uma boa prática
    private int id;
    private String titulo;
    private String conteudo;

    public Nota() {
        // Construtor vazio
    }

    public Nota(int id, String titulo, String conteudo) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo; // <<-- CORREÇÃO IMPORTANTE: GARANTA QUE ESTA LINHA ESTEJA CORRETA
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    // Opcional: Sobrescrever toString() para debug
    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", conteudo='" + conteudo + '\'' +
                '}';
    }
}