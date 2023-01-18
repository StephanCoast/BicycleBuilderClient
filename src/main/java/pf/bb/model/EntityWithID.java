package pf.bb.model;

import com.google.gson.annotations.Expose;

/**
 * Abstrakte Klasse zur ID-Steuerung.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public abstract class EntityWithID {

    public int getId() {
        return id;
    }

    @Expose
    public int id;

    public String toString() {
        return String.format(this.getClass().getName() + "[id=%d]", id);
    }
}
