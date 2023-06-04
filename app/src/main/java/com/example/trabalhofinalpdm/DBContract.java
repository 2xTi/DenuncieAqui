package com.example.trabalhofinalpdm;

public final class DBContract {

    private DBContract() {}

        public static class DenunciaEntry {
            public static final String TABLE_NAME = "denuncias_local";
            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_USER_ID = "user_id";
            public static final String COLUMN_DENUNCIA = "denuncia";
            public static final String COLUMN_IDENTIFY = "identify";

            public static final String CREATE_TABLE =
                    "CREATE TABLE " + TABLE_NAME + " (" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            COLUMN_USER_ID + " TEXT," +
                            COLUMN_DENUNCIA + " TEXT," +
                            COLUMN_IDENTIFY + " INTEGER)";
            public static final String DELETE_TABLE =
                    "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }
