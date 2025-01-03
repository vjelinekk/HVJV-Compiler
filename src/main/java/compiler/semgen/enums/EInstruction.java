package compiler.semgen.enums;

public enum EInstruction {
    /** Nahraje konstantní hodnotu na zásobník.**/
    LIT("LIT"),
    OPR("OPR"), // Provádí aritmetické nebo logické operace (např. sčítání, odčítání, porovnání).
    LOD("LOD"), // Načte hodnotu z paměti (proměnné) na zásobník.
    STO("STO"), // Uloží hodnotu ze zásobníku do paměti (proměnné).
    CAL("CAL"), // Zavolá proceduru – uloží návratovou adresu a nastaví nový stack frame.
    INT("INT"), // Inicializuje prostor na zásobníku pro proměnné nebo návratové hodnoty.
    JMP("JMP"), // Bezpodmínečný skok na specifikovanou instrukci.
    JMC("JMC"), // Podmíněný skok, pokud je hodnota na vrcholu zásobníku `0`.
    RET("RET"); // Návrat z procedury – obnoví předchozí stack frame a návratovou adresu.

    private final String translation;

    EInstruction (String translation) {
        this.translation = translation;
    }


    public static EInstruction getSymbol(String value) {
        for (EInstruction e : EInstruction.values()) {
            if (e.translation.equals(value)) {
                return e;
            }
        }
        return null;
    }

    public String getTranslation() {
        return translation;
    }
}
