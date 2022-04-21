package dat.cupcake.model.services;

import dat.cupcake.model.entities.DBOrdre;
import dat.cupcake.model.entities.DBUdvalg;
import dat.cupcake.model.entities.Ordre;
import dat.cupcake.model.entities.Udvalg;
import dat.cupcake.model.persistence.ConnectionPool;

public class OrdreEntityConverter {
    public static DBOrdre ordreToDBOrdre(Ordre order) {
        return new DBOrdre(order.getID(), order.getKvitteringsId(), order.getBottom().getValg(), order.getTop().getValg());
    }

    public static Ordre dbOrdreToOrdre(DBOrdre dbo, DBUdvalg bottom,DBUdvalg top){

        Udvalg newBottom = UdvalgEntityConverter.dbUdvalgToUdvalg(bottom);
        Udvalg newTop = UdvalgEntityConverter.dbUdvalgToUdvalg(top);

        return dbOrdreToOrdre(dbo, newBottom, newTop);
    }

    public static Ordre dbOrdreToOrdre(DBOrdre dbo, Udvalg bottom, Udvalg top){

        OrdreBuilder ob = new OrdreBuilder();
        ob.addOrdreId(dbo.getOrdreId())
                .addKvitteringsId(dbo.getKvitteringsId())
                .addBottom(bottom)
                .addTop(top);

        return ob.buildOrdre();
    }

    public static Ordre dbOrdreFetchesOrdre(ConnectionPool cp, DBOrdre dbo){
        OrdreBuilder ob = new OrdreBuilder(cp);

        ob  .addOrdreId(dbo.getOrdreId())
            .addKvitteringsId(dbo.getKvitteringsId())
            .fetchBottom(dbo.getBottom())
            .fetchTop(dbo.getTop());
        Ordre o =ob.buildOrdre();

        return o;
    }

}
