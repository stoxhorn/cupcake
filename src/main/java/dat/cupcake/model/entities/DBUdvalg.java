package dat.cupcake.model.entities;

public class DBUdvalg {

    private String valg;
    private String type;
    private float price;

    public DBUdvalg(String valg, String type, float price) {
        this.valg = valg;
        this.type = type;
        this.price = price;
    }

    @Override
    public String toString(){
        return "Bottom: " + valg + " price: " + Float.toString(price);
    }

    public float getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getValg() {
        return valg;
    }
}
