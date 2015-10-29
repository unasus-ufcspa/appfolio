package com.ufcspa.unasus.appportfolio.database;

/**
 * Created by Desenvolvimento on 29/10/2015.
 */
public class ClassTable {

    public static String create(){
        return "CREATE TABLE `Class` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`code`\tTEXT NOT NULL,\n" +
                "\t`description`\tTEXT NOT NULL,\n" +
                "\t`status`\tTEXT,\n" +
                "\t`date_start`\tTEXT,\n" +
                "\t`date_end`\tTEXT\n" +
                ")";
    }




}
