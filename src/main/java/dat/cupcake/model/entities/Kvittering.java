package dat.cupcake.model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Kvittering {
    private int kvitteringsId;
    private String email;
    private String status;
    private String datoOprettet;
    private ArrayList<Ordre> orders;

    public Kvittering(int kvitteringsId, String email, String status, String datoOprettet, ArrayList<Ordre> orders) {
        constructorHelper(kvitteringsId, email, status, datoOprettet, orders);
    }

    public Kvittering(int kvitteringsId, String email, String status, String datoOprettet, Ordre[] orders) {
        ArrayList<Ordre> tmpList = new ArrayList<>();
        Collections.addAll(tmpList, orders);
        constructorHelper(kvitteringsId, email, status, datoOprettet, tmpList);

    }

    private void constructorHelper(int kvitteringsId, String email, String status, String datoOprettet, ArrayList<Ordre> orders){
        this.kvitteringsId = kvitteringsId;
        this.orders = new ArrayList<>();
        this.orders = orders;
        this.email = email;
        this.status = status;
        this.datoOprettet = datoOprettet;

    }

    public void addOrder(Ordre order){
        this.orders.add(order);
    }

    public int getId() {
        return kvitteringsId;
    }

    public ArrayList<Ordre> getOrders() {
        return this.orders;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public Ordre[] getOrdersArray() {
        return this.orders.toArray(new Ordre[0]);
    }

    /**
     * not implemented
     * @param index
     * @return
     */
    public Ordre getOrder(int index) {
        return null;
    }

    public float getTotal(){
        float total = 0.0F;
        for(Ordre o : this.orders){
            total += o.getPrice();
        }
        return total;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getDatoOprettet() {
        return datoOprettet;
    }

    private String orderIDString(){
        String ret = "";
        for(Ordre o: orders){
            ret += Integer.toString(o.getID()) + ", ";
        }
        return ret;
    }

    @Override
    public String toString() {
        return  "kvitteringsId: " + kvitteringsId +
                "\nemail='" + email +
                "\nstatus='" + status +
                "\ndatoOprettet='" + datoOprettet +
                "\nTotal Price" + this.getTotal();
    }

    public String fullToString() {
        return  "kvitteringsId: " + kvitteringsId +
                "\nemail='" + email +
                "\nstatus='" + status +
                "\ndatoOprettet='" + datoOprettet +
                "\norders: " + getOrderString();
    }

    private String getOrderString(){
        String res = "";
        for(Ordre o : this.orders){
            res += o.getNiceString() + ", ";
        }
        return res;
    }

    public ArrayList<String> getOrderStringList(){
        ArrayList<String> orders = new ArrayList<>();
        for(Ordre o : this.orders){
            orders.add(o.getNiceString());
        }
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kvittering that = (Kvittering) o;
        return kvitteringsId == that.kvitteringsId && Objects.equals(orders, that.orders) && Objects.equals(email, that.email) && Objects.equals(status, that.status) && Objects.equals(datoOprettet, that.datoOprettet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kvitteringsId, orders, email, status, datoOprettet);
    }
}
