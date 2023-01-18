package pf.bb.model;

import com.google.gson.annotations.Expose;
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
import pf.bb.Main;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Klasse für das Konfigurationsobjekt.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class Configuration extends EntityWithID {

    public static String[] stats = {"ENTWURF", "ABGESCHLOSSEN", "EINKAUF", "STORNO"};

    public static String getUrl() {
        return Main.API_HOST + "/configurations";
    }

    //@Expose fields are serialized to JSON via Gson -> fields must be public for Gson
    //Getter necessary for TableView<Configuration> (dboard_table) column method .setCellValueFactory
    @Expose
    public String writeAccess;

    @Expose
    public String timestampCreated;

    public String getTimestampLastTouched() {
        return timestampLastTouched;
    }

    @Expose
    public String timestampLastTouched;

    @Expose
    public User user;

    public String getStatus() {
        return status;
    }

    @Expose
    public String status;

    @Expose
    public ArrayList<Article> articles;

    public void setOrder(OrderClass order) {
        order.configuration = null; // must be null otherwise GSON serialize in loop
        this.order = order;
    }

    @Expose
    public OrderClass order;

    // Transient Properties are not serialized - necessary for FXML Tableview
    private transient String customerName;
    public String getCustomerName() {
        return this.order == null ? "" : this.order.customer.lastname + ", " + this.order.customer.forename;
    }

    private transient String customerId;
    public String getCustomerId() {
        return this.order == null ? "" : String.valueOf(this.order.customer.id);
    }


    public Configuration(User user) {

        this.writeAccess = null;
        this.user = user;
        this.status = stats[0];
        this.articles = new ArrayList<>();
    }

    public String toString() {
        return String.format(this.getClass().getName() + "[id=%d, dateCreated='%s', lastTouched='%s']", id, timestampCreated, timestampLastTouched);
    }


    public Article getArticleByType(String typeName) {
        Article article = null;
        for (Article a: this.articles ){
            if (Objects.equals(a.type, typeName)) {
                article = a;
                break;
            }
        }
        return article;
    };


    public void createAndOpenTempPdf(String docType){

        if(!(this.order == null)) {
            if(!(this.order.bill == null)) {
                try {
                    String filepath;

                    if(docType.equals("ORDER")) {
                        filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bicycleOrder.pdf";
                    } else {
                        // create and open pdf file
                        filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bicycleBill.pdf";
                    }

                    PdfWriter writer = new PdfWriter(filepath);
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    Document doc = new Document(pdfDoc);
                    Table table1 = null;

                    //adds elements
                    // TABLE1 bill
                    // Creating a table object with
                    if(docType.equals("ORDER")) {
                        String[] headers1 = new String[]{"AUFTRAG", ""};
                        table1 = this.createTableWithHeaderNames(headers1);
                        table1.setFixedPosition(360, 652, 200);
                        this.addCell(table1, "");
                        this.addCell(table1, "");
                    } else {
                        String[] headers1 = new String[]{"RECHNUNG", ""};
                        table1 = this.createTableWithHeaderNames(headers1);
                        table1.setFixedPosition(360, 652, 200);
                        this.addCell(table1, "Rechnungs-Nr.:");
                        this.addCell(table1, String.valueOf(this.order.bill.id));
                    }
                    this.addCell(table1, "Datum:");
                    this.addCell(table1, this.formatDateStringGermany(this.order.bill.timestampCreated.substring(0,10)));
                    this.addCell(table1, "Auftrags-Nr.:");
                    this.addCell(table1, String.valueOf(this.order.id));
                    this.addCell(table1, "Auftrags-Datum.:");
                    this.addCell(table1, this.formatDateStringGermany(this.order.timestampCreated.substring(0,10)));
                    this.addCell(table1, "Kunden-Nr.:");
                    this.addCell(table1, String.valueOf(this.order.customer.id));
                    this.addCell(table1, "Bearbeiter:");
                    this.addCell(table1, this.user.lastname);
                    this.removeBordersFromTable(table1);

                    // TABLE2 customer
                    String[] headers2 = new String[]{"BicycleBuilder AG, Radweg 8, 10101 Internet"};
                    Table table2 = this.createTableWithHeaderNames(headers2);
                    ImageData data = ImageDataFactory.create("src/main/resources/img/logo.PNG");
                    Image img = new Image(data);
                    img.scale((float) 0.3, (float) 0.3);
                    this.addCell(table2, "").add(img);             // add logo
                    this.addCell(table2, "").setHeight(15);
                    this.addCell(table2, this.order.customer.forename + " " + this.order.customer.lastname);
                    this.addCell(table2, this.order.customer.street + " " + this.order.customer.houseNumber);
                    this.addCell(table2, this.order.customer.zipCode + " " + this.order.customer.city);
                    this.addCell(table2, "").setHeight(100);
                    this.removeBordersFromTable(table2);

                    // TABLE3 - articles
                    String[] headers3 = new String[]{"Pos.", "ANr.", "Typ", "Charakteristik", "Artikelname", "Preis in €"};
                    Table table3 = this.createTableWithHeaderNames(headers3);
                    table3.useAllAvailableWidth();
                    for (int i=0; i<this.articles.size(); i++) {
                        this.addCell(table3, String.valueOf(i+1));
                        this.addCell(table3, String.valueOf(this.articles.get(i).id));
                        this.addCell(table3, this.articles.get(i).type);
                        this.addCell(table3, this.articles.get(i).characteristic);
                        this.addCell(table3, this.articles.get(i).name);
                        this.addCell(table3, String.format(Locale.getDefault(),"%.2f", this.articles.get(i).price), TextAlignment.RIGHT);
                    }
                    //Total Price Row
                    for (int i=0; i< headers3.length-2; i++) {
                        this.addCell(table3, "");
                    }
                    this.addCell(table3, "Gesamtpreis").setBold();
                    this.addCell(table3, String.format(Locale.getDefault(),"%.2f", this.order.priceTotal), TextAlignment.RIGHT).setBold();

                    // Adding tables to document
                    doc.add(table1);
                    doc.add(table2);
                    doc.add(table3);

                    //close the PDF file
                    doc.close();

                    // AR: Linux debug
                    new Thread(() -> {
                        try {
                            File file = new File(filepath);
                            if(!Desktop.isDesktopSupported()) //check if Desktop is supported by Platform or not
                            {
                                System.out.println("opening of File is not supported on this os");
                            } else {
                                if (file.exists()) {
                                    Desktop.getDesktop().open(file);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    /*
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
                     */

                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (MalformedURLException | ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Es wurde noch keine Rechnung erstellt.");
            }
        } else {
            System.out.println("Es wurde noch kein Auftrag erstellt.");
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


}
