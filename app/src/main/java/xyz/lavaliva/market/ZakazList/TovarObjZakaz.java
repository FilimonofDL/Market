package xyz.lavaliva.market.ZakazList;
public class TovarObjZakaz {
    String  idtovarzakaz,
            tovar,
            naimenovanie,
            foto,
            kolihestvovtovare,
            cena,
            primehanie
    ;

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public void setNaimenovanie(String naimenovanie) {
        this.naimenovanie = naimenovanie;
    }

    public String getIdtovarzakaz() {
        return idtovarzakaz;
    }

    public void setIdtovarzakaz(String idtovarzakaz) {
        this.idtovarzakaz = idtovarzakaz;
    }

    public String getTovar() {
        return tovar;
    }

    public void setTovar(String tovar) {
        this.tovar = tovar;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKolihestvovtovare() {
        return kolihestvovtovare;
    }

    public void setKolihestvovtovare(String kolihestvovtovare) {
        this.kolihestvovtovare = kolihestvovtovare;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getPrimehanie() {
        return primehanie;
    }

    public void setPrimehanie(String primehanie) {
        this.primehanie = primehanie;
    }

    public TovarObjZakaz(String idtovarzakaz, String tovar, String naimenovanie, String foto, String kolihestvovtovare, String cena, String primehanie) {

        this.idtovarzakaz = idtovarzakaz;
        this.tovar = tovar;
        this.naimenovanie = naimenovanie;
        this.foto = foto;
        this.kolihestvovtovare = kolihestvovtovare;
        this.cena = cena;
        this.primehanie = primehanie;
    }
}
