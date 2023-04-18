package com.avignon.university.one4all.models;

public class QueryResponse {
    public String message;
    public Object response;
    public ResponseState state;

    public QueryResponse() {
        message = "Erreur lors de l'exécution de la requête";
        response = null;
        state = ResponseState.ERROR;
    }
}
