/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author zikrea
 */
public class GenerateCustomPDF {
    public void generatePDF() throws ClassNotFoundException, SQLException {
    Document document = new Document();

        try {
            // Initialiser un PdfWriter pour écrire le fichier
            PdfWriter.getInstance(document, new FileOutputStream("contrat_peinture_maison.pdf"));

            // Ouverture du document
            document.open();

            // Ajouter le titre
            Paragraph titre = new Paragraph("Three L");
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            // Ajouter les informations de l'entreprise
            document.add(new Paragraph("- Représenté par le Directeur Général : GABRIEL Jonah"));
            document.add(new Paragraph("- Domicilié à Ambovombe Androy"));
            document.add(new Paragraph("- N° de Registre de Commerce : RCS /AMBOV/2023/A/00038"));
            document.add(new Paragraph("- N° statistique : 10402 52 2022 0 00060"));
            document.add(new Paragraph("- Identification fiscale : 4002743746"));
            document.add(new Paragraph("- Téléphone : +261 34 32 351 90 ; +261 33 29 238 09"));
            document.add(new Paragraph("- E-mail : paikablessing@gmail.com"));
            document.add(new Paragraph("- Titulaire du compte : GABRIEL Jonah"));
            document.add(new Paragraph("- Établissement bancaire : BNI"));
            document.add(new Paragraph("- Agence : TULEAR"));
            document.add(new Paragraph("- Numéro de compte : 00041 70823680003"));

            // Ajouter un espace
            document.add(new Paragraph("\n"));

            // Ajouter le titre du contrat
            Paragraph sousTitre = new Paragraph("CONTRAT DE PEINTURE DE MAISON");
            sousTitre.setAlignment(Element.ALIGN_CENTER);
            document.add(sousTitre);
            



            // Informations du client
            document.add(new Paragraph("Nom : HERINIANTSONIAVO" ));
            document.add(new Paragraph("Prénoms : Jonas " ));
            document.add(new Paragraph("Date et de naissance : 25/02/2002" ));
            document.add(new Paragraph("Numéro de carte d'identité nationale : 201011031062 " ));
            document.add(new Paragraph("Numéro téléphone : 0334001828 " ));
            document.add(new Paragraph("Adresse : Sotema " ));
            document.add(new Paragraph("\n"));

            // Ajouter le texte du contrat
            document.add(new Paragraph("Le client accepte que l'entreprise Three L peigne son église, maison, école ou hôtel situé à Sotema " 
                    + "d'un surface totale de 100 " + " m², pour un montant total de 500000" + " Ariary."));
            
            document.add(new Paragraph("Mode de paiement"));
            document.add(new Paragraph("Les 40% du montant doivent être payés par le propriétaire en espèces immédiatement avant le début des travaux, ce qui totalise 200000"+" Ariary."));
            document.add(new Paragraph("Les 60% restants doivent être payés par le propriétaire 20% par mois à la fin de chaque mois pendant trois (3) mois, soit en espèces, soit par Mvola, ou sur les comptes BNI."));
            document.add(new Paragraph("Les 20% totalisent 100000" +" Ariary"));
            document.add(new Paragraph("Ce contrat est établi pour être valable à tout moment où cela sera nécessaire."));
            Clientgestion cg = new Clientgestion();
            document.add(new Paragraph("Fait le 23/10/2014" ));
            document.add(new Paragraph("\n"));

            // Ajouter les signatures
            document.add(new Paragraph("Le propriétaire                                                                                          Le responsable"));
            document.add(new Paragraph("\n\n\n\n"));
            document.add(new Paragraph("                                                                                                                GABRIEL Jonah"));

            // Fermeture du document
            document.close();

            System.out.println("PDF généré avec succès!");
            JOptionPane.showMessageDialog(null, "Contrat généré avec succès !", "succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    }
    
