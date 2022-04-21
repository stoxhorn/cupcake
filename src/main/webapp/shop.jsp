<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
         Welcome to the frontpage
    </jsp:attribute>

    <jsp:attribute name="footer">
        Welcome to the frontpage
    </jsp:attribute>

    <jsp:body>


        <c:if test="${sessionScope.user != null}">

            <div class = "container">
                <div class = "row">
                    <div class = "col-sm">
                    <p>Current users email: ${sessionScope.user.email}</p>
                    <p>Current users balance: ${sessionScope.user.getRoundedBalance()}</p>
                    </div>
                    <div class = "col-sm">
                        <c:if test="${sessionScope.user.isCartEmpty()}">

                        </c:if>
                        <c:if test="${!sessionScope.user.isCartEmpty()}">
                            <p>Total Costs: ${sessionScope.user.getCartTotal()}</p>
                            <c:if test="${sessionScope.user.canAffordCart()}">
                                <p>TODO make a buy button</p>
                                <form action="buyCart" method="post">
                                    <input type="submit"  value="Buy"/>
                                </form>


                            </c:if>
                            <c:if test="${!sessionScope.user.canAffordCart()}">
                                <p>You cannot afford this</p>
                            </c:if>
                        </c:if>
                    </div>
                </div>
                <div class = "row">
                    <div class = "col-sm">
                        <form action="makeOrder" method="post">
                        <p>Du kan vælge bundene:</p>

                        <c:forEach items="${sessionScope.bottoms}" var="valg">
                            <label for="bottom">${valg.valg} </label>
                            <input type="radio" id="bottom" name="bottom" checked value = ${valg.valg}/>
                        </c:forEach>
                        <p>Du kan vælge toppene:</p>
                        <c:forEach items="${sessionScope.tops}" var="valg">
                            <label for="top">${valg.valg} </label>
                            <input type="radio" id="top" name="top" checked value = ${valg.valg}/>
                        </c:forEach>
                        <br>

                        <input type="submit"  value="Make Order"/>
                    </form>
                    </div>

                    <div class = "col-sm">
                        <c:forEach items="${sessionScope.user.getCartOrdersString()}" var="order">
                            <p>${order}</p>
                        </c:forEach>
                    </div>
                </div>

            </div>


        </c:if>



        <c:if test="${sessionScope.user == null}">
            <p>You are not logged in yet. You can do it here: <a
                    href="login.jsp">Login</a></p>
            <p>Otherwise if you don't have a user, create one here: <a
                    href="createUser.jsp">Create Account</a></p>
        </c:if>




    </jsp:body>

</t:pagetemplate>
