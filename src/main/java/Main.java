import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Conectar ao banco de dados SQLite:
            connection = DriverManager.getConnection("jdbc:sqlite:teste.db");
            System.out.println("Conexão com SQLite estabelecida!");

            // Criar uma tabela (usuario):
            String createTableSQL = 
            """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    nome VARCHAR(256) NOT NULL, 
                    nascimento TEXT
                );
            """;
            // Criar e executar uma declaração SQL:
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Tabela 'usuario' criada ou já existe!");

            // Inserir dados na tabela 'usuario':
            String insertSQL = 
            """
                INSERT INTO usuario (nome, nascimento) VALUES 
                ('Ana', '2000-06-03'), 
                ('Bruna', '2001-02-17'),
                ('Carlos', '2002-04-21'),
                ('Daniel', '2003-10-30');
            """;
            statement.execute(insertSQL);
            System.out.println("Dados inseridos na tabela 'usuario'!");

            // Consultar dados da tabela 'usuario':
            String selectSQL = "SELECT * FROM usuario;";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + 
                                   ", Nome: " + resultSet.getString("nome") +
                                   ", Nascimento: " + resultSet.getString("nascimento"));
            }

            // Atualizar registros no banco de dados:
            String updateSQL = "UPDATE usuario SET nome = ?, nascimento = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            // Estrutura de repetição para atualizar usuários:
            String continuar;
            do {
                // Pedir o ID do usuário que se deseja atualizar:
                System.out.print("Informe o ID do usuário que se deseja atualizar: ");
                int id = Integer.parseInt(scanner.nextLine());
                // Pedir o novo nome e data de nascimento do usuário:
                System.out.print("Informe o novo nome do usuário: ");
                String nome = scanner.nextLine();
                System.out.print("Informe a nova data de nascimento (AAAA-MM-DD): ");
                String nascimento = scanner.nextLine();

                // Definir os valores no PreparedStatement:
                preparedStatement.setString(1, nome);
                preparedStatement.setString(2, nascimento);
                preparedStatement.setInt(3, id);
                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Usuário atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum usuário encontrado com o ID especificado.");
                }

                // Perguntar se deseja atualizar outro usuário:
                System.out.print("Deseja atualizar outro usuário? (sim/não): ");
                continuar = scanner.nextLine();
            } while (continuar.equalsIgnoreCase("sim"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
