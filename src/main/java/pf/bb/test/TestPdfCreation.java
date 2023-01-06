package pf.bb.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import pf.bb.Main;
import pf.bb.model.Configuration;

import java.awt.Desktop;
import java.io.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestPdfCreation {

    public static List<Configuration> CONFIGURATIONS = new ArrayList<>();

    public TestPdfCreation() { }


    public static void main(String[] args) {

        try{
            String url = Main.API_HOST + "/configurations";
            // Synchronous request otherwise use .asAsyncJson()
            HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").asJson();
            String json = resJson.getBody().toString();
            CONFIGURATIONS.addAll(new Gson().fromJson(json, new TypeToken<ArrayList<Configuration>>() {}.getType()));

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        TestPdfCreation tpc = new TestPdfCreation();
        if (CONFIGURATIONS.size() > 0) {
            tpc.createAndOpenTempPdfBill(CONFIGURATIONS.get(0));
        }
    }

    public void addHeaderCells(Table table, String[] headers) {
        for (String header : headers) {
            Cell cell = new Cell();
            Paragraph cellText = new Paragraph(header);
            cell.add(cellText);
            table.addHeaderCell(cell);
        }
    }

    public void addCell(Table table, String value, TextAlignment hzA) {
        Cell cell = new Cell();
        cell.setTextAlignment(hzA);
        Paragraph cellText = new Paragraph(value);
        cell.add(cellText);
        table.addCell(cell);
    }

    public void addCell(Table table, String value) {
        this.addCell(table, value, TextAlignment.LEFT);
    }



    public void createAndOpenTempPdfBill (Configuration config){
        try {

            // Creating a PdfWriter
            String filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bicycleBill.pdf";
            PdfWriter writer = new PdfWriter(filepath);

            // Creating a PdfDocument
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Creating a Document
            Document doc = new Document(pdfDoc);

            //adds elements to file
            // Creating a table object with
            int columns = 6;
            Table table = new Table(columns).useAllAvailableWidth().setAutoLayout();

            this.addHeaderCells(table, new String[]{"Pos.", "ANr.", "Typ", "Charakteristik", "Artikelname", "Preis in €"});

            for (int i=0; i<config.articles.size(); i++) {

                this.addCell(table, String.valueOf(i+1));
                this.addCell(table, String.valueOf(config.articles.get(i).id));
                this.addCell(table, config.articles.get(i).type);
                this.addCell(table, config.articles.get(i).characteristic);
                this.addCell(table, config.articles.get(i).name);
                this.addCell(table, String.format(Locale.getDefault(),"%.2f", config.articles.get(i).price), TextAlignment.RIGHT);
            }

            //Total Price Row
            for (int i=0; i<columns-2; i++) {
                this.addCell(table, "");
            }
            this.addCell(table, "Gesamtpreis");
            this.addCell(table, String.format(Locale.getDefault(),"%.2f", config.order.priceTotal), TextAlignment.RIGHT);

            // Adding table to the document
            doc.add(table);

            //close the PDF file
            doc.close();

            try
            {
                //constructor of file class having file as argument
                File file = new File(filepath);
                if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
                {
                    System.out.println("opening of File is not supported on this os");
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                if(file.exists())         //checks file exists or not
                    desktop.open(file);              //opens the specified file
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
