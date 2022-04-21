package dat.cupcake.model.entities;


import java.util.Arrays;
import java.util.Objects;

public class Ordre {
    private int ordreId;
    private int kvitteringsId;
    private Udvalg[] choices;

    public Ordre(int ordreId, int kvitteringsId, Udvalg[] choices) {
        this.choices = choices;
        this.ordreId = ordreId;
        this.kvitteringsId = kvitteringsId;
    }

    public String getNiceString(){
        Udvalg top = null;
        Udvalg bottom = null;
        for(Udvalg u : this.choices){

            String type = u.getType();

            if(type.equals("top")){top = u;}
            else if (type.equals("bottom")){bottom = u;}

        }
        float price = choices[0].getPris();
        price += choices[1].getPris();

        if(top == null){return null;}
        else if(bottom == null){return null;}
        else{
            return bottom.getValg() + " with " + top.getValg() + " costing: " + price;

        }
    }

    @Override
    public String toString() {
        return  "OrdreID: " + ordreId +
                "\nkvitteringsId=" + kvitteringsId +
                "\nchoices=" + getNiceString();
    }

    public int getID(){
        return 0;
    }

    public int getKvitteringsId() {
        return kvitteringsId;
    }

    public Udvalg[] getChoices() {
        return choices;
    }

    public Udvalg getTop(){
        for(Udvalg i : this.choices){
            if(i.getType() == "top"){
                return i;
            }
        }
        return null;
    }
    public Udvalg getBottom(){
        for(Udvalg i : this.choices){
            if(i.getType() == "bottom"){
                return i;
            }
        }
        return null;
    }

    public float getPrice(){
        float total = 0.0F;
        for(Udvalg o : this.choices){
            total += o.getPris();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Ordre ordre = (Ordre) o;
        return ordreId == ordre.ordreId && kvitteringsId == ordre.kvitteringsId && Arrays.equals(choices, ordre.choices);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ordreId, kvitteringsId);
        result = 31 * result + Arrays.hashCode(choices);
        return result;
    }
}
