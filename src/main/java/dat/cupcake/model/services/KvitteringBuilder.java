package dat.cupcake.model.services;

import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.entities.Ordre;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;
import dat.cupcake.model.persistence.OrdreMapper;

import java.util.ArrayList;

public class KvitteringBuilder {

    ConnectionPool cp;
    boolean noCp;

    private int kvitteringsId;
    private ArrayList<Ordre> orders;
    private String email;
    private String status;
    private String datoOprettet;

    boolean[] missingFields;

    public KvitteringBuilder(ConnectionPool cp){
        this.cp = cp;
        this.noCp = false;
        this.orders = new ArrayList<>();
        this.missingFields = new boolean[]{true, true, true, true};
    }

    public KvitteringBuilder(){
        noCp = true;
        this.orders = new ArrayList<>();
        this.missingFields = new boolean[]{true, true, true, true};
    }

    public Kvittering buildKvittering(){
        String tmp = getMissingFields();
        if (tmp != null){
            throw new RuntimeException(tmp);
        }
        try {
            return new Kvittering(this.kvitteringsId, this.email, this.status, this.datoOprettet, this.orders);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error building kvittering with ID: " + this.kvitteringsId);
        }
    }

    private String getMissingFields(){
        int check = readyToBuild();
        if (check == 0) return null;
        if (check == 1) return "Missing kvitterings Id";
        if (check == 2) return "Missing email";
        if (check == 3) return "Missing status";
        if (check == 4) return "Missing datoOprettet";

        return "readyToBuild() in KvitteringBuilder is counting weirdly";
    }

    private int readyToBuild(){
        int i = 0;
        for(boolean bool : this.missingFields){
            if(bool) return i;
            i++;
        }
        i = 0;
        return i;
    }

    public KvitteringBuilder addID(int id){
        this.kvitteringsId = id;
        this.missingFields[0] = false;
        return this;
    }
    public KvitteringBuilder addEmail(String email){
        this.email = email;
        this.missingFields[1] = false;
        return this;
    }
    public KvitteringBuilder addStatus(String status){
        this.status = status;
        this.missingFields[2] = false;
        return this;
    }
    public KvitteringBuilder addDatoOprettet(String datoOprettet){
        this.datoOprettet = datoOprettet;
        this.missingFields[3] = false;
        return this;
    }

    public KvitteringBuilder addOrdre(Ordre order){
        this.orders.add(order);
        return this;
    }

    public KvitteringBuilder fetchAllOrdre(int id){
        try {
            if(noCp){
                throw new RuntimeException("No connectionPool given");
            }
            OrdreMapper oMapper = new OrdreMapper(this.cp);
            this.orders = oMapper.fetchAllKvitteringsOrdre(id);

        } catch (DatabaseException e) {
            throw new RuntimeException("Error fetching all Orders belonging to kvitteringsId: " + id);
        }
        return this;
    }

    public KvitteringBuilder fetchAllOrdre(){
        try {
            if(noCp){
                throw new RuntimeException("No connectionPool given");
            }
            if(this.missingFields[0]){
                throw new RuntimeException("Missing KvitteringsId");
            }
            OrdreMapper oMapper = new OrdreMapper(this.cp);
            this.orders = oMapper.fetchAllKvitteringsOrdre(this.kvitteringsId);

        } catch (DatabaseException e) {
            throw new RuntimeException("Error fetching all Orders belonging to kvitteringsId: " + this.kvitteringsId);
        }
        return this;
    }

}
