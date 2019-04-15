
package com.martinryberglaude.solsken.networkYR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meta {

    @SerializedName("model")
    @Expose
    private List<Model> model;

    public List<Model> getModel() {
        return model;
    }

    public void setModel(List<Model> model) {
        this.model = model;
    }

}
