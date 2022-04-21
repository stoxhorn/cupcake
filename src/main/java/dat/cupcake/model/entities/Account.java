package dat.cupcake.model.entities;

import dat.cupcake.model.services.AccountEntityConverter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Account {
    private String email;
    private String role;
    private String password;
    private ArrayList<Kvittering> receipts;
    private float balance;

    private int cartId;


    public Account(String email, String role, String password, float balance, Kvittering[] receipts) {
        ArrayList<Kvittering> tmpL = new ArrayList<>();
        Collections.addAll(tmpL, receipts);
        constructor(email, role, password, balance, tmpL);
    }
    public Account(String email, String role, String password, float balance, ArrayList<Kvittering> receipts) {
        constructor(email, role, password, balance, receipts);
    }

    private void constructor(String email, String role, String password, float balance, ArrayList<Kvittering> receipts) {
        this.email = email;
        this.role = role;
        this.password = password;
        this.receipts = receipts;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return  "Email='" + email +
                "\nrole='" + role +
                "\npassword='" + password +
                "\nbalance=" + balance +
                "\nreceipts=" + getReceiptIDString();
    }

    public String fullToString() {
        return  "Email='" + email +
                "\nrole='" + role +
                "\npassword='" + password +
                "\nbalance=" + balance +
                "\nreceipts=" + AllKvitteringsString();
    }

    private String AllKvitteringsString(){
        String res = "";
        for(Kvittering k : this.receipts){
            res += k.fullToString() + " new Receipt: ";
        }
        return res;
    }

    private String getReceiptIDString(){
        String ret = "";

        for(Kvittering k : receipts){
            ret += k.getId() + ", ";
        }

        return ret;
    }


    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public float getBalance() {
        return balance;
    }

    public String getRoundedBalance(){
        DecimalFormat df = new DecimalFormat("0.00");

        return df.format(this.balance);
    }

    public Kvittering[] getReceiptsArray() {
        return receipts.toArray(new Kvittering[0]);
    }

    public ArrayList<Kvittering> getReceipts() {
        return receipts;
    }

    public Kvittering getKvittering(int index){return null;}

    public void addKvittering(Kvittering kvit){
        this.receipts.add(kvit);
    }

    public void setCartId(int i){
        for(Kvittering k : this.receipts){
            if(i == k.getId()){
                this.cartId = i;
            }
        }
    }

    public Kvittering getCart(){
        for(Kvittering k : this.receipts){
            if(k.getId() == this.cartId){
                return k;
            }
        }
        return null;
    }

    public boolean isCartEmpty(){
        return this.getCart().getOrders().isEmpty();
    }

    public void addToCart(Ordre newOrder){
        for(Kvittering k : this.receipts){
            if(k.getId() == this.cartId){
                k.addOrder(newOrder);
            }
        }
    }

    public ArrayList<String> getCartOrdersString(){
        return this.getCart().getOrderStringList();
    }

    public void addToBalance(float deposit){
        this.balance += deposit;

    }

    public boolean isAdmin(){
        return this.role.equals("admin");
    }
    public void setBalance(DBWallet depositWallet){
        if(depositWallet.getEmail().equals(this.email)){
            this.balance += depositWallet.getBalance();
        }
        else{
            throw new RuntimeException("Given wallet does not belong to this user");
        }
    }

    /**
     * returns a DBWalllet object with the propsed update to balance
     * DOES NOT UPDATE THIS OBJECT'S BALANCE
     * @param deposit
     * @return
     */
    public DBWallet getDepositWallet(float deposit){
        DBWallet dbw = AccountEntityConverter.AccountToWallet(this);
        return dbw.depositWallet(deposit);
    }

    public boolean canAffordCart(){
        return this.balance > this.getCart().getTotal();
    }

    public float getCartTotal(){
        return this.getCart().getTotal();
    }

    public void buyCart(){
        if(this.balance < this.getCart().getTotal()){
            return;
        }
        this.subtractFromBalance(this.getCart().getTotal());
        this.setCartStatus("paid");
    }

    public void subtractFromBalance(float withdraw){
        this.balance -= withdraw;
    }

    private int getCartSize(){
        return this.getCart().getOrders().size();
    }

    private void setCartStatus(String status){
        for(Kvittering k : this.receipts){
            k.setStatus(status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Float.compare(account.balance, balance) == 0 && Objects.equals(email, account.email) && Objects.equals(role, account.role) && Objects.equals(password, account.password) && Objects.equals(receipts, account.receipts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, role, password, receipts, balance);
    }
}
