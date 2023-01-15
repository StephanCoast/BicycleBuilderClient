package pf.bb.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SVGManager {

    private static final SVGManager instance = new SVGManager();
    public static final Group svgGroup = new Group();
    public static ObservableList<String> svgStringList;

    // STATIC SET COLORS
    public static final String COLOR_BLACK = "#000000";
    public static final String COLOR_WHITE = "#ffffff";
    public static final String COLOR_SILVER = "#808080";
    public static final String COLOR_BROWN = "#4b2c20"; /* material palette - brown 500 primary dark */

    // SET 1
    public static final String FRAME1 = "M609.5,363.5l-77.1-134.6l-2.6-9.6H354.1l-0.8,7.9c0,0-68.4,127.3-73.8,138c-5.4,10.5,7.8,13.3,7.8,13.3l144.2,1.4l17.5-6.8l70.9-127.6c0,0,63.9,108,74.7,127.3C612.4,385.6,609.5,363.5,609.5,363.5z M301.8,360.3l63.3-119.9l60.5,121.3L301.8,360.3z M440.1,352.2l-60.7-115.8h124.9L440.1,352.2z";            //"M312.2,170.8l-54.5-96.4L256,68H131l-1,5.9c0,0-47.8,89.8-51.8,97.3c-3.9,7.3,5.6,9.2,5.6,9.2h101.9l12.6-4.7l51.2-88.4c0,0,44.1,76.6,51.8,89.9C314.3,186,312.2,170.8,312.2,170.8zM94.3,167.7l43.6-83.9l43.6,83.9H94.3z M192.1,161l-43.8-80.1h90.1L192.1,161z";
    public static final String SEAT1 = "M368.5,219.5l-2.1-6.6c0,0,36.4-8.3,35.2-16.5c-0.7-3.2-9.7-3.2-9.7-3.2l-69.8-3.2c0,0-10.7-0.7-3.5,8c7.2,8.7,14.1,11.8,28.5,14.5l6,15L368.5,219.5z";
    public static final String TIRE1 = "M600.3,254.8c-61.9,0-112.2,50.3-112.2,112.2s50.3,112.2,112.2,112.2S712.4,428.9,712.4,367S662.2,254.8,600.3,254.8z M600.3,460.3c-51.9,0-93.9-42-93.9-93.9s42-93.9,93.9-93.9s93.9,42,93.9,93.9S652.2,460.3,600.3,460.3zM285.8,257.4c-61.9,0-112.2,50.3-112.2,112.2s50.3,112.2,112.2,112.2S398,431.5,398,369.6S347.8,257.4,285.8,257.4z M285.8,463c-51.9,0-93.9-42-93.9-93.9s42-93.9,93.9-93.9s93.9,42,93.9,93.9S337.8,463,285.8,463z";
    public static final String HANDLEBAR1 = "M492.5,181.8h50.3c0,0,15.7-2.4,7.5,13.6c-11.2,16.3-18.4,31.4-18.4,31.4l-19.4-7.3l14.5-19.8c0,0-31.4-0.4-34.3,0C486.1,199,476,187.4,492.5,181.8z";

    // SET 2
    public static final String FRAME2 = "M281.8,359.4l-2.9,13l8.9,9.3l123.8,33.7c0,0,117.5-136,114.7-145.3c12.1,40,59.1,96.7,70.6,106.6c0,0,9.6-0.4,11.2-10.4c-41.6-69.1-58.7-71.6-78.5-147H359.8l-6.5,8L281.8,359.4z M374.5,239.6l142.2,0.3l2.1,7.9L415.9,380.2l-0.8-3.5L374.5,239.6z M294.6,365.2L357.3,253l39.5,138.1L294.6,365.2z";
    public static final String SEAT2 = "M353.3,227.5l15.2-8l-2.9-6.2c0,0,2.9,0.3,9.7,0c6.5-0.3,15.9-17.9-2.4-17.9c-19.9,0-55.4,0-67.2,0s-8.3,18,0,17.9c10.7,0,39.2,0,39.2,0L353.3,227.5z";
    public static final String TIRE2 = "M601.1,259.2c-60,0-108.7,48.6-108.7,108.7S541,476.7,601.1,476.7c60,0,108.7-48.6,108.7-108.7S661.2,259.2,601.1,259.2z M601.1,463.5c-52.8,0-95.6-42.8-95.6-95.6s42.8-95.6,95.6-95.6s95.6,42.8,95.6,95.6S654,463.5,601.1,463.5zM285.8,261.5c-60,0-108.7,48.6-108.7,108.7s48.6,108.7,108.7,108.7c60,0,108.7-48.6,108.7-108.7S346,261.5,285.8,261.5z M285.8,465.7c-52.8,0-95.6-42.8-95.6-95.6s42.8-95.6,95.6-95.6s95.6,42.8,95.6,95.6S338.6,465.7,285.8,465.7z";
    public static final String HANDLEBAR2 = "M512.4,219.5l-17.2-29.8c0,0-2.1-8,7.6-9.3c9.7-1.2,61.9,0.3,61.9,0.3s19.9,1.5,23.1,20.6c3.2,19.3-24.2,25.5-35.2,25.1c-11.1-0.7-11.5-14,0-14.3s17.6,0.7,19.3-8.4c1.5-9.1-9.7-8.4-9.7-8.4h-46l13.4,24.4L512.4,219.5z";

    // SET 3
    public static final String FRAME3 = "M610.1,365.6c-4.3-6.5-76.2-131-76.2-131l-4.2-15.1h-17.3c0-0.7-7.1,27.8-94.7,101.4l-49.4-102.2l-17.6,2.1l2.6,6.6l-82.5,151h160l-6.8-15.1H294.4L361.9,241l-3.5-4.8l72.2,142.2l19.5-6.5l78.9-129.2l-5.7,9.3l0.7-0.8l71.1,123.3C595,374.3,612.4,387.2,610.1,365.6z M438.8,359.1l-13.9-23.5c7.5-7.6,39.6-29.9,76.6-72.7L438.8,359.1z";
    public static final String SEAT3 = "M334,188.2c0,0,0,0-20.1,0c-14.7-2.9-10-18,0-17.9c10,0.3,57.5-0.3,63.4,0c11.1-1.5,14.3,18,0,17.9c-14.3-0.3-24,0-24,0l15.2,31.2l-15.2,8L334,188.2z";
    public static final String TIRE3 = "M603,255.1c-62.1,0-112.3,50.3-112.3,112.3S541,479.7,603,479.7s112.3-50.3,112.3-112.3S665.1,255.1,603,255.1z M602.5,448c-43.9,0-79.5-35.6-79.5-79.5s35.6-79.5,79.5-79.5c43.9,0,79.5,35.6,79.5,79.5C681.8,412.4,646.2,448,602.5,448zM286.5,256.7c-62.1,0-112.3,50.3-112.3,112.3s50.3,112.3,112.3,112.3s112.3-50.3,112.3-112.3S348.6,256.7,286.5,256.7z M285.8,449.7c-43.9,0-79.5-35.6-79.5-79.5s35.6-79.5,79.5-79.5s79.5,35.6,79.5,79.5S329.8,449.7,285.8,449.7z";
    public static final String HANDLEBAR3 = "M514.2,172.8c0,0,12-19.7,11.9-20.4s-23.5-25.9-23.5-25.9h-50l-0.7,14.7h43.9c0,0,7.2,12.7,7.2,13.2c0,0.4-8.4,15.7-8.4,15.7l17.9,49.4h17.3L514.2,172.8z";

    public SVGManager() {
        svgStringList = FXCollections.observableArrayList(FRAME1, COLOR_BLACK, SEAT1, COLOR_BLACK, TIRE1, COLOR_BLACK, HANDLEBAR1, COLOR_BLACK);
    }

    public static SVGManager getInstance() {
        return instance;
    }

    public void setSVGSet() {
        svgGroup.getChildren().clear();
        svgGroup.getChildren().addAll(getSVGPath(getSVGListElement(0), getSVGListElement(1)), getSVGPath(getSVGListElement(2), getSVGListElement(3)), getSVGPath(getSVGListElement(4), getSVGListElement(5)), getSVGPath(getSVGListElement(6), getSVGListElement(7)));
    }

    private SVGPath getSVGPath(String pathString, String colorString) {
        SVGPath path = new SVGPath();
        path.setFill(Color.web(colorString));
        path.setContent(pathString);
        return path;
    }

    public void setFrame(String frameString) {
        svgStringList.set(0, frameString);
    }

    public void setFrameColor(String frameColor) {
        svgStringList.set(1, frameColor);
    }

    public void setSeat(String seatString) {
        svgStringList.set(2, seatString);
    }

    public void setSeatColor(String seatColor) {
        svgStringList.set(3, seatColor);
    }

    public void setTire(String tireString) {
        svgStringList.set(4, tireString);
    }

    public void setTireColor(String tireColor) {
        svgStringList.set(5, tireColor);
    }

    public void setHandlebar(String hbString) {
        svgStringList.set(6, hbString);
    }

    public void setHandlebarColor(String hbColor) {
        svgStringList.set(7, hbColor);
    }

    public String getSVGListElement(Integer pos) {
        return svgStringList.get(pos);
    }
}
