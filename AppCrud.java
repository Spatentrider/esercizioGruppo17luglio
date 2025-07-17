package com.example;

import java.sql.*;
import java.util.Scanner;

public class AppCrud {
    static final String URL = "jdbc:mysql://localhost:3306/sakila";
    static final String USER = "";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        // CREATE

        try (Scanner intScanner = new Scanner(System.in);
                Scanner stringScanner = new Scanner(System.in);) {

            int scelta;
            do {

                System.out.println("Menu");
                System.out.println("'1' inserisci utente");
                System.out.println("'2' modifica utente");
                System.out.println("'3' cancella utente");
                System.out.println("'4' stampa utenti");
                System.out.println("'5' exit");
                System.out.println("Scegli l'operazione che vuoi effettuare.");
                scelta = intScanner.nextInt();

                switch (scelta) {
                    case 1:
                        System.out.println("Inserisci nome utente:");
                        String nome = stringScanner.nextLine();
                        System.out.println("Inserisci email:");
                        String email = stringScanner.nextLine();
                        if (validateEmail(email) && validateNome(nome)) {
                            insertUtente(nome, email);
                            System.out.println("Utente inserito con successo");
                            break;
                        }
                        System.out.println("Dati non validi");

                        break;
                    case 2:
                        System.err.println("Lista utenti");
                        readUtenti();
                        System.out.println("Inserisci id utente da modficare:");
                        int id_utente = intScanner.nextInt();
                        System.out.println("Inserisci nuovo nome:");
                        String nomeAggiornato = stringScanner.nextLine();
                        if (validateNome(nomeAggiornato)) {
                            updateUtente(id_utente, nomeAggiornato);
                            System.out.println("Utente inserito con successo");
                            break;
                        }
                        System.out.println("Dati non validi");

                        break;
                    case 3:
                        System.out.println("Inserisci id utente da cancellare:");
                        id_utente = intScanner.nextInt();
                        // richiesta conferma -> dati utente relativi a l'id
                        deleteUtente(id_utente);
                        break;
                    case 4:
                        readUtenti();
                        break;
                    case 5:
                        System.out.println("Uscita programma ..");
                        break;
                    default:
                        System.out.println("Scelta non valida");
                        break;
                }

            } while (scelta != 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // insertUtente("Mario Rossi", "mario@example.com");

        // READ
        // readUtenti();

        // UPDATE
        // updateUtente(1, "Mario Bianchi");

        // DELETE
        // deleteUtente(1);
    }

    // CREATE
    public static void insertUtente(String nome, String email) {
        String sql = "INSERT INTO utenti (nome, email) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("Utente inserito.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public static void readUtenti() {
        String sql = "SELECT * FROM utenti";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Nome: " + rs.getString("nome") +
                        ", Email: " + rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public static void updateUtente(int id, String nuovoNome) {
        String sql = "UPDATE utenti SET nome = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuovoNome);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println("Utente aggiornato.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public static void deleteUtente(int id) {
        String sql = "DELETE FROM utenti WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Utente eliminato.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateNome(String nome) {
        // La regex corretta che include solo lettere, numeri e spazi.
        String regex = "^[a-zA-Z0-9 ]*$"; // Simplified regex

        // Controlla che il nome non sia nullo, che la lunghezza sia corretta
        // e che CORRISPONDA alla regex.
        if (nome != null && nome.length() <= 50 && nome.matches(regex)) {
            return true; // Il nome è valido
        }

        return false; // Il nome non è valido
    }

    public static boolean validateEmail(String email) {
        boolean validate = false;
        if (email != null && email.length() <= 50
                && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            validate = true;
        }
        return validate;
    }
}
