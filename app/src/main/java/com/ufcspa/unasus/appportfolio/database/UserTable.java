package com.ufcspa.unasus.appportfolio.database;

/**
 * Created by Desenvolvimento on 29/10/2015.
 */
public class UserTable {

    public static String create(){
        return "CREATE TABLE `User` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`name`\tTEXT NOT NULL,\n" +
                "\t`cpf`\tTEXT NOT NULL,\n" +
                "\t`type`\tTEXT NOT NULL,\n" +
                "\t`userName`\tTEXT NOT NULL,\n" +
                "\t`password`\tTEXT NOT NULL\n" +
                ")";
    }


}
