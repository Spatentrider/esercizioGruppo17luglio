package com.example;

import java.sql.*;
import java.util.Scanner;

public class AppCrud {
    //variabili per consentire connessione 
    static final String URL = "jdbc:mysql://localhost:3306/sakila";
    static final String USER = "";
    static final String PASSWORD = "";

    public static void main(String[] args) {
    
        //scanner per prendere input utente
        try (Scanner intScanner = new Scanner(System.in);
                Scanner stringScanner = new Scanner(System.in);) {
            //ciclo do-while per prendere input utente e scegliere varie azioni
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
                    case 1://aggiunta utente al database con input nome ed email ell'utente 
                        System.out.println("Inserisci nome utente:");
                        String nome = stringScanner.nextLine();
                        System.out.println("Inserisci email:");
                        String email = stringScanner.nextLine();
                        //se email e nome validi inserisci utente
                        if (validateEmail(email) && validateNome(nome)) {
                            insertUtente(nome, email);
                            System.out.println("Utente inserito con successo");
                            break;
                        }//altrimenti stampa di errore
                        System.out.println("Dati non validi");

                        break;
                    case 2://modifica utenti
                        //stampa della lista degli utenti già presentu
                        System.err.println("Lista utenti");
                        readUtenti();
                        //richiesta inserimento id e nuovo nome
                        System.out.println("Inserisci id utente da modficare:");
                        int id_utente = intScanner.nextInt();
                        System.out.println("Inserisci nuovo nome:");
                        String nomeAggiornato = stringScanner.nextLine();
                        //se nome valida aggiorna utente
                        if (validateNome(nomeAggiornato)) {
                            updateUtente(id_utente, nomeAggiornato);
                            System.out.println("Utente inserito con successo");
                            break;
                        }/altrimenti stampa di errore
                        System.out.println("Dati non validi");

                        break;
                    case 3://cancella utente
                        //richiesta inserimento id dell'utente da cancellare
                        System.out.println("Inserisci id utente da cancellare:");
                        id_utente = intScanner.nextInt();
                        //cancella utente
                        deleteUtente(id_utente);
                        break;
                    case 4://stampa lista utenti
                        readUtenti();
                        break;
                    case 5://uscita dal programma
                        System.out.println("Uscita programma ..");
                        break;
                    default://stampa di errore scelta non valida
                        System.out.println("Scelta non valida");
                        break;
                }

            } while (scelta != 5);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // CREATE
    public static void insertUtente(String nome, String email) {
        //query per inserire utente
        String sql = "INSERT INTO utenti (nome, email) VALUES (?, ?)";
        //try connessione e creazione prepared statemnt con query
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            //controllo dei dati dello statement
            stmt.setString(1, nome);
            stmt.setString(2, email);
            //esecuzione query
            stmt.executeUpdate();
            System.out.println("Utente inserito.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public static void readUtenti() {
        //query per selezionare tutti gli utenti
        String sql = "SELECT * FROM utenti";
        //try connessione e creazione prepared statemnt con query
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                //esecuzione query
                ResultSet rs = stmt.executeQuery(sql)) {

            //while per ciclare tutti gli utenti e stampare id, nome ed email di ciascuno
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
        //query per aggiornare utenti
        String sql = "UPDATE utenti SET nome = ? WHERE id = ?";
        //try connessione e creazione prepared statemnt con query
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            //controllo dei dati dello statement
            stmt.setString(1, nuovoNome);
            stmt.setInt(2, id);
            //esecuzione query
            stmt.executeUpdate();
            System.out.println("Utente aggiornato.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public static void deleteUtente(int id) {
        //query per eliminare utente
        String sql = "DELETE FROM utenti WHERE id = ?";
        //try connessione e creazione prepared statemnt con query
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            //controllo dati dello statement
            stmt.setInt(1, id);
            //esecuzione query
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
        //se mail non è nulla, minore di 50 caratteri e rispetta la regex booleano è true
        if (email != null && email.length() <= 50
                && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            validate = true;
        }
        return validate;
    }
}
