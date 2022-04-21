package dat.cupcake.model.entities;

public class DBWallet {
    private String email;
    private float balance;

    public DBWallet(String email, float balance) {
        this.email = email;
        this.balance = balance;
    }

    /**
     * This constructor overload, is so i can create a new wallet object when adding balance,
     * so i don't accidentally destroy persistence, in case there's a failure when adding to the database.
     * @param oldWallet
     * @param deposit
     */
    public DBWallet(DBWallet oldWallet, float deposit) {
        this.email = oldWallet.getEmail();
        this.balance = oldWallet.getBalance() + deposit;
    }

    public DBWallet depositWallet(float desposit){
        return new DBWallet(this, desposit);
    }

    @Override
    public String toString(){
        return "Email: " + email +
                "\nBalance: " + balance;
    }

    public String getEmail() {
        return email;
    }

    public float getBalance() {
        return balance;
    }
}
