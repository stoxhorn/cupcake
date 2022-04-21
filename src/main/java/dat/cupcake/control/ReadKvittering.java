package dat.cupcake.control;

import dat.cupcake.model.config.ApplicationStart;
import dat.cupcake.model.entities.Account;
import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;
import dat.cupcake.model.persistence.KvitteringMapper;
import dat.cupcake.model.services.KvitteringEntityConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "readKvittering", urlPatterns = {"/readKvittering"} )
public class ReadKvittering extends HttpServlet
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
        Account acc = (Account) session.getAttribute("user");
        if(!acc.getRole().equals("admin")){
            return;
        }

        String kvit = (String) request.getParameter("readKvittering");
        int id = Integer.parseInt(kvit);
        KvitteringMapper kMapper = new KvitteringMapper(connectionPool);
        try {
            Kvittering kvitterit = KvitteringEntityConverter
                    .DBKvitteringFetchesKvittering(
                            connectionPool,
                            kMapper.getDBKvittering(id));
            request.setAttribute("kvittering", kvitterit);
            request.getRequestDispatcher("/adminView.jsp").forward(request, response);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

    }


    public void destroy()
    {

    }
}
