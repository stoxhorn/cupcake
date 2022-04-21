package dat.cupcake.control;

import dat.cupcake.model.config.ApplicationStart;
import dat.cupcake.model.entities.Account;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;
import dat.cupcake.model.persistence.WalletMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "addMoney", urlPatterns = {"/addMoney"} )
public class AddMoney extends HttpServlet
{
    private ConnectionPool connectionPool;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        float deposit = Float.parseFloat(request.getParameter("deposit"));
        Account acc = (Account) session.getAttribute("user");

        WalletMapper wMapper = new WalletMapper(connectionPool);

        try {
            wMapper.updateDBWallet(acc.getDepositWallet(deposit));
            acc.addToBalance(deposit);
            session.setAttribute("user", acc);
            request.setAttribute("message", "successfully added to balance");
            request.getRequestDispatcher("/makeKvittering").forward(request, response);
        } catch (DatabaseException e) {
            throw new RuntimeException("error adding wallet to balance");
        }


    }


    public void destroy()
    {

    }
}
