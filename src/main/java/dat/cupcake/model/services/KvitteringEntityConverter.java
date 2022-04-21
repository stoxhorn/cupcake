package dat.cupcake.model.services;

import dat.cupcake.model.entities.DBKvittering;
import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.persistence.ConnectionPool;

public class KvitteringEntityConverter {
    public static DBKvittering kvitteringToDBKvittering(Kvittering kvit){
        return new DBKvittering(kvit.getId(), kvit.getEmail(), kvit.getStatus(), kvit.getDatoOprettet());
    }

    public static Kvittering dbKvitteringToKvitterinng(DBKvittering dbk){

        KvitteringBuilder kb = new KvitteringBuilder();

        kb  .addID(
                dbk.getKvitteringsId())
            .addEmail(
                dbk.getEmail())
            .addStatus(
                dbk.getStatus())
            .addDatoOprettet(
                dbk.getDatoOprettet());

        return kb.buildKvittering();
    }

    public static Kvittering DBKvitteringFetchesKvittering(ConnectionPool cp, DBKvittering dbk){
        KvitteringBuilder kb = new KvitteringBuilder(cp);

        kb  .addID(
                dbk.getKvitteringsId())
            .addEmail(
                dbk.getEmail())
            .addStatus(
                dbk.getStatus())
            .addDatoOprettet(
                dbk.getDatoOprettet())
            .fetchAllOrdre();

        return kb.buildKvittering();
    }
}
