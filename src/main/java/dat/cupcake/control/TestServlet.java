package dat.cupcake.control;

import dat.cupcake.model.config.ApplicationStart;
import dat.cupcake.model.entities.*;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.*;
import dat.cupcake.model.services.AccountBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "testServlet", urlPatterns = {"/testServlet"} )
public class TestServlet extends HttpServlet
{
    private ConnectionPool connectionPool;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        // You shouldn't end up here with a GET-request, thus you get sent back to frontpage
        doPost(request, response);
        response.sendRedirect("testPage.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

        response.setContentType("text/html");
        HttpSession session = request.getSession();
        session.setAttribute("testString", null);
        try {

            String result = testAccountBuilder();

            DBKvittering kvit = new DBKvittering(4, "user", "test", "dato");

            KvitteringMapper kMapper = new KvitteringMapper(connectionPool);
            kMapper.createDBKvittering(kvit);

            session.setAttribute("testString", result);
            request.getRequestDispatcher("testPage.jsp").forward(request, response);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy()
    {

    }

    /**
     * tested to work
     * @return
     * @throws DatabaseException
     */
    private String testAccountBuilder() throws DatabaseException {
        String res = "";
        AccountBuilder ab = new AccountBuilder(this.connectionPool);
        populateDatabase();
        UserMapper userMapper = new UserMapper(this.connectionPool);
        WalletMapper wMapper = new WalletMapper(this.connectionPool);


        User newUser = userMapper.fetchUser("testEmail", "testPassword");


        ab  .addEmail(
                newUser.getEmail())
            .addPassword(
                newUser.getPassword())
            .addRole(
                newUser.getRole());

        DBWallet wallet = wMapper.getDBWallet(newUser.getEmail());

        ab.addBalance(wallet.getBalance());

        ab.fetchAllKvitterings();
        Account acc = ab.buildAccount();

        res += acc.fullToString();

        return res;
    }



    private String testMappers() throws DatabaseException{
        String result = testUserMapper();

        result += " <-----> " + testUMapper();
        result += " <-----> " + testKMapper();
        result += " <-----> " + testOMapper();
        result += " <-----> " + testWMapper();

        return result;
    }

    /**
     * tested positive
     * @return
     * @throws DatabaseException
     */
    private String testUserMapper() throws DatabaseException {
        UserMapper uMapper = new UserMapper(connectionPool);

        String result = "User made: ";
        result += uMapper.createUser("asd", "123", "user");

        result += " - User Updated: ";
        result += uMapper.updateUser(new User("asd","321", "admin"));

        result += " - User Deleted: ";
        result += uMapper.removeUser("asd");


        return result;
    }

    /**
     * tested positive
     * @return
     * @throws DatabaseException
     */
    private String testKMapper() throws DatabaseException {
        KvitteringMapper kMapper = new KvitteringMapper(connectionPool);

        String result = "kvittering made: ";
        result += kMapper.createDBKvittering(1, "user", "bestilt", "1/1-22");

        result += "kvittering Updated: ";
        result += kMapper.updateDBKvittering(new DBKvittering(1,"user", "betalt", "2/1-22"));

        String tmp = kMapper.getDBKvittering(1).toString();

        result += "kvittering Deleted: ";
        result += kMapper.removeDBKvittering(1);

        result += tmp;

        return result;
    }

    /**
     * tested positive
     * @return
     * @throws DatabaseException
     */
    private String testOMapper() throws DatabaseException {
        OrdreMapper oMapper = new OrdreMapper(connectionPool);
        KvitteringMapper kMapper = new KvitteringMapper(connectionPool);
        UdvalgMapper uMapper = new UdvalgMapper(connectionPool);

        kMapper.createDBKvittering(1, "user", "oprettet","test");
        uMapper.createDBUdvalg("vanilje", "top", 4.0F);
        uMapper.createDBUdvalg("chokolade", "bottom", 5.0F);


        String result = "Ordre made: ";
        result += oMapper.createDBOrdre(1, 1, "chokolade", "chokolade");

        result += "Ordre Updated: ";
        result += oMapper.updateDBOrdre(new DBOrdre(1,2, "chokolade", "vanilje"));

        String tmp = oMapper.fetchDBOrdre(1).toString();

        result += "Ordre Deleted: ";
        result += oMapper.removeDBOrdre(new DBOrdre(1,2, "vanilje", "chokolade"));

        result += tmp;

        kMapper.removeDBKvittering(1);

        return result;
    }

    /**
     * Tested Positive
     * @return
     * @throws DatabaseException
     */
    private String testUMapper() throws DatabaseException {
        UdvalgMapper uMapper = new UdvalgMapper(connectionPool);

        String result = "User made: ";
        result += uMapper.createDBUdvalg("chokolade", "bottom", 5.0F);

        result += " - User update: ";
        result += uMapper.updateDBUdvalg(new DBUdvalg("chokolade", "bottom", 1.0F));

        result += " - User delete: ";
        result += uMapper.removeDBUdvalg("chokolade");

        return result;
    }

    /**
     * tested positive
     * @return
     * @throws DatabaseException
     */
    private String testWMapper() throws DatabaseException {
        WalletMapper wMapper = new WalletMapper(connectionPool);
        String result = "wallet created: ";
        result += wMapper.createDBWallet("user");

        result += " - wallet updated: ";
        result += wMapper.updateDBWallet(new DBWallet("user", 1.0F));

        String tmp = wMapper.getDBWallet("user").toString();

        result += " - wallet removed: ";
        result +=  wMapper.removeDBWallet(new DBWallet("user", 0.F));

        result += tmp;

        return result;
    }

    private void populateDatabase() throws DatabaseException {
        WalletMapper wMapper = new WalletMapper(connectionPool);
        UserMapper userMapper = new UserMapper(connectionPool);
        UdvalgMapper uMapper = new UdvalgMapper(connectionPool);
        OrdreMapper oMapper = new OrdreMapper(connectionPool);
        KvitteringMapper kMapper = new KvitteringMapper(connectionPool);

        String email = "testEmail";
        String password = "testPassword";
        String role = "testRole";

        float balance = 4.20F;

        int kvitteringsId = 420;
        String status = "testStatus";
        String datoOprettet = "4/20-1337";

        int ordreId = 1337;
        String bottom = "testBottom";
        String bottomTo = "testBottomTo";

        String top = "testTop";
        String topTo = "testTopTo";

        String typeTop = "testTypeTop";
        String typeBottom = "testTypeBottom";
        float price = 3.50F;

        userMapper.createUser(email, password, role);

        wMapper.createDBWallet(email, balance);

        uMapper.createDBUdvalg(bottom, typeBottom, price);
        uMapper.createDBUdvalg(top, typeTop, price);

        kMapper.createDBKvittering(kvitteringsId, email, status, datoOprettet);

        oMapper.createDBOrdre(ordreId, kvitteringsId, bottom, top);



    }
}

