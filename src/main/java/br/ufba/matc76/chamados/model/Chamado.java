package br.ufba.matc76.chamados.model;

public record Chamado(
    Long id,
    String titulo,
    String solicitante,
    String setor,
    Prioridade prioridade,
    Status status
){}
