package dat.cupcake.model.entities;

public class DBOrdre {

    private int ordreId;
    private int kvitteringsId;
    private String bottom;
    private String top;

    public DBOrdre(int ordreId, int kvitteringsId, String bottom, String top) {
        this.ordreId = ordreId;
        this.kvitteringsId = kvitteringsId;
        this.bottom = bottom;
        this.top = top;
    }


    @Override
    public String toString(){
        return "Order ID: " + ordreId +
                "\nkvitteringsId: " + kvitteringsId +
                "\nBottom: " + bottom +
                "\nTop: " + top;
    }

    public int getOrdreId() {
        return ordreId;
    }

    public int getKvitteringsId() {
        return kvitteringsId;
    }

    public String getBottom() {
        return bottom;
    }

    public String getTop() {
        return top;
    }
}

