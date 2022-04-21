package dat.cupcake.model.services;

import dat.cupcake.model.entities.DBUdvalg;
import dat.cupcake.model.entities.Udvalg;

public class UdvalgEntityConverter {
    public static DBUdvalg udvalgToDBUdvalg(Udvalg choice){
        return new DBUdvalg(choice.getValg(), choice.getType(), choice.getPris());
    }

    public static Udvalg dbUdvalgToUdvalg(DBUdvalg dbu){
        return new Udvalg(dbu.getValg(), dbu.getType(), dbu.getPrice());
    }
}
