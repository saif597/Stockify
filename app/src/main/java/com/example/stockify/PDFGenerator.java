package com.example.stockify;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

public class PDFGenerator {
    private static final int CREATE_FILE_REQUEST_CODE = 42;

//    public static void generatePDF(Context context, List<HashMap<String, Object>> salesDataList) {
//        // Create the PDF document
//        Document document = new Document();
//        try {
//            // Get the URI to the directory where the PDF file will be saved
//            Uri pdfUri = getPdfUri(context);
//
//            if (pdfUri != null) {
//                // Open the document for writing
//                PdfWriter.getInstance(document, context.getContentResolver().openOutputStream(pdfUri));
//                document.open();
//
//                Font font = FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL);
//
//                for (HashMap<String, Object> sale : salesDataList) {
//                    // Retrieve the necessary data from the sale HashMap
//                    String productName = String.valueOf(sale.get("productName"));
//                    String date = String.valueOf(sale.get("date"));
//                    String quantity = String.valueOf(sale.get("quantity"));
//                    String profit = String.valueOf(sale.get("profit"));
//                    String revenue = String.valueOf(sale.get("revenue"));
//
//                    // Build a string representation of the sale data
//                    String saleData = "Product: " + productName + "\n"
//                            + "Date: " + date + "\n"
//                            + "Quantity: " + quantity + "\n"
//                            + "Profit: " + profit + "\n"
//                            + "Revenue: " + revenue + "\n";
//
//                    // Add the sale data to the document
//                    Paragraph paragraph = new Paragraph(saleData, font);
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    document.add(paragraph);
//                }
//
//                document.close();
//                Toast.makeText(context, "PDF generated and saved successfully.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "Failed to create PDF file.", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public static void generatePDF(Context context, List<HashMap<String, Object>> salesDataList) {
    // Create the PDF document
    Document document = new Document();
    try {
        // Get the URI to the directory where the PDF file will be saved
        Uri pdfUri = getPdfUri(context);

        if (pdfUri != null) {
            // Open the document for writing
            PdfWriter.getInstance(document, context.getContentResolver().openOutputStream(pdfUri));
            document.open();

            Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font cellFont = FontFactory.getFont(FontFactory.COURIER, 12);

            // Add the heading
            Paragraph heading = new Paragraph("Stockify", headingFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            // Add the sales report heading
            Paragraph salesReportHeading = new Paragraph("Sales Report", headingFont);
            salesReportHeading.setAlignment(Element.ALIGN_CENTER);
            document.add(salesReportHeading);

            // Add a line separator
            document.add(new Paragraph("-----------------------------------------------------------"));

            // Create a table for the sales data
            PdfPTable table = new PdfPTable(7); // Number of columns
            table.setWidthPercentage(100);

            // Add table headers
            table.addCell(createCell("Product Name", tableHeaderFont));
            table.addCell(createCell("Product ID", tableHeaderFont));
            table.addCell(createCell("Sale ID", tableHeaderFont));
            table.addCell(createCell("Quantity", tableHeaderFont));
            table.addCell(createCell("Total Price", tableHeaderFont));
            table.addCell(createCell("Date", tableHeaderFont));
            table.addCell(createCell("Profit", tableHeaderFont));

            // Add table rows with sales data
            for (HashMap<String, Object> sale : salesDataList) {
                table.addCell(createCell(String.valueOf(sale.get("productName")), cellFont));
                table.addCell(createCell(String.valueOf(sale.get("productId")), cellFont));
                table.addCell(createCell(String.valueOf(sale.get("saleId")), cellFont));
                table.addCell(createCell(String.valueOf(sale.get("quantity")), cellFont));
                table.addCell(createCell(String.valueOf(sale.get("totalPrice")), cellFont));
                table.addCell(createCell(String.valueOf(sale.get("date")), cellFont));
                table.addCell(createCell(String.valueOf(sale.get("profit")), cellFont));
            }

            // Add the table to the document
            document.add(table);

            document.close();
            Toast.makeText(context, "PDF generated and saved successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to create PDF file.", Toast.LENGTH_SHORT).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private static PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private static Uri getPdfUri(Context context) {
        String fileName = "listview.pdf";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri downloadsUri = Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            Uri pdfUri = Uri.withAppendedPath(downloadsUri, fileName);
            return pdfUri;
        } else {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File pdfFile = new File(downloadsDir, fileName);
            return Uri.fromFile(pdfFile);
        }
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data, List<HashMap<String, Object>> salesDataList) {

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                generatePDF(context, salesDataList);
                Toast.makeText(context, "PDF generated and saved successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to create PDF file.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}



