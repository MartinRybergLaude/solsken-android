package com.martinryberglaude.skyfall.utils_smhi;

import java.util.ArrayList;
import java.util.List;

public class SMHIPrecipitationCategory {
    public List<String> getCategoryList() {
        return categoryList;
    }

    private List<String> categoryList = new ArrayList<String>() {{
        add("No precipitation");
        add("Snow");
        add("Snow and rain");
        add("Rain");
        add("Drizzle");
        add("Freezing rain");
        add("Freezing drizzle");
    }};
}