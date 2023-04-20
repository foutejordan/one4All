package com.avignon.university.one4all.models;

import java.util.ArrayList;

public class QueryResponse {
    public String message;
    public ArrayList<Object> response;
    public ResponseState state;

    public QueryResponse() {
        message = "Erreur lors de l'exécution de la requête";
        response = null;
        state = ResponseState.ERROR;
    }
}
