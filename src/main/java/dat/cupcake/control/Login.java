package dat.cupcake.control;

import dat.cupcake.model.config.ApplicationStart;
import dat.cupcake.model.entities.Account;
import dat.cupcake.model.entities.DBWallet;
import dat.cupcake.model.entities.Udvalg;
import dat.cupcake.model.entities.User;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "login", urlPatterns = {"/login"} )
public class Login extends HttpServlet
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
        response.sendRedirect("index.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        session.setAttribute("user", null); // adding empty user object to session scope
        User user = null;
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try
        {
            populateDatabase();
            Account acc = buildAccount(email, password);
            session = request.getSession();
            session.setAttribute("user", acc); // adding user object to session scope

            ArrayList<Udvalg> uv = getUdvalg();

            ArrayList<Udvalg> bottoms = new ArrayList<>();
            ArrayList<Udvalg> tops = new ArrayList<>();

            for(Udvalg u : uv){
                if(u.getType().equals("bottom")){
                    bottoms.add(u);
                }
                else if(u.getType().equals("top")){
                    tops.add(u);
                }
            }


            session.setAttribute("bottoms", bottoms);
            session.setAttribute("tops", tops);
            if(acc.getRole().equals("admin")){
                request.getRequestDispatcher("/prepareAdminView").forward(request, response);
            }
            request.getRequestDispatcher("/makeKvittering").forward(request, response);
        }
        catch (DatabaseException e)
        {
            Logger.getLogger("web").log(Level.SEVERE, e.getMessage());
            request.setAttribute("errormessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    public void destroy()
    {

    }
    
    public Account buildAccount(String email, String password) throws DatabaseException {
        AccountBuilder ab = new AccountBuilder(this.connectionPool);
        UserMapper userMapper = new UserMapper(connectionPool);
        WalletMapper wMapper = new WalletMapper(this.connectionPool);


        User user = userMapper.fetchUser(email, password);
        ab  .addEmail(
                        user.getEmail())
                .addPassword(
                        user.getPassword())
                .addRole(
                        user.getRole());
        DBWallet wallet = wMapper.getDBWallet(user.getEmail());


        if (wallet == null){
            wMapper.createDBWallet(email);
            wallet = wMapper.getDBWallet(user.getEmail());
        }

        ab.addBalance(wallet.getBalance());

        ab.fetchAllKvitterings();
         return ab.buildAccount();
    }

    public ArrayList<Udvalg> getUdvalg() throws DatabaseException {
        UdvalgMapper uMapper = new UdvalgMapper(this.connectionPool);
        return uMapper.getAllUdvalg();
    }


    private void populateDatabase() throws DatabaseException {
        WalletMapper wMapper = new WalletMapper(connectionPool);
        UdvalgMapper uMapper = new UdvalgMapper(connectionPool);


        String email = "user";

        float balance = 0.0F;

        String bottom = "chokolade";
        String bottomTo = "vanilje";

        String top = "krymmel";
        String topTo = "creme";

        String typeTop = "top";
        String typeBottom = "bottom";
        try{
            uMapper.createDBUdvalg(bottom, typeBottom, 5.0F);
            uMapper.createDBUdvalg(bottomTo, typeBottom, 7.0F);
            uMapper.createDBUdvalg(top, typeTop, 2.0F);
            uMapper.createDBUdvalg(topTo, typeTop, 3.0F);

            wMapper.createDBWallet(email);
        } catch (DatabaseException e) {

        }


    }
}

