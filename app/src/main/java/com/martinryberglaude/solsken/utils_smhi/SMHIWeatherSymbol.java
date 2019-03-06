package com.martinryberglaude.solsken.utils_smhi;

import java.util.ArrayList;
import java.util.List;

public class SMHIWeatherSymbol {
    public List<String> getSymbolList() {
        return symbolList;
    }

    private List<String> symbolList = new ArrayList<String>() {{
        add("Klart");
        add("Nästan klart");
        add("Varierande molnighet");
        add("Halvklart");
        add("Molnigt");
        add("Mulet");
        add("Dimma");
        add("Lätta regnskurar");
        add("Måttliga regnskurar");
        add("Kraftiga regnskurar");
        add("Åskväder");
        add("Lätta skurar snöblandat regn");
        add("Måttliga skurar snöblandat regn");
        add("Kraftiga skurar snöblandat regn");
        add("Lätta snöskurar");
        add("Måttliga snöskurar");
        add("Kraftiga snöskurar");
        add("Lätt regn");
        add("Måttligt regn");
        add("Kraftigt regn");
        add("Åska");
        add("Lätt snöblandat regn");
        add("Måttligt snöblandat regn");
        add("Kraftigt snöblandat regn");
        add("Lätt snöfall");
        add("Måttligt snöfall");
        add("Kraftigt snöfall");
    }};

}
