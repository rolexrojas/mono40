package com.tpago.movil.util;

/*LEAVE THIS HERE FOR REFERENCE*/
/*
    *    Name: ADEMI CODE: 38 ID: ADM
    *    Name: ADOPEM CODE: 44 ID: ADO
         Name: ALAVER CODE: 35 ID: ALV
         Name: BanReservas CODE: 4 ID: BRD
         Name: Banco BDI CODE: 36 ID: BDI
         Name: Banco Lopez De Haro CODE: 37 ID: BLH
         Name: Banco Union CODE: 45 ID: UNB
         Name: Banco del Progreso CODE: 24 ID: BDP
         Name: CITIBANK CODE: 34 ID: CTB
         Name: DKT CODE: 39 ID: DKT
         Name: Popular CODE: 5 ID: 102
         Name: Scotiabank CODE: 49 ID: SCT
    * */

public class Bank {
    public static final int ADEMI = 38;
    public static final int ADOPEM = 44;
    public static final int ALAVER = 35;
    public static final int BANRESERVAS = 4;
    public static final int BDI= 36;
    public static final int LOPEZ_DE_HARO = 37;
    public static final int UNION = 45;
    public static final int PROGRESO = 24;
    public static final int CITIBANK = 34;
    public static final int DKT = 39;
    public static final int POPULAR = 5;
    public static final int SCOTIABANK= 49;

    private String Email;
    private String DomesticPhone;
    private String FreeCallPhone;
    private String URL;
    private String Name;
    private String LogoURL;

    public Bank(int bankCode, String name, String logoURL){
        this.Email = getEmail(bankCode);
        this.DomesticPhone = getDomesticPhone(bankCode);
        this.FreeCallPhone = getFreeCallPhone(bankCode);
        this.URL = getURL(bankCode);
        this.Name = name;
        this.LogoURL = logoURL;
    }

    public String getEmail() {
        return Email;
    }

    public String getDomesticPhone() {
        return DomesticPhone;
    }

    public String getFreeCallPhone() {
        return FreeCallPhone;
    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return Name;
    }

    public String getLogoURL() {
        return LogoURL;
    }

    private String getEmail(int bankCode){
        switch (bankCode) {
            case ALAVER:
                return "elmejorservicio@alaver.com.do";
            case POPULAR:
                return "vozdelcliente@bpd.com.do";
            case UNION:
                return "contacto@bancounion.com.do";
            case SCOTIABANK:
                return "drinfo@scotiabank.com";
            default:
                return "";
        }
    }

    private String getDomesticPhone(int bankCode) {
        switch (bankCode) {
            case ADEMI:
                return "809-683-0203";
            case ADOPEM:
                return "809-563-3939";
            case ALAVER:
                return "809-573-2655";
            case BANRESERVAS:
                return "809-960-2121";
            case BDI:
                return "809-286-0004";
            case LOPEZ_DE_HARO:
                return "809-535-8994";
            case UNION:
                return "809-565-6191";
            case PROGRESO:
                return "809-566-7000";
            case POPULAR:
                return "809-544-5555";
            case SCOTIABANK:
                return "809-567-7268";
            default:
                return "";
        }
    }
    private String getFreeCallPhone(int bankCode) {
        switch (bankCode) {
            case ALAVER:
                return "809-200-1244";
            case BANRESERVAS:
                return "809-200-2131";
            case PROGRESO:
                return "809-200-7000";
            case POPULAR:
                return "809-200-5555";
            case SCOTIABANK:
                return "809-200-7268";
            default:
                return "";
        }
    }

    private String getURL(int bankCode) {
        switch (bankCode) {
            case ADEMI:
                return "www.bancoademi.com.do";
            case ADOPEM:
                return "www.bancoadopem.com.do";
            case ALAVER:
                return "www.alaver.com.do";
            case BANRESERVAS:
                return "www.banreservas.com";
            case BDI:
                return "www.bdi.com.do";
            case LOPEZ_DE_HARO:
                return "www.blh.com.do";
            case UNION:
                return "www.bancounion.com.do";
            case PROGRESO:
                return "www.progreso.com.do";
            case POPULAR:
                return "www.popularenlinea.com";
            case SCOTIABANK:
                return "www.scotiabank.com/do";
            default:
                return "";
        }
    }


}
