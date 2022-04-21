package dat.cupcake.model.entities;

public class DBKvittering {
    private int kvitteringsId;
    private String email;
    private String status;
    private String datoOprettet;

    public DBKvittering(int kvitteringsId, String email, String status, String datoOprettet) {
        this.kvitteringsId = kvitteringsId;
        this.email = email;
        this.status = status;
        this.datoOprettet = datoOprettet;
    }

    @Override
    public String toString(){
        return "Receipt ID: " + Integer.toString(kvitteringsId) +
                "\ndate: " + datoOprettet +
                "\nStatus: " + status +
                "\nEmail: " + email;
    }

    public int getKvitteringsId() {
        return kvitteringsId;
    }

    public String getDatoOprettet() {
        return datoOprettet;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
