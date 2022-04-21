package dat.cupcake.control;

import dat.cupcake.model.config.ApplicationStart;
import dat.cupcake.model.entities.Account;
import dat.cupcake.model.entities.DBOrdre;
import dat.cupcake.model.entities.Kvittering;
import dat.cupcake.model.exceptions.DatabaseException;
import dat.cupcake.model.persistence.ConnectionPool;
import dat.cupcake.model.persistence.OrdreMapper;
import dat.cupcake.model.services.OrdreEntityConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "makeOrder", urlPatterns = {"/makeOrder"} )
public class MakeOrder extends HttpServlet
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

        String message = "";

        String bottom = request.getParameter("bottom");
        String top = request.getParameter("top");
        bottom = bottom.substring(0,bottom.length()-1);
        top = top.substring(0,top.length()-1);
        OrdreMapper oMapper = new OrdreMapper(connectionPool);


        try {
            ArrayList<DBOrdre> dboL = oMapper.fetchAllDBOrdre();
            int i = dboL.size();
            int j = -1;
            while(true){
                for(DBOrdre dbo : dboL) {
                    if(i == dbo.getOrdreId()){
                        i++;
                        break;
                    }

                }
                j++;
                if(j == dboL.size()){
                    break;
                }
            }
            Account acc = (Account) session.getAttribute("user");
            ArrayList<Kvittering> kvL = acc.getReceipts();
            int kId = -1;
            for(Kvittering k : kvL){
                if(k.getId()>kId){
                    kId = k.getId();
                }
            }
            DBOrdre dbo = new DBOrdre(i, kId, bottom, top);
            boolean success = oMapper.createDBOrdre(dbo);
            acc.addToCart(
                    OrdreEntityConverter.
                            dbOrdreFetchesOrdre(connectionPool, dbo));

            if(success){
                message = "Successfully added order of ";
            }
            else{
                message = "Error adding order of ";
            }
            message += bottom + " and " + top;
            request.setAttribute("message", message);
            request.setAttribute("user", acc);
            request.getRequestDispatcher("shop.jsp").forward(request, response);

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }



    }


    public void destroy()
    {

    }
}


