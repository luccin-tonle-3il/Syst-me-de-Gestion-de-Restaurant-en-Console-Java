package service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import dao.DBConnection;

import java.io.FileOutputStream;
import java.sql.*;

public class FacturePdfGenerator {

    public static void genererFacture(int commandeId) throws Exception {
        Document document = new Document();
        String nomFichier = "facture_commande_" + commandeId + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(nomFichier));

        document.open();

        // Titre
        Font titreFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph titre = new Paragraph("🧾 Facture - Restaurant Les Triplés", titreFont);
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);
        document.add(new Paragraph("\nCommande n°: " + commandeId));
        document.add(new Paragraph("Date: " + new java.util.Date()));
        document.add(new Paragraph("\n"));

        // Table des items
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Type");
        table.addCell("Nom");
        table.addCell("Quantité");
        table.addCell("Prix");

        Connection conn = DBConnection.getConnection();
        String sql = """
                SELECT 'Plat' AS type, p.nom, ci.quantite, p.prix
                FROM CommandeItem ci
                JOIN Plat p ON ci.plat_id = p.id
                WHERE ci.commande_id = ?
                UNION
                SELECT 'Boisson' AS type, b.nom, ci.quantite, b.prix
                FROM CommandeItem ci
                JOIN Boisson b ON ci.boisson_id = b.id
                WHERE ci.commande_id = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, commandeId);
        stmt.setInt(2, commandeId);

        ResultSet rs = stmt.executeQuery();
        double total = 0;

        while (rs.next()) {
            table.addCell(rs.getString("type"));
            table.addCell(rs.getString("nom"));
            int qte = rs.getInt("quantite");
            double prix = rs.getDouble("prix");
            table.addCell(String.valueOf(qte));
            table.addCell(String.format("%.2f €", prix * qte));
            total += prix * qte;
        }

        document.add(table);
        document.add(new Paragraph("\nTotal à payer : " + String.format("%.2f €", total)));

        document.close();
        System.out.println("📄 Facture générée : " + nomFichier);
    }
}