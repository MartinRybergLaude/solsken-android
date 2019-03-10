package com.martinryberglaude.solsken.utils_weather;

public class YRWeatherSymbol {

    public String getSymbolString(int id) {
        String symbolString;
        switch (id) {
            case 1:
                symbolString = "Klart";
                break;
            case 2:
                symbolString = "Lätt molnighet";
                break;
            case 3:
                symbolString = "Delvis molnigt";
                break;
            case 4:
                symbolString = "Molnigt";
                break;
            case 5:
                symbolString = "Lätt regn";
                break;
            case 6:
                symbolString = "Åskväder";
                break;
            case 7:
                symbolString = "Snöblandat regn";
                break;
            case 8:
                symbolString = "Snöfall";
                break;
            case 9:
                symbolString = "Lätt regn";
                break;
            case 10:
                symbolString = "Regn";
                break;
            case 11:
                symbolString = "Åskväder";
                break;
            case 12:
                symbolString = "Snöblandat regn";
                break;
            case 13:
                symbolString = "Snöfall";
                break;
            case 14:
                symbolString = "Åskväder";
                break;
            case 15:
                symbolString = "Dimma";
                break;
            case 20:
                symbolString = "Åskväder";
                break;
            case 21:
                symbolString = "Åskväder";
                break;
            case 22:
                symbolString = "Åskväder";
                break;
            case 23:
                symbolString = "Åskväder";
                break;
            case 24:
                symbolString = "Åskväder";
                break;
            case 25:
                symbolString = "Åskväder";
                break;
            case 26:
                symbolString = "Åskväder";
                break;
            case 27:
                symbolString = "Åskväder";
                break;
            case 28:
                symbolString = "Åskväder";
                break;
            case 29:
                symbolString = "Åskväder";
                break;
            case 30:
                symbolString = "Åskväder";
                break;
            case 31:
                symbolString = "Åskväder";
                break;
            case 32:
                symbolString = "Åskväder";
                break;
            case 33:
                symbolString = "Åskväder";
                break;
            case 34:
                symbolString = "Åskväder";
                break;
            case 40:
                symbolString = "Duggregn";
                break;
            case 41:
                symbolString = "Regn";
                break;
            case 42:
                symbolString = "Lätt snöblandat regn";
                break;
            case 43:
                symbolString = "Kraftig snöblandat regn";
                break;
            case 44:
                symbolString = "Lätt snöfall";
                break;
            case 45:
                symbolString = "Kraftigt snöfall";
                break;
            case 46:
                symbolString = "Duggregn";
                break;
            case 47:
                symbolString = "Lätt snöblandat regn";
                break;
            case 48:
                symbolString = "Kraftig snöblandat regn";
                break;
            case 49:
                symbolString = "Lätt snöfall";
                break;
            case 50:
                symbolString = "Kraftigt snöfall";
                break;
            default:
                 symbolString = "Klart";
        }
        return symbolString;
    }
}
