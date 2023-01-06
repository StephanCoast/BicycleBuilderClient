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
    public static final String FRAME1 = "M410.5,323.1l-73.1-126.9l-0.6-8.2H170.4l0.2,7.8c0,0-63.5,117.9-68.7,127.7c-5.2,9.6,7.4,12.2,7.4,12.2h134.4l16.6-6.2l67.5-116.7c0,0,58.1,101.1,68.3,118.7C413.3,343.1,410.5,323.1,410.5,323.1z M123,318.9l57.5-110.7l57.5,110.7H123zM252.1,310.1l-57.7-105.7h118.9L252.1,310.1z";
    public static final String SEAT1 = "M152.4,158.9c0,0,0,0-19,0c-13.8-2.8-9.4-17,0-16.8c9.4,0.2,54.1-0.2,59.7,0c10.4-1.4,13.4,17,0,16.8c-13.4-0.2-22.6,0-22.6,0l14.2,29l-14.2,8L152.4,158.9z";
    public static final String TIRE1 = "M109.1,221.6C50.7,221.6,3.4,269,3.4,327.3S50.7,433,109.1,433s105.7-47.3,105.7-105.7S167.4,221.6,109.1,221.6zM109.1,415.2c-48.9,0-88.5-39.6-88.5-88.5s39.6-88.5,88.5-88.5s88.5,39.6,88.5,88.5S157.8,415.2,109.1,415.2zM403.3,221.6c-58.3,0-105.7,47.3-105.7,105.7S345,433,403.3,433S509,385.7,509,327.3S461.7,221.6,403.3,221.6z M403.3,415.2c-48.9,0-88.5-39.6-88.5-88.5s39.6-88.5,88.5-88.5s88.5,39.6,88.5,88.5S452.3,415.2,403.3,415.2z";
    public static final String HANDLEBAR1 = "M301.8,152.9h47.3c0,0,14.8-2.2,7,12.8c-10.6,15.4-18.8,30.6-18.8,30.6l-17.4-8l14.2-18.6c0,0-29.6-0.4-32.4,0C295.8,169.1,286.3,158.1,301.8,152.9z";

    // SET 2
    public static final String FRAME2 = "M85.3,324.1l5.6,21l134.6,27.8c0,0,110.7-128.1,108.1-136.8c11.4,37.8,47.7,95.1,58.5,104.5c0,0,9-0.4,10.6-9.8c-39.2-65.1-47.1-71.3-65.7-142.2l-153-0.4l-14.8,7.8L85.3,324.1z M190.6,207.3l134,0.2l2,7.4l-94.5,121.5h-3.4L190.6,207.3zM97.3,329.5l77.1-109.7l37.2,130.1L97.3,329.5z";
    public static final String SEAT2 = "M185.2,188.1l-2-6c0,0,34.4-7.8,33.2-15.6c-0.6-3-9.2-3-9.2-3l-65.7-3c0,0-10-0.6-3.2,7.6s13.8,11.2,27.4,13.8l4.8,14.2L185.2,188.1z";
    public static final String TIRE2 = "M403,221c-58.5,0-106,47.5-106,106s47.5,106,106,106s106-47.5,106-106S461.5,221,403,221z M402.5,400c-40.6,0-73.5-32.9-73.5-73.5c0-40.6,32.9-73.5,73.5-73.5s73.5,32.9,73.5,73.5C476,367.1,443.1,400,402.5,400z M109,222C50.5,222,3,269.5,3,328s47.5,106,106,106s106-47.5,106-106S167.5,222,109,222z M108.5,401 C67.9,401,35,368.1,35,327.5c0-40.6,32.9-73.5,73.5-73.5s73.5,32.9,73.5,73.5C182,368.1,149.1,401,108.5,401z";
    public static final String HANDLEBAR2 = "M320.4,188.1l-16-27.8c0,0-2-7.6,7.2-8.8s58.3,0.2,58.3,0.2s18.8,1.4,21.8,19.4c3,18.2-22.8,24-33.2,23.6c-10.4-0.6-10.8-13.2,0-13.4s16.6,0.6,18.2-8c1.4-8.6-9.2-8-9.2-8h-43.4l12.8,23.2L320.4,188.1z";

    // SET 3
    public static final String FRAME3 = "M410.5,323.1l-73.1-126.9l-0.6-8.2H170.4l0.2,7.8c-35.2,37-63.5,117.9-68.7,127.7c-5.2,9.6,7.4,12.2,7.4,12.2h134.4l16.6-6.2c0,0,55.3-76.3,67.5-116.7c0,0,58.1,101.1,68.3,118.7C413.3,343.1,410.5,323.1,410.5,323.1z M123,318.9c39.6-48.7,50.7-97.7,57.5-110.7c10.2,42.6,57.5,110.7,57.5,110.7S205.4,305.3,123,318.9z M252.1,310.1c0,0-28-78.3-57.7-105.7c11,0,48.3,16,118.9,0C275.3,246.6,252.1,310.1,252.1,310.1z";
    public static final String SEAT3 = "M170.6,195.9l14.8-7.8l-3-5.6c0,0,2.8,0.2,9.2,0c6.2-0.2,15-16.8-2.2-16.8c-18.8,0-52.1,0-63.3,0s-7.8,17,0,16.8c10,0,37,0,37,0L170.6,195.9z";
    public static final String TIRE3 = "M403,221c-58.5,0-106,47.5-106,106s47.5,106,106,106s106-47.5,106-106S461.5,221,403,221z M403,420c-51.4,0-93-41.6-93-93s41.6-93,93-93s93,41.6,93,93S454.4,420,403,420zM109,222C50.5,222,3,269.5,3,328s47.5,106,106,106s106-47.5,106-106S167.5,222,109,222z M109,421c-51.4,0-93-41.6-93-93s41.6-93,93-93s93,41.6,93,93S160.4,421,109,421z";
    public static final String HANDLEBAR3 = "M322.2,144.5l11.2-19.2L311.2,101h-47.1l-0.6,13.8h41.4c0,0,6.4,9.5,6.8,12.4s-8,14.8-8,14.8l16.4,46.1l16.8,0.4L322.2,144.5z";

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
