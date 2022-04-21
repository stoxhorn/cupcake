package dat.cupcake.control;

import dat.cupcake.model.config.ApplicationStart;
import dat.cupcake.model.entities.Account;
import dat.cupcake.model.entities.DBKvittering;
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
import java.util.ArrayList;

import static java.time.LocalTime.now;

@WebServlet(name = "makeKvittering", urlPatterns = {"/makeKvittering"} )
public class MakeKvittering extends HttpServlet
{
    private ConnectionPool connectionPool;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
        response.sendRedirect("index.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        Account acc = (Account) session.getAttribute("user");

        ArrayList<Kvittering> kvL = acc.getReceipts();
        KvitteringMapper kMapper = new KvitteringMapper(connectionPool);

        int id = -1;

        for(Kvittering k : kvL){
            if(k.getStatus().equals("cart")){
                id = k.getId();
            }
        }

            try {
                boolean success = false;
                if(id == -1) {
                    ArrayList<DBKvittering> dbkL = kMapper.fetchAllDBKvittering();
                    id = dbkL.size();
                    int j = -1;
                    while (true) {
                        for (DBKvittering dbk : dbkL) {
                            if (id == dbk.getKvitteringsId()) {
                                id++;
                                break;
                            }
                        }
                        j++;
                        if (j == dbkL.size()) {
                            break;
                        }
                    }
                    DBKvittering dbk = new DBKvittering(id, acc.getEmail(), "cart", now().toString());
                    success = kMapper.createDBKvittering(dbk);
                    Kvittering kvit = KvitteringEntityConverter.dbKvitteringToKvitterinng(dbk);
                    acc.addKvittering(kvit);
                    acc.setCartId(kvit.getId());
                    session.setAttribute("user", acc);
                }
                else{
                    DBKvittering dbk = new DBKvittering(id, acc.getEmail(), "cart", now().toString());
                    Kvittering kvit = KvitteringEntityConverter.dbKvitteringToKvitterinng(dbk);
                    acc.setCartId(kvit.getId());
                    session.setAttribute("user", acc);
                    success = true;
                }

                String message = "";
                if(success){
                    message += "Success creating new kvittering ";
                }
                else{
                    message += "Error creating new kvittering ";
                }
                message += "with ID: " + id;
                request.setAttribute("message", message);
                request.getRequestDispatcher("shop.jsp").forward(request, response);

            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }





    public void destroy()
    {

    }
}
