package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.User;
import com.github.javafaker.Faker;

import java.sql.Types;

public class SignupModel {

    public static int signup(String login, String password, int role) {

        int id = (int) CRUDHelper.create(
                "Users",
                new String[]{"login", "password","role"},
                new Object[]{login, password, role},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.INTEGER});

        return id;
    }

    public static int signup(User user) {

        int id = (int) CRUDHelper.create(
                "Users",
                new String[]{"login", "password", "image","role"},
                new Object[]{user.login, user.password, user.image, user.role},
                new int[]{Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR,Types.INTEGER});

        return id;
    }


}
