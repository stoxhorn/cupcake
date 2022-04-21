package dat.cupcake.model.entities;

import java.util.Objects;

public class Udvalg {
    private String valg;
    private String type;
    private float pris;

    public Udvalg(String valg, String type, float pris) {
        this.valg = valg;
        this.type = type;
        this.pris = pris;
    }

    public String getValg() {
        return valg;
    }

    public void setValg(String valg) {
        this.valg = valg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPris() {
        return pris;
    }

    public void setPris(float pris) {
        this.pris = pris;
    }

    @Override
    public String toString() {
        return  "Valg: " + valg +
                "\ntype='" + type +
                "\npris=" + pris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Udvalg udvalg = (Udvalg) o;
        return Float.compare(udvalg.pris, pris) == 0 && Objects.equals(valg, udvalg.valg) && Objects.equals(type, udvalg.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valg, type, pris);
    }


}
