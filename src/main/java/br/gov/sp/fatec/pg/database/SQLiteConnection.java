package br.gov.sp.fatec.pg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Classe responsável pela Infraestrutura do Banco de Dados.
 * Gerencia a conexão com o arquivo físico (.db) e cria a estrutura das tabelas (DDL Data Definition Language).
 */
public class SQLiteConnection {

    // Define o caminho e o nome do arquivo do banco. 
    // "jdbc:sqlite" diz ao Java qual driver usar.
    private static final String URL = "jdbc:sqlite:ong.db";

    /**
     * Método utilitário para abrir uma conexão.
     * É usado por todos os Repositórios quando precisam salvar ou ler dados.
     */
    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    /**
     * Método de Inicialização (Setup).
     * Chamado no Main.java assim que o sistema liga
     * Garante que as tabelas existam antes de qualquer operação.
     */
    public static void createTables() {
        
        //Definição da Tabela de USUÁRIOS
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "username TEXT UNIQUE NOT NULL, " +        
                "password TEXT NOT NULL, " +               // Senha (será salvo o Hash criptografado)
                "role TEXT NOT NULL DEFAULT 'USER', " +    // Nível de acesso (USER ou ADMIN)
                "token TEXT);";                            // Guarda a sessão do usuário logado

        //Definição da Tabela de DOAÇÕES
        String sqlDoacoes = "CREATE TABLE IF NOT EXISTS doacoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                
                "quantidade TEXT, " +  
                "destino TEXT, " +    
                
                "recebido BOOLEAN DEFAULT FALSE, " + // Status: 0 (Pendente) ou 1 (Entregue)
                "userId INTEGER NOT NULL, " +       
                
                // CHAVE ESTRANGEIRA (Foreign Key):
                "FOREIGN KEY (userId) REFERENCES users(id));";

        // Bloco de Execução
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // Envia os comandos SQL para o arquivo do banco
            stmt.execute(sqlUsers);
            stmt.execute(sqlDoacoes);
            System.out.println("Banco de dados configurado com Logística.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}