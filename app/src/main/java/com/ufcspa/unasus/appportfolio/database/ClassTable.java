package com.ufcspa.unasus.appportfolio.database;

/**
 * Created by Desenvolvimento on 29/10/2015.
 */
public class ClassTable {

    public static String create(){
        return "-- Creator:       MySQL Workbench 6.3.5/ExportSQLite Plugin 0.1.0\n" +
                "-- Author:        UNA-SUS\n" +
                "-- Caption:       New Model\n" +
                "-- Project:       Name of the project\n" +
                "-- Changed:       2015-11-05 18:59\n" +
                "-- Created:       2015-11-05 18:51\n" +
                "PRAGMA foreign_keys = OFF;\n" +
                "\n" +
                "-- Schema: db_portfolio\n" +
                "ATTACH \"db_portfolio.sdb\" AS \"db_portfolio\";\n" +
                "BEGIN;\n" +
                "CREATE TABLE \"tb_portfolio\"(\n" +
                "  \"id_portfolio\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"ds_title\" VARCHAR(255) DEFAULT NULL,\n" +
                "  \"ds_description\" LONGTEXT DEFAULT NULL,\n" +
                "  \"nu_portfolio_version\" VARCHAR(15) DEFAULT NULL\n" +
                ");\n" +
                "CREATE TABLE \"tb_activity\"(\n" +
                "  \"id_activity\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_portfolio\" INTEGER NOT NULL,\n" +
                "  \"nu_order\" INTEGER DEFAULT NULL,\n" +
                "  \"ds_title\" VARCHAR(255) DEFAULT NULL,\n" +
                "  \"ds_description\" LONGTEXT DEFAULT NULL,\n" +
                "  CONSTRAINT \"fk_tb_atividade_tb_portfolio\"\n" +
                "    FOREIGN KEY(\"id_portfolio\")\n" +
                "    REFERENCES \"tb_portfolio\"(\"id_portfolio\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_activity.fk_tb_atividade_tb_portfolio_idx\" ON \"tb_activity\" (\"id_portfolio\");\n" +
                "CREATE TABLE \"tb_class\"(\n" +
                "  \"id_class\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"ds_code\" VARCHAR(45) DEFAULT NULL,\n" +
                "  \"ds_description\" LONGTEXT DEFAULT NULL,\n" +
                "  \"st_status\" CHAR(1) DEFAULT NULL,\n" +
                "  \"dt_start\" DATE DEFAULT NULL,\n" +
                "  \"dt_finish\" DATE DEFAULT NULL\n" +
                ");\n" +
                "CREATE TABLE \"tb_user\"(\n" +
                "  \"id_user\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"nm_user\" VARCHAR(80) DEFAULT NULL,\n" +
                "  \"nu_identification\" VARCHAR(45) DEFAULT NULL,\n" +
                "  \"tp_user\" CHAR(1) DEFAULT NULL,\n" +
                "--   User type:\n" +
                "--   P = Proposer;\n" +
                "--   A = Administrator;\n" +
                "--   S = Student;\n" +
                "--   T = Tutor;\n" +
                "  \"ds_email\" VARCHAR(255) DEFAULT NULL,\n" +
                "  \"ds_password\" VARCHAR(64) DEFAULT NULL,\n" +
                "  \"nu_cellphone\" VARCHAR(20) DEFAULT NULL\n" +
                ");\n" +
                "CREATE TABLE \"tb_portfolio_class\"(\n" +
                "  \"id_portfolio_class\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_portfolio\" INTEGER NOT NULL,\n" +
                "  \"id_class\" INTEGER NOT NULL,\n" +
                "  CONSTRAINT \"fk_tb_portfolio_class_id_class\"\n" +
                "    FOREIGN KEY(\"id_class\")\n" +
                "    REFERENCES \"tb_class\"(\"id_class\"),\n" +
                "  CONSTRAINT \"fk_tb_portfolio_class_id_portfolio\"\n" +
                "    FOREIGN KEY(\"id_portfolio\")\n" +
                "    REFERENCES \"tb_portfolio\"(\"id_portfolio\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_portfolio_class.fk_tb_portfolio_class_id_portfolio_idx\" ON \"tb_portfolio_class\" (\"id_portfolio\");\n" +
                "CREATE INDEX \"tb_portfolio_class.fk_tb_portfolio_class_id_class_idx\" ON \"tb_portfolio_class\" (\"id_class\");\n" +
                "CREATE TABLE \"tb_portfolio_student\"(\n" +
                "  \"id_portfolio_student\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_student\" INTEGER NOT NULL,\n" +
                "  \"id_tutor\" INTEGER NOT NULL,\n" +
                "  \"id_portfolio_class\" INTEGER DEFAULT NULL,\n" +
                "  \"dt_first_sync\" DATE DEFAULT NULL,\n" +
                "  \"nu_portfolio_version\" VARCHAR(15) DEFAULT NULL,\n" +
                "  CONSTRAINT \"fk_tb_portfolio_student_tb_portfolio_class1\"\n" +
                "    FOREIGN KEY(\"id_portfolio_class\")\n" +
                "    REFERENCES \"tb_portfolio_class\"(\"id_portfolio_class\"),\n" +
                "  CONSTRAINT \"fk_tb_portfolio_student_tb_user1\"\n" +
                "    FOREIGN KEY(\"id_student\")\n" +
                "    REFERENCES \"tb_user\"(\"id_user\"),\n" +
                "  CONSTRAINT \"fk_tb_portfolio_student_tb_user2\"\n" +
                "    FOREIGN KEY(\"id_tutor\")\n" +
                "    REFERENCES \"tb_user\"(\"id_user\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_portfolio_student.fk_tb_portfolio_student_tb_user2_idx\" ON \"tb_portfolio_student\" (\"id_tutor\");\n" +
                "CREATE INDEX \"tb_portfolio_student.fk_tb_portfolio_student_tb_user1_idx\" ON \"tb_portfolio_student\" (\"id_student\");\n" +
                "CREATE INDEX \"tb_portfolio_student.fk_tb_portfolio_student_tb_portfolio_class1_idx\" ON \"tb_portfolio_student\" (\"id_portfolio_class\");\n" +
                "CREATE TABLE \"tb_class_tutor\"(\n" +
                "--   Tabela para armazenar os usuários que poderão ser cadastrados como tutor em uma turma. Tabela usada como referência para associação de tutores aos alunos de uma turma.\n" +
                "  \"id_class_tutor\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,-- Identificador da tabela.\n" +
                "  \"id_class\" INTEGER DEFAULT NULL,-- Chave estrangeira para a tabela \"tb_class\" para identificar a turma dos tutores.\n" +
                "  \"id_tutor\" INTEGER DEFAULT NULL,-- Chave estrangeira para a tabela \"tb_user\" para identificar os tutores. Somente deve ser preenchida com usuários do tipo 'T'.\n" +
                "  CONSTRAINT \"fk_tb_class_tutor_tb_class1\"\n" +
                "    FOREIGN KEY(\"id_class\")\n" +
                "    REFERENCES \"tb_class\"(\"id_class\"),\n" +
                "  CONSTRAINT \"fk_tb_class_tutor_tb_user1\"\n" +
                "    FOREIGN KEY(\"id_tutor\")\n" +
                "    REFERENCES \"tb_user\"(\"id_user\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_class_tutor.fk_tb_class_tutor_tb_class1_idx\" ON \"tb_class_tutor\" (\"id_class\");\n" +
                "CREATE INDEX \"tb_class_tutor.fk_tb_class_tutor_tb_user1_idx\" ON \"tb_class_tutor\" (\"id_tutor\");\n" +
                "CREATE TABLE \"tb_activity_student\"(\n" +
                "  \"id_activity_student\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_portfolio_student\" INTEGER NOT NULL,\n" +
                "  \"id_activity\" INTEGER NOT NULL,\n" +
                "  \"tx_activity\" LONGTEXT DEFAULT NULL,\n" +
                "  \"dt_first_sync\" DATE DEFAULT NULL,\n" +
                "  \"dt_last_access\" DATE DEFAULT NULL,\n" +
                "  \"dt_conclusion\" DATE DEFAULT NULL,\n" +
                "  CONSTRAINT \"fk_tb_activity_student_tb_activity1\"\n" +
                "    FOREIGN KEY(\"id_activity\")\n" +
                "    REFERENCES \"tb_activity\"(\"id_activity\"),\n" +
                "  CONSTRAINT \"fk_tb_activity_student_tb_portfolio_student1\"\n" +
                "    FOREIGN KEY(\"id_portfolio_student\")\n" +
                "    REFERENCES \"tb_portfolio_student\"(\"id_portfolio_student\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_activity_student.fk_tb_activity_student_tb_portfolio_student1_idx\" ON \"tb_activity_student\" (\"id_portfolio_student\");\n" +
                "CREATE INDEX \"tb_activity_student.fk_tb_activity_student_tb_activity1_idx\" ON \"tb_activity_student\" (\"id_activity\");\n" +
                "CREATE TABLE \"tb_class_student\"(\n" +
                "  \"id_class_student\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,-- Identificador da tabela.\n" +
                "  \"id_class\" INTEGER DEFAULT NULL,-- Chave estrangeira para a tabela \"tb_class\" para identificar a turma dos alunos.\n" +
                "  \"id_student\" INTEGER DEFAULT NULL,-- Chave estrangeira para a tabela \"tb_user\" para identificar os alunos. Somente deve ser preenchida com usuários do tipo 'S'.\n" +
                "  CONSTRAINT \"fk_tb_class_student_tb_class1\"\n" +
                "    FOREIGN KEY(\"id_class\")\n" +
                "    REFERENCES \"tb_class\"(\"id_class\"),\n" +
                "  CONSTRAINT \"fk_tb_class_student_tb_user1\"\n" +
                "    FOREIGN KEY(\"id_student\")\n" +
                "    REFERENCES \"tb_user\"(\"id_user\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_class_student.fk_tb_class_student_tb_class1_idx\" ON \"tb_class_student\" (\"id_class\");\n" +
                "CREATE INDEX \"tb_class_student.fk_tb_class_student_tb_user1_idx\" ON \"tb_class_student\" (\"id_student\");\n" +
                "CREATE TABLE \"tb_reference\"(\n" +
                "  \"id_reference\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_activity_student\" INTEGER NOT NULL,\n" +
                "  \"ds_url\" VARCHAR(500) DEFAULT NULL,\n" +
                "  CONSTRAINT \"fk_tb_reference_tb_activity_student1\"\n" +
                "    FOREIGN KEY(\"id_activity_student\")\n" +
                "    REFERENCES \"tb_activity_student\"(\"id_activity_student\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_reference.fk_tb_reference_tb_activity_student1_idx\" ON \"tb_reference\" (\"id_activity_student\");\n" +
                "CREATE TABLE \"tb_comment\"(\n" +
                "  \"id_comment\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_activity_student\" INTEGER NOT NULL,\n" +
                "  \"id_author\" INTEGER NOT NULL,\n" +
                "  \"tx_comment\" VARCHAR(255) DEFAULT NULL,\n" +
                "  \"tx_reference\" LONGTEXT DEFAULT NULL,\n" +
                "  \"tp_comment\" CHAR(1) DEFAULT NULL,\n" +
                "  \"dt_comment\" DATE DEFAULT NULL,\n" +
                "  CONSTRAINT \"fk_tb_comment_tb_activity_student1\"\n" +
                "    FOREIGN KEY(\"id_activity_student\")\n" +
                "    REFERENCES \"tb_activity_student\"(\"id_activity_student\"),\n" +
                "  CONSTRAINT \"fk_tb_comment_tb_user1\"\n" +
                "    FOREIGN KEY(\"id_author\")\n" +
                "    REFERENCES \"tb_user\"(\"id_user\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_comment.fk_tb_comment_tb_activity_student1_idx\" ON \"tb_comment\" (\"id_activity_student\");\n" +
                "CREATE INDEX \"tb_comment.fk_tb_comment_tb_user1_idx\" ON \"tb_comment\" (\"id_author\");\n" +
                "CREATE TABLE \"tb_attachment\"(\n" +
                "  \"id_attachment\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"id_comment\" INTEGER NOT NULL,\n" +
                "  \"id_activity_comment\" INTEGER NOT NULL,\n" +
                "  \"ds_local_path\" VARCHAR(500) DEFAULT NULL,\n" +
                "  \"ds_server_path\" VARCHAR(500) DEFAULT NULL,\n" +
                "  CONSTRAINT \"fk_tb_attachment_tb_activity_student1\"\n" +
                "    FOREIGN KEY(\"id_activity_comment\")\n" +
                "    REFERENCES \"tb_activity_student\"(\"id_activity_student\"),\n" +
                "  CONSTRAINT \"fk_tb_attachment_tb_comment1\"\n" +
                "    FOREIGN KEY(\"id_comment\")\n" +
                "    REFERENCES \"tb_comment\"(\"id_comment\")\n" +
                ");\n" +
                "CREATE INDEX \"tb_attachment.fk_tb_attachment_tb_activity_student1_idx\" ON \"tb_attachment\" (\"id_activity_comment\");\n" +
                "CREATE INDEX \"tb_attachment.fk_tb_attachment_tb_comment1_idx\" ON \"tb_attachment\" (\"id_comment\");\n" +
                "COMMIT;\n";
    }

    public static String insertValues(){
        return "--\n" +
                "-- Dumping data for table `tb_user`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (1,'João','123','A','joao@port.com','123',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (2,'Maria','456','P','maria@port.com','456',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (3,'Paulo','789','T','paulo@port.com','789',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (4,'Luciana','111','T','luciana@port.com','111',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (5,'Mario','222','T','mario@port.com','222',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (6,'Joana','333','S','joana@port.com','333',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (7,'Ricardo','444','S','ricardo@port.com','444',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (8,'Eduardo','555','S','eduardo@port.com','555',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (9,'Arthur','666','S','arthur@port.com','666',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (10,'Icaro','777','S','icaro@port.com','777',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (11,'Greta','888','S','greta@port.com','888',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (12,'Marcus','999','S','marcus@port.com','999',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (13,'Alessandra','112','S','alessandra@port.com','112',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (14,'Paula','221','S','paula@port.com','221',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (15,'Elissandra','121','S','elis@port.com','121',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (16,'Diego','212','S','diego@port.com','212',NULL);\n" +
                "INSERT INTO tb_user (id_user, nm_user, nu_identification, tp_user, ds_email, ds_password, nu_cellphone) VALUES (17,'Carlos','113','S','carlos@port.com','113',NULL);\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_class`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_class (id_class, ds_code, ds_description, st_status, dt_start, dt_finish) VALUES (1,'T001','Turma 1','A',NULL,NULL);\n" +
                "INSERT INTO tb_class (id_class, ds_code, ds_description, st_status, dt_start, dt_finish) VALUES (2,'T002','Turma 2','A',NULL,NULL);\n" +
                "INSERT INTO tb_class (id_class, ds_code, ds_description, st_status, dt_start, dt_finish) VALUES (3,'T003','Turma 3','A',NULL,NULL);\n" +
                "INSERT INTO tb_class (id_class, ds_code, ds_description, st_status, dt_start, dt_finish) VALUES (4,'T004','Turma 4','A',NULL,NULL);\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_portfolio`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_portfolio (id_portfolio, ds_title, ds_description, nu_portfolio_version) VALUES (1,'Portfólio da Turma 1 - 1o semestre 2015','Portfólio contendo as atividades da Turma 1',NULL);\n" +
                "INSERT INTO tb_portfolio (id_portfolio, ds_title, ds_description, nu_portfolio_version) VALUES (2,'Portfólio da Turma 1 - 2o semestre 2015','Portfólio contendo as atividades da Turma 1',NULL);\n" +
                "INSERT INTO tb_portfolio (id_portfolio, ds_title, ds_description, nu_portfolio_version) VALUES (3,'Portfólio da Turma 2','Portfólio contendo as atividades da Turma 2',NULL);\n" +
                "INSERT INTO tb_portfolio (id_portfolio, ds_title, ds_description, nu_portfolio_version) VALUES (4,'Portfólio da Turma 3','Portfólio contendo as atividades da Turma 3',NULL);\n" +
                "INSERT INTO tb_portfolio (id_portfolio, ds_title, ds_description, nu_portfolio_version) VALUES (5,'Portfólio da Turma 4','Portfólio contendo as atividades da Turma 4',NULL);\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_activity`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (1,1,1,'Introdução','Nesta atividade o aluno deve dissertar sobre...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (2,1,3,'Estudo de caso clínico','O estudo de caso clínico serve para...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (3,1,2,'Composição da unidade','A descrição da unidade é importante no sentido de...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (4,2,1,'Introdução','Nesta atividade o aluno deve dissertar sobre...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (5,2,2,'Ambientação','A tarefa de ambientação é necessária...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (6,3,1,'Introdução','Nesta atividade o aluno deve dissertar sobre...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (7,3,2,'Composição da unidade','A descrição da unidade é importante no sentido de...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (8,4,1,'Introdução','Nesta atividade o aluno deve dissertar sobre...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (9,4,2,'Projeto de intervenção','Projeto de intervenção de acordo com...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (10,5,1,'Introdução','Nesta atividade o aluno deve dissertar sobre...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (11,5,3,'Estudo de caso clínico','O estudo de caso clínico serve para...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (12,5,4,'Composição da unidade','A descrição da unidade é importante no sentido de...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (13,5,2,'Ambientação','A tarefa de ambientação é necessária...');\n" +
                "INSERT INTO tb_activity (id_activity, id_portfolio, nu_order, ds_title, ds_description) VALUES (14,5,5,'Projeto de intervenção','Projeto de intervenção de acordo com...');\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_portfolio_class`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_portfolio_class (id_portfolio_class, id_portfolio, id_class) VALUES (1,1,1);\n" +
                "INSERT INTO tb_portfolio_class (id_portfolio_class, id_portfolio, id_class) VALUES (2,2,1);\n" +
                "INSERT INTO tb_portfolio_class (id_portfolio_class, id_portfolio, id_class) VALUES (3,3,2);\n" +
                "INSERT INTO tb_portfolio_class (id_portfolio_class, id_portfolio, id_class) VALUES (4,4,3);\n" +
                "INSERT INTO tb_portfolio_class (id_portfolio_class, id_portfolio, id_class) VALUES (5,5,4);\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_portfolio_student`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (1,6,3,1,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (2,7,3,1,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (3,8,4,1,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (4,9,4,1,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (5,10,5,2,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (6,11,5,2,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (7,12,3,2,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (8,13,3,3,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (9,14,4,3,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (10,15,5,4,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (11,16,5,4,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (12,17,4,4,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (13,6,5,3,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (14,7,4,3,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (15,11,4,5,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (16,12,4,5,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (17,13,4,5,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (18,16,5,5,NULL,NULL);\n" +
                "INSERT INTO tb_portfolio_student (id_portfolio_student, id_student, id_tutor, id_portfolio_class, dt_first_sync, nu_portfolio_version) VALUES (19,17,5,5,NULL,NULL);\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_activity_student`\n" +
                "--\n" +
                "\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (1,1,1,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (2,1,3,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (3,1,2,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (4,2,1,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (5,2,3,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (6,2,2,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (7,3,1,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (8,3,3,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (9,3,2,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (10,4,1,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (11,4,3,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (12,4,2,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (16,5,4,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (17,5,5,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (18,6,4,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (19,6,5,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (20,7,4,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (21,7,5,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (23,8,6,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (24,8,7,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (25,9,6,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (26,9,7,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (27,13,6,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (28,13,7,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (29,14,6,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (30,14,7,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (38,10,8,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (39,10,9,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (40,11,8,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (41,11,9,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (42,12,8,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (43,12,9,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (52,15,10,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (53,15,13,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (54,15,11,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (55,15,12,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (56,15,14,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (57,16,10,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (58,16,13,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (59,16,11,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (60,16,12,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (61,16,14,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (62,17,10,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (63,17,13,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (64,17,11,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (65,17,12,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (66,17,14,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (67,18,10,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (68,18,13,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (69,18,11,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (70,18,12,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (71,18,14,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (72,19,10,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (73,19,13,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (74,19,11,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (75,19,12,NULL,NULL,NULL,NULL);\n" +
                "INSERT INTO tb_activity_student (id_activity_student, id_portfolio_student, id_activity, tx_activity, dt_first_sync, dt_last_access, dt_conclusion) VALUES (76,19,14,NULL,NULL,NULL,NULL);\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_attachment`\n" +
                "--\n" +
                "\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_class_student`\n" +
                "--\n" +
                "\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_class_tutor`\n" +
                "--\n" +
                "\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_comment`\n" +
                "--\n" +
                "\n" +
                "\n" +
                "\n" +
                "--\n" +
                "-- Dumping data for table `tb_reference`\n" +
                "--\n" +
                "\n";
    }




}
