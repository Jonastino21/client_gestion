/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;

/**
 *
 * @author zikrea
 */
public class UserSignUp extends javax.swing.JFrame {

    /**
     * Creates new form UserSignUp
     */
    public UserSignUp() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private final Connection connection = Userlogin.Connect();

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }

    private void insertData() throws SQLException {
        EmailSender es = new EmailSender();
        String checkSql = "SELECT * FROM employe WHERE Adresse_mail = ? AND Log = 0";
        String sql = "INSERT INTO employe (Nom, Prenoms, Date_de_naissance, Sexe, Cin, Telephone, Adresse_mail, Mot_de_passe,Log) VALUES (?,?,?,?,?,?,?,?,0)";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            String mails = champ_mail.getText();
            checkStatement.setString(1, mails);
            ResultSet rsCheck = checkStatement.executeQuery();

            if (rsCheck.next()) {
                // Si un compte non confirmé avec cet email existe, afficher un message
                JOptionPane.showMessageDialog(null, "Votre inscription est déjà en cours de traitement. Veuillez attendre sa validation.", "Information", JOptionPane.INFORMATION_MESSAGE);
                close();
                Userlogin ul = new Userlogin();
                ul.setVisible(true);
            } else {
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    String nom = champs_nom.getText();
                    String prenom = champ_prenoms.getText();
                    String birth = champ_birth.getText();
                    String sexe = (String) cham_sexe.getSelectedItem();
                    String cin = champ_cin.getText();
                    String phone = champ_phone.getText();
                    String mail = champ_mail.getText();
                    char[] mdpArray = champ_pass.getPassword();

                    String passw = new String(mdpArray);

                    preparedStatement.setString(1, nom);
                    preparedStatement.setString(2, prenom);
                    preparedStatement.setString(3, birth);
                    preparedStatement.setString(4, sexe);
                    preparedStatement.setString(5, cin);
                    preparedStatement.setString(6, phone);
                    preparedStatement.setString(7, mail);
                    preparedStatement.setString(8, hashPassword(passw));

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows > 0) {
                        // Récupérer l'ID généré automatiquement
                        try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                            if (rs.next()) {
                                int employeeId = rs.getInt(1);
                                System.out.println("ID employé récupéré : " + rs.getInt(1));
                                System.out.println("Envoi de l'e-mail...");
                                System.out.println("Envoi de l'e-mail...");
                                es.sendVerificationEmail(employeeId, mail, hashPassword(passw));  // Envoi de l'email au PDG
                                JOptionPane.showMessageDialog(null, "Votre demande a été envoyée au PDG. Veuillez attendre sa validation.", "Information", JOptionPane.INFORMATION_MESSAGE);
                                close();
                                Userlogin ul = new Userlogin();
                                ul.setVisible(true);
                                System.out.println("Data insérée");
                            }
                        }
                    } else {
                        System.out.println("L'insertion a échoué.");
                        JOptionPane.showMessageDialog(null, "Veuillez bien vérifier les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        } catch (SQLException e) {
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        nomLabel = new javax.swing.JLabel();
        champs_nom = new javax.swing.JTextField();
        prenomsLabel = new javax.swing.JLabel();
        champ_prenoms = new javax.swing.JTextField();
        birthLabel = new javax.swing.JLabel();
        champ_birth = new javax.swing.JTextField();
        sexeLabel = new javax.swing.JLabel();
        cham_sexe = new javax.swing.JComboBox<>();
        cinLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        champ_cin = new javax.swing.JTextField();
        champ_phone = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        champ_mail = new javax.swing.JTextField();
        champ_pass = new javax.swing.JPasswordField();
        jButton10 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(" Inscription Employé");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        nomLabel.setText("Nom");

        champs_nom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champs_nomActionPerformed(evt);
            }
        });

        prenomsLabel.setText("Prénoms");

        champ_prenoms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champ_prenomsActionPerformed(evt);
            }
        });

        birthLabel.setText("Date de naissance (DD/MM/YYYY)");

        sexeLabel.setText("Sexe");

        cham_sexe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Homme", "Femme" }));

        cinLabel.setText("CIN");

        phoneLabel.setText("Téléphone");

        champ_cin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champ_cinActionPerformed(evt);
            }
        });

        champ_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champ_phoneActionPerformed(evt);
            }
        });

        jLabel25.setText("Adresse email");

        jLabel26.setText("Mot de passe");

        champ_mail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champ_mailActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(102, 204, 255));
        jButton10.setText("S'inscrire");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Cantarell", 1, 36)); // NOI18N
        jLabel1.setText("Three L");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(birthLabel)
                            .addComponent(champ_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cinLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(champ_cin, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(champ_mail, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(119, 119, 119)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cham_sexe, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(champ_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(champ_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 211, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(champs_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nomLabel))
                        .addGap(119, 119, 119)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(prenomsLabel)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(champ_prenoms, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(427, 427, 427)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prenomsLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nomLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champs_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_prenoms, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(birthLabel)
                    .addComponent(sexeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cham_sexe, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cinLabel)
                    .addComponent(phoneLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(champ_cin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(champ_mail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(champ_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(600, 600, 600))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void champs_nomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champs_nomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champs_nomActionPerformed

    private void champ_prenomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champ_prenomsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champ_prenomsActionPerformed

    private void champ_cinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champ_cinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champ_cinActionPerformed

    private void champ_phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champ_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champ_phoneActionPerformed

    private void champ_mailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champ_mailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_champ_mailActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            // TODO add your handling code here:
            insertData();
        } catch (SQLException ex) {
            Logger.getLogger(UserSignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

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
            java.util.logging.Logger.getLogger(UserSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserSignUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel birthLabel;
    private javax.swing.JComboBox<String> cham_sexe;
    private javax.swing.JTextField champ_birth;
    private javax.swing.JTextField champ_cin;
    private javax.swing.JTextField champ_mail;
    private javax.swing.JPasswordField champ_pass;
    private javax.swing.JTextField champ_phone;
    private javax.swing.JTextField champ_prenoms;
    private javax.swing.JTextField champs_nom;
    private javax.swing.JLabel cinLabel;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nomLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel prenomsLabel;
    private javax.swing.JLabel sexeLabel;
    // End of variables declaration//GEN-END:variables
}
