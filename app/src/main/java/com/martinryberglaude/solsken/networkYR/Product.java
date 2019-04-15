
package com.martinryberglaude.solsken.networkYR;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("time")
    @Expose
    private List<Time> time = null;
    @SerializedName("class")
    @Expose
    private String _class;

    public List<Time> getTime() {
        return time;
    }

    public void setTime(List<Time> time) {
        this.time = time;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

}
