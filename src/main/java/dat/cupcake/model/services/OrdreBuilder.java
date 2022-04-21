package dat.cupcake.model.services;

import dat.cupcake.model.entities.Ordre;
import dat.cupcake.model.entities.Udvalg;
import dat.cupcake.model.exceptions.ConverterException;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;
import dat.cupcake.model.persistence.UdvalgMapper;

public class OrdreBuilder {

    ConnectionPool cp;
    boolean noCp;

    int ordreId;
    int kvitteringsId;
    Udvalg[] choices;
    boolean[] missingFields;

    public OrdreBuilder(ConnectionPool cp){
        this.cp = cp;
        noCp = false;
        this.choices = new Udvalg[2];
        this.missingFields = new boolean[]{true, true, true, true};
    }

    public OrdreBuilder(){
        noCp = true;
        this.choices = new Udvalg[2];
        this.missingFields = new boolean[]{true, true, true, true};
    }

    public OrdreBuilder addConnectionPool(ConnectionPool cp){
        this.cp = cp;
        return this;
    }

    public OrdreBuilder addOrdreId(int id){
        this.ordreId = id;
        this.missingFields[0] = false;
        return this;
    }

    public OrdreBuilder addKvitteringsId(int kvitteringsId){
        this.kvitteringsId = kvitteringsId;
        this.missingFields[1] = false;
        return this;
    }

    public OrdreBuilder addBottom(Udvalg valg){
        this.choices[0] = valg;
        this.missingFields[2] = false;
        return this;
    }

    public OrdreBuilder fetchBottom(String valg){
        try {
            this.choices[0] = fetchUdvalgFromChoice(valg);
            this.missingFields[2] = false;
        } catch (ConverterException e) {
            throw new RuntimeException("error adding bottom to Order Builder");
        }
        return this;
    }

    public OrdreBuilder addTop(Udvalg valg){
        this.choices[1] = valg;
        this.missingFields[3] = false;
        return this;
    }

    public OrdreBuilder fetchTop(String valg){
        try {
            this.choices[1] = fetchUdvalgFromChoice(valg);
            this.missingFields[3] = false;
        } catch (ConverterException e) {
            throw new RuntimeException("error adding bottom to Order Builder");
        }
        return this;
    }

    private Udvalg fetchUdvalgFromChoice(String valg) throws ConverterException {
        if(noCp){
            throw new RuntimeException("No connectionPool given");
        }
        try {
            UdvalgMapper uMapper = new UdvalgMapper(this.cp);
            return uMapper.getUdvalg(valg);
        } catch (DatabaseException e) {
            throw new RuntimeException("Error finding a given choice in the DB");
        }
    }

    public Ordre buildOrdre(){
        String tmp = getMissingFields();
        if (tmp != null){
            throw new RuntimeException(tmp);
        }

        return new Ordre(this.ordreId, this.kvitteringsId, this.choices);

    }

    private String getMissingFields(){
        int check = readyToBuild();
        if (check == 0) return null;
        if (check == 1) return "Missing Ordre Id";
        if (check == 2) return "Missing kvitterings Id";
        if (check == 3) return "Missing bottom";
        if (check == 4) return "Missing top";

        return "readyToBuild() in OrdreBuilder is counting weirdly";
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
}
