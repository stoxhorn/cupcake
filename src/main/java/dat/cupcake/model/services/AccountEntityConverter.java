package dat.cupcake.model.services;

import dat.cupcake.model.entities.Account;
import dat.cupcake.model.entities.DBWallet;
import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.entities.User;

import java.util.ArrayList;

public class AccountEntityConverter {
    public static Account DBToAccount(User user, DBWallet wallet){
        return new Account(user.getEmail(), user.getRole(), user.getPassword(), wallet.getBalance(), new ArrayList<Kvittering>());
    }
    public static  User AccountToUser(Account acc) {
        return new User(acc.getEmail(), acc.getRole(), acc.getPassword());
    }
    public static  DBWallet AccountToWallet(Account acc){
        return new DBWallet(acc.getEmail(), acc.getBalance());
    }


}
