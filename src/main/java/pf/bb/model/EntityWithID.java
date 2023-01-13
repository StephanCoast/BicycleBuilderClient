package pf.bb.model;

import com.google.gson.annotations.Expose;

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
