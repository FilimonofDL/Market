package xyz.lavaliva.market.ZakazList;

import java.util.List;

public class
ZakazObj {
    String noZakaza,
            vremiaZakaza,
    itogoPoZakazu;
    List <TovarObjZakaz> tovarObjZakazList;

    public String getText() {
        return noZakaza;
    }

    public void setText(String text) {
        this.noZakaza = text;
    }

    public List<TovarObjZakaz> getTovarObjZakazList() {
        return tovarObjZakazList;
    }

    public void setTovarObjZakazList(List<TovarObjZakaz> tovarObjZakazList) {
        this.tovarObjZakazList = tovarObjZakazList;
    }

    public String getNoZakaza() {
        return noZakaza;
    }

    public void setNoZakaza(String noZakaza) {
        this.noZakaza = noZakaza;
    }

    public String getVremiaZakaza() {
        return vremiaZakaza;
    }

    public void setVremiaZakaza(String vremiaZakaza) {
        this.vremiaZakaza = vremiaZakaza;
    }

    public String getItogoPoZakazu() {
        return itogoPoZakazu;
    }

    public void setItogoPoZakazu(String itogoPoZakazu) {
        this.itogoPoZakazu = itogoPoZakazu;
    }

    public ZakazObj(String noZakaza, String vremiaZakaza, String itogoPoZakazu, List<TovarObjZakaz> tovarObjZakazList) {

        this.noZakaza = noZakaza;
        this.vremiaZakaza = vremiaZakaza;
        this.itogoPoZakazu = itogoPoZakazu;
        this.tovarObjZakazList = tovarObjZakazList;
    }
}
