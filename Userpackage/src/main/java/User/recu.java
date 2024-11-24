/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author zikrea
 */
public class recu {


    public void generatePDF() {
        Document document = new Document();

        try {
            // Initialiser un PdfWriter pour écrire le fichier
            PdfWriter.getInstance(document, new FileOutputStream("reçu_premier_paiement.pdf"));

            // Ouverture du document
            document.open();

            // Ajouter le titre
            Paragraph titre = new Paragraph("REÇU DE PREMIER PAIEMENT");
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            // Ajouter un espace
            document.add(new Paragraph("\n"));

            // Informations du client
            document.add(new Paragraph("Nom du client : HERINIANTSONIAVO Jonas"));
            document.add(new Paragraph("Date de naissance : 25/02/2002"));
            document.add(new Paragraph("Numéro de carte d'identité : 201011031062"));
            document.add(new Paragraph("Téléphone : 0334001828"));
            document.add(new Paragraph("Adresse : Sotema"));
            document.add(new Paragraph("\n"));

            // Détails du paiement
            document.add(new Paragraph("Montant total du contrat : 500000 Ariary"));
            document.add(new Paragraph("Montant du premier paiement (40%) : 200000 Ariary"));
            document.add(new Paragraph("Date du paiement : 23/10/2014"));
            document.add(new Paragraph("\n"));

            // Confirmation du paiement
            document.add(new Paragraph("Nous confirmons que le premier paiement de 200000 Ariary a été reçu avec succès."));
            document.add(new Paragraph("Ce paiement correspond à 40% du montant total du contrat de peinture."));
            document.add(new Paragraph("\n"));

            // Ajouter les signatures
            document.add(new Paragraph("Le propriétaire                                                                                          Le responsable"));
            document.add(new Paragraph("\n\n\n\n"));
            document.add(new Paragraph("                                                                                                                GABRIEL Jonah"));

            // Fermeture du document
            document.close();

            System.out.println("Reçu de paiement généré avec succès!");
            JOptionPane.showMessageDialog(null, "Reçu de paiement généré avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}

    

