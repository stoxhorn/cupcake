package dat.cupcake.model.services;

import dat.cupcake.model.entities.Account;
import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;
import dat.cupcake.model.persistence.KvitteringMapper;

import java.util.ArrayList;

public class AccountBuilder {
    private ConnectionPool cp;
    private boolean noCp;
    private boolean[] missingFields;

    private String email;
    private String role;
    private String password;
    private float balance;
    private ArrayList<Kvittering> receipts;


    public AccountBuilder(ConnectionPool cp){
        this.cp = cp;
        this.noCp = false;
        receipts = new ArrayList<>();
        this.missingFields = new boolean[]{true, true, true, true};
    }

    public AccountBuilder(){
        noCp = true;
        receipts = new ArrayList<>();
        this.missingFields = new boolean[]{true, true, true, true};
    }

    public AccountBuilder(Account acc){
        this.email = acc.getEmail();
        this.password = acc.getPassword();
        this.balance = acc.getBalance();
        this.role = acc.getRole();
    }

    public AccountBuilder addConnectionPool(ConnectionPool cp){
        this.cp = cp;
        return this;
    }

    public AccountBuilder addEmail(String email){
        this.email = email;
        this.missingFields[0] = false;
        return this;
    }

    public AccountBuilder addRole(String role){
        this.role = role;
        this.missingFields[1] = false;
        return this;
    }

    public AccountBuilder addPassword(String password){
        this.password = password;
        this.missingFields[2] = false;
        return this;
    }
    public AccountBuilder addBalance(Float balance){
        this.balance = balance;
        this.missingFields[3] = false;
        return this;
    }

    public AccountBuilder addKvittering(Kvittering kvit){
        this.receipts.add(kvit);
        return this;
    }

    public AccountBuilder fetchAllKvitterings(){
        try {
            if(noCp){
                throw new RuntimeException("No connectionPool given");
            }
            if(this.missingFields[0]){
                throw new RuntimeException("Missing Email");
            }
            KvitteringMapper kMapper = new KvitteringMapper(this.cp);
            this.receipts = kMapper.fetchAllKvitteringFromEmail(this.email);

        } catch (DatabaseException e) {
            throw new RuntimeException("Error fetching all kvitterings belonging to the email: " + this.email);
        }

        return this;
    }

    public Account buildAccount(){
        String tmp = getMissingFields();
        if (tmp != null){
            throw new RuntimeException(tmp);
        }
        try {
            return new Account(this.email, this.role, this.password, this.balance, this.receipts);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error building Account with email: " + this.email);
        }
    }

    private String getMissingFields(){
        int check = readyToBuild();
        if (check == 0) return null;
        if (check == 1) return "Missing Email";
        if (check == 2) return "Missing Role";
        if (check == 3) return "Missing Password";
        if (check == 4) return "Missing balance";

        return "readyToBuild() in AccountBuilder is counting weirdly";
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
