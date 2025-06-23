package com.desafio.dev.itau.jr.exceptions;

public class UnprocessableEntity extends RuntimeException{

    public UnprocessableEntity(String mensagem) {
        super(mensagem);
    }
    
}
