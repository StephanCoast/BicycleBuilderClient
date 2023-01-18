package pf.bb.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
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
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Test-Klasse Bicycle Builder zur PDF-Erstellung.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
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

    public Table createTableWithHeaderNames(String[] headers) {
        int columns = headers.length;
        Table table = new Table(columns).setAutoLayout();

        for (String header : headers) {
            Cell cell = new Cell();
            cell.setBold();
            Paragraph cellText = new Paragraph(header);
            cell.add(cellText);
            table.addHeaderCell(cell);
        }
        return table;
    }

    public Cell addCell(Table table, String value, TextAlignment hzA) {
        Cell cell = new Cell();
        cell.setTextAlignment(hzA);
        if (!Objects.equals(value, "")) {
            Paragraph cellText = new Paragraph(value);
            cell.add(cellText);
        }
        table.addCell(cell);
        return cell;
    }

    public Cell addCell(Table table, String value) {
        return this.addCell(table, value, TextAlignment.LEFT);
    }

    public void removeBordersFromTable(Table table) {
        //HEADER
        Table header = table.getHeader();
        for (int h=0; h<header.getNumberOfColumns(); h++)
            header.getCell(0,h).setBorder(Border.NO_BORDER);
        //BODY
        for (int r=0; r<table.getNumberOfRows(); r++) {
            for (int c=0; c<table.getNumberOfColumns(); c++) {
                table.getCell(r,c).setBorder(Border.NO_BORDER);
            }
        }
    }

    public String formatDateStringGermany(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        dateString = df.format(date);
        return dateString;
    }


    public void createAndOpenTempPdfBill (Configuration config){
        try {
            // create and open pdf file
            String filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bicycleBill.pdf";
            PdfWriter writer = new PdfWriter(filepath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            //adds elements
            // TABLE1 bill
            // Creating a table object with
            String[] headers1 = new String[]{"RECHNUNG", ""};
            Table table1 = this.createTableWithHeaderNames(headers1);
            table1.setFixedPosition(360, 652, 200);
            this.addCell(table1, "Rechnungs-Nr.:");
            this.addCell(table1, String.valueOf(config.order.bill.id));
            this.addCell(table1, "Datum:");
            this.addCell(table1, this.formatDateStringGermany(config.order.bill.timestampCreated.substring(0,10)));
            this.addCell(table1, "Auftrags-Nr.:");
            this.addCell(table1, String.valueOf(config.order.id));
            this.addCell(table1, "Auftrags-Datum.:");
            this.addCell(table1, this.formatDateStringGermany(config.order.timestampCreated.substring(0,10)));
            this.addCell(table1, "Kunden-Nr.:");
            this.addCell(table1, String.valueOf(config.order.customer.id));
            this.addCell(table1, "Bearbeiter:");
            this.addCell(table1, config.user.lastname);
            this.removeBordersFromTable(table1);

            // TABLE2 customer
            String[] headers2 = new String[]{"BicycleBuilder AG, Radweg 8, 10101 Internet"};
            Table table2 = this.createTableWithHeaderNames(headers2);
            ImageData data = ImageDataFactory.create("src/main/resources/img/logo.PNG");
            Image img = new Image(data);
            img.scale((float) 0.3, (float) 0.3);
            this.addCell(table2, "").add(img);             // add logo
            this.addCell(table2, "").setHeight(15);
            this.addCell(table2, config.order.customer.forename + " " + config.order.customer.lastname);
            this.addCell(table2, config.order.customer.street + " " + config.order.customer.houseNumber);
            this.addCell(table2, config.order.customer.zipCode + " " + config.order.customer.city);
            this.addCell(table2, "").setHeight(100);
            this.removeBordersFromTable(table2);

            // TABLE3 - articles
            String[] headers3 = new String[]{"Pos.", "ANr.", "Typ", "Charakteristik", "Artikelname", "Preis in €"};
            Table table3 = this.createTableWithHeaderNames(headers3);
            table3.useAllAvailableWidth();
            for (int i=0; i<config.articles.size(); i++) {
                this.addCell(table3, String.valueOf(i+1));
                this.addCell(table3, String.valueOf(config.articles.get(i).id));
                this.addCell(table3, config.articles.get(i).type);
                this.addCell(table3, config.articles.get(i).characteristic);
                this.addCell(table3, config.articles.get(i).name);
                this.addCell(table3, String.format(Locale.getDefault(),"%.2f", config.articles.get(i).price), TextAlignment.RIGHT);
            }
            //Total Price Row
            for (int i=0; i< headers3.length-2; i++) {
                this.addCell(table3, "");
            }
            this.addCell(table3, "Gesamtpreis").setBold();
            this.addCell(table3, String.format(Locale.getDefault(),"%.2f", config.order.priceTotal), TextAlignment.RIGHT).setBold();

            // Adding tables to document
            doc.add(table1);
            doc.add(table2);
            doc.add(table3);

            //close the PDF file
            doc.close();

            try  // to open the temp file in default Desktop App
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
        } catch (MalformedURLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
