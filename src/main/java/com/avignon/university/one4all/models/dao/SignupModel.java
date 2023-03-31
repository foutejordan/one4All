package com.avignon.university.one4all.models.dao;

import java.sql.Types;

public class SignupModel {

    public static int signup(String login, String password, int role) {

        int id = (int) CRUDHelper.create(
                "Users",
                new String[]{"login", "password", "role"},
                new Object[]{login, password, role},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.INTEGER});

        return id;
    }
}
