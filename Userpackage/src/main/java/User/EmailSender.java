package User;


import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public void sendVerificationEmail(int employeeId, String mail, String passwords) {
        // Remplacez ces valeurs par votre e-mail et le mot de passe d'application généré par Gmail
        String fromEmail = "heryniantso@gmail.com";
        String password = "wpzg quiz stws rktq";
        String verificationLink = "http://127.0.0.1/index.php?email=" + mail + "&pass=" + passwords;
        String messages = "Bonjour, \n\n"
                + "Un nouvel employé  " + mail + " vient d'envoyer une demande d'ajout. \n"
                + "Veuillez cliquer sur le lien pour la validation de son enregistrement \n"
                + verificationLink;

        // Configuration des propriétés pour le serveur SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");  // Serveur SMTP de Gmail
        properties.put("mail.smtp.port", "587");  // Port pour TLS
        properties.put("mail.smtp.auth", "true");  // Authentification requise
        properties.put("mail.smtp.starttls.enable", "true");  // Activer TLS

        // Création d'une session avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        session.setDebug(true);

        try {
            // Création d'un message e-mail
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));  // Adresse de l'expéditeur
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("heryniavo@yahoo.com"));  // Destinataire
            message.setSubject("Vérification d'inscription d'employé");  // Sujet de l'e-mail
            message.setText(messages);  // Corps de l'e-mail

            // Envoi de l'e-mail
            Transport.send(message);

            System.out.println("E-mail de vérification envoyé avec succès.");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
