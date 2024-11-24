/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import javax.swing.Timer;
import java.util.Date;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

/**
 *
 * @author zikrea
 */

public class Clientgestion extends javax.swing.JFrame {

    private static Clientgestion instanceclientgestion;
    private final Connection connection = Userlogin.Connect();
    private int clientId;

    /**
     * Creates new form Clientgestion
     *
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public Clientgestion() throws ClassNotFoundException, SQLException {
        initComponents();
        TableClient();
        loadDataFromDB();
        reste.setText("Total restant à payé en " + String.valueOf((int) tranchePayment - 1) + " mois");
        startDateTimer();
        Time();
        setLocationRelativeTo(null);

    }

   public static Clientgestion getInstance() throws ClassNotFoundException, SQLException {
    if (instanceclientgestion == null) {  // Utiliser la variable instanceclientgestion directement
        instanceclientgestion = new Clientgestion();
    }
    return instanceclientgestion;  // Retourner la variable et non une méthode
}

   
    public double tranchePayment;
    public double prixParM2;
    public double pourcentage;
    public String tab;
    private SimpleDateFormat timeFormat; // Format de l'heure
    private Timer timer;

    public int getclientId(){
        return clientId;
    }
    public double getTranchePayment() {
        return tranchePayment;
    }

    private void loadDataFromDB() {
        try {
            double[] values = getAdminValues(); // Récupérer les valeurs

            tranchePayment = values[0];
            prixParM2 = values[1];
            pourcentage = values[2];
            System.out.println(getTranchePayment());

            champ_tranche.setText(String.valueOf((int) getTranchePayment()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double[] getAdminValues() throws SQLException {
        String query = "SELECT Tranche_paiement FROM Tranche_paiement";
        String sql = "SELECT Prixmc FROM Prixmc";
        String Sql = "SELECT Pourcentage_paiement FROM Pourcentage_paiement";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statements = connection.prepareStatement(sql);
        PreparedStatement statementss = connection.prepareStatement(Sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSet resultSets = statements.executeQuery();
        ResultSet resultSetss = statementss.executeQuery();
        double tranches = 0.0, prixParM2 = 0.0, pourcentage = 0.0;

        if (resultSet.next() && resultSets.next() && resultSetss.next()) {
            tranches = resultSet.getDouble("Tranche_paiement");
            prixParM2 = resultSets.getDouble("Prixmc");
            pourcentage = resultSetss.getDouble("Pourcentage_paiement");
        }

        return new double[]{tranches, prixParM2, pourcentage};
    }

    // Méthode pour fermer la connexion
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void calculer() {
        try {
            // Récupérer la valeur de mesure entrée par l'utilisateur
            double mesure = Double.parseDouble(champ_mesure_trano.getText());

            // Calcul du prix total
            double prixTotal = (mesure * prixParM2);
            champ_prix_total.setText(String.valueOf((int) prixTotal));

            // Calcul du montant du pourcentage
            double montantPourcentage = ((prixTotal * pourcentage) / 100);
            champ_first_payment.setText(String.valueOf((int) montantPourcentage));

            // Calcul du reste
            double reste = (prixTotal - montantPourcentage);
            champ_resteApayé.setText(String.valueOf((int) reste));

            double resultat = (reste / (getTranchePayment() - 1));
            champ_prix_prmois.setText(String.valueOf((int) resultat));

        } catch (NumberFormatException e) {
            champ_prix_total.setText("");
            champ_first_payment.setText("");
            champ_resteApayé.setText("");
            champ_prix_prmois.setText("");
        }
    }

    private void calculpersonalize() {
        try {
            double mesure = Double.parseDouble(champ_mesure_trano.getText());

            double prixTotal = (mesure * prixParM2);

            double montantPourcentage = ((prixTotal * pourcentage) / 100);
            double value = Double.parseDouble(champ_first_payment.getText());
            System.out.println(String.valueOf(value));
            if (value >= montantPourcentage) {
                btn_valider.setEnabled(true);
            } else {
                btn_valider.setEnabled(false);
            }

            champ_first_payment.setText(String.valueOf((int) value));

            double reste = (prixTotal - value);
            champ_resteApayé.setText(String.valueOf((int) reste));

            double resultat = (reste / (getTranchePayment() - 1));
            champ_prix_prmois.setText(String.valueOf((int) resultat));

        } catch (NumberFormatException e) {
            // Réinitialiser les champs en cas d'erreur de format

            champ_first_payment.setText("");
            champ_resteApayé.setText("");
            champ_prix_prmois.setText("");
        }
    }

    private void TableClient() throws ClassNotFoundException, SQLException {
        String[] colonnes = {"Id Client", "Nom", "Prénoms", "Date de naissance", "Sexe", "Adresse", "CIN", "Téléphone", "Id employé", "Tranche_paiement", "Surface à peindre", "Prix Total", "1er paiement en Ariary", "Total restant", "Reste à payer mensuellement", "Date du 1er Versement"};
        DefaultTableModel model = new DefaultTableModel(null, colonnes);

        String sql = "SELECT * FROM client";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Parcours du ResultSet pour récupérer les données de chaque employé
            while (resultSet.next()) {
                // Récupération des données de chaque colonne
                String[] afficher = new String[16];
                afficher[0] = resultSet.getString("Id_client");
                afficher[1] = resultSet.getString("Nom");
                afficher[2] = resultSet.getString("Prenoms");
                afficher[3] = resultSet.getString("Date_de_naissance");
                afficher[4] = resultSet.getString("Sexe");
                afficher[5] = resultSet.getString("Adresse");
                afficher[6] = resultSet.getString("Cin");
                afficher[7] = resultSet.getString("Telephone");
                afficher[8] = resultSet.getString("Id_employe");
                afficher[9] = resultSet.getString("Tranche_paiement");
                afficher[10] = resultSet.getString("Mesure_trano");
                afficher[11] = resultSet.getString("Prix_total");
                afficher[12] = resultSet.getString("First_payment");
                afficher[13] = resultSet.getString("Reste_apaye");
                afficher[14] = resultSet.getString("Prix_par_mois_reste");
                afficher[15] = resultSet.getString("Date_1er_paiement");

                // Ajout de la ligne au modèle de table
                model.addRow(afficher);
            }
            // Mise à jour du modèle de la JTable
            txtaffiche.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void insertData() throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO client (Nom, Prenoms, Date_de_naissance, Sexe, Adresse, Cin, Telephone, Id_employe,Tranche_paiement, Mesure_trano,Prix_total,First_payment ,Reste_apaye ,Prix_par_mois_reste, Date_1er_paiement) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String nom = champ_nom.getText();
                String prenom = champ_prenoms.getText();
                String birth = champ_birth.getText();
                String sexe = (String) champ_sexe.getSelectedItem();
                String adresse = champ_adresse.getText();
                String cin = champ_cin.getText();
                String phone = champ_phone.getText();
                String employe = champ_id_employe.getText();
                int tranche_paiement = (int) getTranchePayment();
                String mesure_trano = champ_mesure_trano.getText();
                String prix_total = champ_prix_total.getText();
                String first_payment = champ_first_payment.getText();
                String resteApaye = champ_resteApayé.getText();
                String reste_par_mois = champ_prix_prmois.getText();

                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, prenom);
                preparedStatement.setString(3, birth);
                preparedStatement.setString(4, sexe);
                preparedStatement.setString(5, adresse);
                preparedStatement.setString(6, cin);
                preparedStatement.setString(7, phone);
                preparedStatement.setString(8, employe);
                preparedStatement.setInt(9, tranche_paiement);
                preparedStatement.setString(10, mesure_trano);
                preparedStatement.setString(11, prix_total);
                preparedStatement.setString(12, first_payment);
                preparedStatement.setString(13, resteApaye);
                preparedStatement.setString(14, reste_par_mois);

                ZonedDateTime madagascarDate = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
                String formattedDate = madagascarDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                preparedStatement.setString(15, formattedDate);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0 && !"".equals(nom.trim()) && !"".equals(prenom.trim()) && !"".equals(birth.trim()) && !"".equals(cin.trim()) && !"".equals(phone.trim()) && !"".equals(mesure_trano.trim()) && !"".equals(first_payment.trim()) && !"".equals(adresse.trim())) {
                    btn_valider.setEnabled(true);
                    reinitialiser();
                    JOptionPane.showMessageDialog(null, "Opération réussie.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    TableClient();
                } else {
                    System.out.println("L'insertion a échoué.");
                    JOptionPane.showMessageDialog(null, "Veuillez bien vérifier les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Dates() {
        ZonedDateTime madagascarDate = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
        // Formatter la date selon le format désiré (par exemple : dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd / MM / yyyy");
        String formattedDate = madagascarDate.format(formatter);
        date.setText(formattedDate);
    }

    private void startDateTimer() {
        Timer timer = new Timer(60000, e -> Dates()); // Met à jour toutes les 60000 ms (1 minute)
        timer.start();

        // Démarrer la mise à jour initiale
        Dates();
    }

    private void updateTime() {
        ZonedDateTime madagascarTime = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
        String currentTime = madagascarTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); // Récupère l'heure actuelle
        time.setText(currentTime); // Met à jour le label avec l'heure actuelle
        int currentHour = madagascarTime.getHour();
        if (currentHour >= 5 && currentHour < 12) {
            greeting.setText("Bonjour");  // Matin
        } else if (currentHour >= 12 && currentHour < 18) {
            greeting.setText("Bon après-midi");  // Après-midi
        } else {
            greeting.setText("Bonsoir");  // Soir et nuit
        }
    }


    private void Time() {
        timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Créer un timer qui met à jour l'heure toutes les secondes (1000 ms)
        timer = new Timer(1000, e -> updateTime());
        timer.start();

        // Démarrer la mise à jour initiale
        updateTime();
    }

    public LocalDate getFirstPaymentDate(int clientId) throws SQLException {
        String query = "SELECT Date_1er_paiement FROM client WHERE Id_client = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                java.sql.Date sqlDate = resultSet.getDate("Date_1er_paiement");
                // Convertir java.sql.Date en LocalDate
                LocalDate localDate = sqlDate.toLocalDate();
                // Convertir LocalDate en LocalDateTime à minuit (ou une autre heure si nécessaire)
                LocalDateTime localDateTime = localDate.atStartOfDay();
                // Appliquer le fuseau horaire de Madagascar (Antananarivo)
                ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Indian/Antananarivo"));
                // Retourner uniquement la partie date, si vous voulez la date sans l'heure
                return zonedDateTime.toLocalDate();
            }
            return null;
        }
    }

    public LocalDate calculateNextPaymentDate(LocalDate firstPaymentDate) {
        return firstPaymentDate.plusDays(31); // Ajouter 31 jours
    }

    @SuppressWarnings("empty-statement")

    public void reinitialiser() {
        champ_nom.setText("");
        champ_prenoms.setText("");
        champ_birth.setText("");
        champ_adresse.setText("");
        champ_cin.setText("");
        champ_phone.setText("");
        champ_mesure_trano.setText("");
        champ_prix_total.setText("");
        champ_first_payment.setText("");
        champ_resteApayé.setText("");
        champ_prix_prmois.setText("");

    }

    // Méthode pour récupérer la tranche de paiement, prix par m² et pourcentage depuis la DB
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        navbar = new javax.swing.JPanel();
        time = new javax.swing.JLabel();
        greeting = new javax.swing.JLabel();
        leftside = new javax.swing.JPanel();
        champ_nom = new javax.swing.JTextField();
        champ_prenoms = new javax.swing.JTextField();
        champ_birth = new javax.swing.JTextField();
        champ_adresse = new javax.swing.JTextField();
        champ_cin = new javax.swing.JTextField();
        champ_phone = new javax.swing.JTextField();
        champ_id_employe = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        champ_tranche = new javax.swing.JTextField();
        champ_mesure_trano = new javax.swing.JTextField();
        champ_prix_total = new javax.swing.JTextField();
        champ_prix_prmois = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        champ_first_payment = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        champ_resteApayé = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        champ_sexe = new javax.swing.JComboBox<>();
        reste = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtaffiche = new javax.swing.JTable();
        btn_valider = new javax.swing.JButton();
        btn_reinitialiser = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        champ_critere = new javax.swing.JComboBox<>();
        champ_valeur = new javax.swing.JTextField();
        btn_recherche = new javax.swing.JButton();
        btn_affichetous = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion des clients");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        navbar.setBackground(new java.awt.Color(0, 204, 204));

        time.setFont(new java.awt.Font("Cantarell", 1, 18)); // NOI18N
        time.setForeground(new java.awt.Color(255, 255, 255));

        greeting.setFont(new java.awt.Font("Cantarell", 1, 18)); // NOI18N
        greeting.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout navbarLayout = new javax.swing.GroupLayout(navbar);
        navbar.setLayout(navbarLayout);
        navbarLayout.setHorizontalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navbarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(greeting, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        navbarLayout.setVerticalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(time, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(greeting, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        leftside.setBackground(new java.awt.Color(204, 204, 204));

        champ_id_employe.setEditable(false);
        champ_id_employe.setText(String.valueOf(Userlogin.getUserId()));

        jLabel1.setText("Nom");

        jLabel2.setText("Prénoms");

        jLabel3.setText("Date de naissance (DD/MM/YYYY)");

        jLabel4.setText("Sexe");

        jLabel5.setText("Adresse");

        jLabel6.setText("CIN");

        jLabel7.setText("Téléphone");

        jLabel8.setText("ID employé");

        champ_tranche.setEditable(false);

        champ_mesure_trano.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                champ_mesure_tranoKeyReleased(evt);
            }
        });

        champ_prix_total.setEditable(false);

        champ_prix_prmois.setEditable(false);

        jLabel9.setText("Tranche de paiement");

        jLabel10.setText("Surface à peindre (en m²)");

        jLabel11.setText("Prix total");

        jLabel12.setText("1er Paiement ");

        champ_first_payment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                champ_first_paymentKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                champ_first_paymentKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                champ_first_paymentKeyTyped(evt);
            }
        });

        jLabel13.setText("Reste à payer (mensuellement)");

        champ_resteApayé.setEditable(false);

        jLabel15.setFont(new java.awt.Font("Cantarell", 1, 20)); // NOI18N
        jLabel15.setText("Formulaire Client");

        champ_sexe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Homme", "Femme" }));

        jLabel14.setText("Date :");

        date.setFont(new java.awt.Font("Cantarell", 1, 14)); // NOI18N

        javax.swing.GroupLayout leftsideLayout = new javax.swing.GroupLayout(leftside);
        leftside.setLayout(leftsideLayout);
        leftsideLayout.setHorizontalGroup(
            leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftsideLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(176, 176, 176))
            .addGroup(leftsideLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(leftsideLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(champ_resteApayé)
                    .addComponent(reste, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(champ_prix_total)
                    .addComponent(champ_tranche)
                    .addComponent(champ_phone)
                    .addComponent(champ_adresse)
                    .addComponent(champ_birth)
                    .addComponent(champ_nom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(champ_prenoms, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(champ_sexe, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(champ_cin, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(champ_id_employe, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(champ_prix_prmois, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(champ_first_payment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(champ_mesure_trano, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        leftsideLayout.setVerticalGroup(
            leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftsideLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel15)
                .addGap(29, 29, 29)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_prenoms, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(champ_sexe, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(champ_birth))
                .addGap(9, 9, 9)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_adresse, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_cin, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_id_employe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_tranche, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_mesure_trano, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(2, 2, 2)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_prix_total, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_first_payment, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(reste, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(champ_prix_prmois, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_resteApayé, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(leftsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        txtaffiche.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        txtaffiche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtafficheMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtafficheMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txtaffiche);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(panel);

        btn_valider.setBackground(new java.awt.Color(102, 204, 255));
        btn_valider.setText("Valider");
        btn_valider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_validerActionPerformed(evt);
            }
        });

        btn_reinitialiser.setBackground(new java.awt.Color(255, 102, 102));
        btn_reinitialiser.setText("Réinitialiser");
        btn_reinitialiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reinitialiserActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel16.setBackground(new java.awt.Color(0, 153, 153));
        jLabel16.setText("Rechercher par");

        champ_critere.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Id", "Nom", "Id employé" }));
        champ_critere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champ_critereActionPerformed(evt);
            }
        });

        btn_recherche.setBackground(new java.awt.Color(51, 255, 153));
        btn_recherche.setText("Rechercher");
        btn_recherche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rechercheActionPerformed(evt);
            }
        });

        btn_affichetous.setText("Afficher Tous");
        btn_affichetous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_affichetousActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(champ_critere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(champ_valeur, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_recherche)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_affichetous)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(champ_critere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_valeur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_recherche)
                    .addComponent(btn_affichetous))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(leftside, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(btn_valider, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(btn_reinitialiser)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(leftside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_valider, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reinitialiser, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_validerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_validerActionPerformed
        try {
            insertData();
            // TODO add your handling code here:
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Clientgestion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_validerActionPerformed

    private void champ_mesure_tranoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_champ_mesure_tranoKeyReleased
        // TODO add your handling code here:
        calculer();
    }//GEN-LAST:event_champ_mesure_tranoKeyReleased

    private void champ_first_paymentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_champ_first_paymentKeyReleased
        // TODO add your handling code here:
        calculpersonalize();
        System.out.println("OKey");
    }//GEN-LAST:event_champ_first_paymentKeyReleased

    private void champ_first_paymentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_champ_first_paymentKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_champ_first_paymentKeyTyped

    private void champ_first_paymentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_champ_first_paymentKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_champ_first_paymentKeyPressed

    private void txtafficheMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtafficheMouseClicked

    }//GEN-LAST:event_txtafficheMouseClicked

    private void txtafficheMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtafficheMouseReleased
        try {
            GenerateCustomPDF gc = new GenerateCustomPDF();
            InfoClient ic = new InfoClient();
            ic.setVisible(true);
            int i = txtaffiche.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) txtaffiche.getModel();
            ic.getNom().setText(model.getValueAt(i, 1).toString());
            ic.getPrenoms().setText(model.getValueAt(i, 2).toString());
            ic.getBirthday().setText(model.getValueAt(i, 3).toString());
            ic.getSexe1().setText(model.getValueAt(i, 4).toString());
            ic.getAdresse().setText(model.getValueAt(i, 5).toString());
            ic.getCin().setText(model.getValueAt(i, 6).toString());
            ic.getPhone().setText(model.getValueAt(i, 7).toString());
            ic.getEmploye().setText(model.getValueAt(i, 8).toString());
            ic.getTranche().setText(model.getValueAt(i, 9).toString());
            ic.getMesure().setText(model.getValueAt(i, 10).toString());
            ic.getTotalPrice().setText(model.getValueAt(i, 11).toString());
            ic.getFirst_payment().setText(model.getValueAt(i, 12).toString());
            ic.getResteApaye().setText(model.getValueAt(i, 13).toString());
            ic.getMontant_par_mois().setText(model.getValueAt(i, 14).toString());
            clientId = Integer.parseInt(txtaffiche.getValueAt(i, 0).toString());
            ic.test();
            try {
                LocalDate firstPaymentDate = getFirstPaymentDate(clientId);
                if (firstPaymentDate != null) {
                    LocalDate nextPaymentDate = calculateNextPaymentDate(firstPaymentDate);
                    ic.getDate_prochain_paiement().setText(String.valueOf(nextPaymentDate));
                    System.out.println("Date du prochain paiement : " + nextPaymentDate);
                } else {
                    System.out.println("Aucune date de premier paiement trouvée.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la récupération de la date de paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Clientgestion.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*     } catch (ClassNotFoundException | SQLException ex) {
        Logger.getLogger(Clientgestion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
 /*     } catch (ClassNotFoundException | SQLException ex) {
        Logger.getLogger(Clientgestion.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }//GEN-LAST:event_txtafficheMouseReleased

    private void btn_rechercheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rechercheActionPerformed
        // TODO add your handling code here:
        String critereSelectionne = champ_critere.getSelectedItem().toString();
        String valeurRecherche = champ_valeur.getText();

        effectuerRecherche(critereSelectionne, valeurRecherche);
    }//GEN-LAST:event_btn_rechercheActionPerformed

    private void btn_affichetousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_affichetousActionPerformed
        try {
            // TODO add your handling code here:
            TableClient();
            champ_valeur.setText("");
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Clientgestion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_affichetousActionPerformed

    private void btn_reinitialiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reinitialiserActionPerformed
        // TODO add your handling code here:
        reinitialiser();
    }//GEN-LAST:event_btn_reinitialiserActionPerformed

    private void champ_critereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champ_critereActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champ_critereActionPerformed

    private void effectuerRecherche(String critere, String valeur) {
    String[] colonnes = {"Id Client", "Nom", "Prénoms", "Date de naissance", "Sexe", "Adresse", "Cin", "Téléphone", "Id employé", "Mésure Trano", "Prix Total", "1er paiement en Ariary", "Reste à payé en mois", "Reste à payés par mois"};
    DefaultTableModel model = new DefaultTableModel(null, colonnes);

    String query = "";
    switch (critere) {
        case "Id" ->
            query = "SELECT * FROM client WHERE Id_client = ?";
        case "Nom" -> {
            query = "SELECT * FROM client WHERE Nom LIKE ?";
            valeur = "%" + valeur + "%"; // Ajout des wildcards pour une recherche partielle
        }
        case "Id Employé" ->
            query = "SELECT * FROM client WHERE Id_employe = ?";
    }

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, valeur);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            // Parcours du ResultSet pour récupérer les données de chaque client correspondant
            while (resultSet.next()) {
                String[] afficher = new String[17];
                afficher[0] = resultSet.getString("Id_client");
                afficher[1] = resultSet.getString("Nom");
                afficher[2] = resultSet.getString("Prenoms");
                afficher[3] = resultSet.getString("Date_de_naissance");
                afficher[4] = resultSet.getString("Sexe");
                afficher[5] = resultSet.getString("Adresse");
                afficher[6] = resultSet.getString("Cin");
                afficher[7] = resultSet.getString("Telephone");
                afficher[8] = resultSet.getString("Id_employe");
                afficher[9] = resultSet.getString("Mesure_trano");
                afficher[10] = resultSet.getString("Prix_total");
                afficher[11] = resultSet.getString("First_payment");
                afficher[12] = resultSet.getString("Reste_apaye");
                afficher[13] = resultSet.getString("Prix_par_mois_reste");

                // Ajout des résultats au modèle de la table
                model.addRow(afficher);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erreur lors de la recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Mise à jour du modèle de la JTable avec les résultats de la recherche
    txtaffiche.setModel(model);
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(Clientgestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(Clientgestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(Clientgestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(Clientgestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            try {
                new Clientgestion().setVisible(true);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Clientgestion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_affichetous;
    private javax.swing.JButton btn_recherche;
    private javax.swing.JButton btn_reinitialiser;
    private javax.swing.JButton btn_valider;
    private javax.swing.JTextField champ_adresse;
    private javax.swing.JTextField champ_birth;
    private javax.swing.JTextField champ_cin;
    private javax.swing.JComboBox<String> champ_critere;
    private javax.swing.JTextField champ_first_payment;
    private javax.swing.JTextField champ_id_employe;
    private javax.swing.JTextField champ_mesure_trano;
    private javax.swing.JTextField champ_nom;
    private javax.swing.JTextField champ_phone;
    private javax.swing.JTextField champ_prenoms;
    private javax.swing.JTextField champ_prix_prmois;
    private javax.swing.JTextField champ_prix_total;
    private javax.swing.JTextField champ_resteApayé;
    private javax.swing.JComboBox<String> champ_sexe;
    private javax.swing.JTextField champ_tranche;
    private javax.swing.JTextField champ_valeur;
    private javax.swing.JLabel date;
    private javax.swing.JLabel greeting;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftside;
    private javax.swing.JPanel navbar;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel reste;
    private javax.swing.JLabel time;
    private javax.swing.JTable txtaffiche;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the champ_id_employe
     */

}
