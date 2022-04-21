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
        <c:if test="${sessionScope.user.isAdmin()}">

            <div class = "container">
                <div class = "row">
                    <div class = "col-sm">
                        <p>Current users email: ${sessionScope.user.email}</p>
                    </div>
                    <div class = "col-sm">
                        <p>admin view</p>
                    </div>
                </div>
                <div class = "row">
                    <div class = "col-sm">
                        <form action="readKvittering" method="post">
                        <c:forEach items="${sessionScope.allReceipts}" var="receipt">
                            <label for="readKvittering">${receipt}</label>
                            <input type="radio" id="readKvittering" name="readKvittering" checked  value="${receipt.getId()}"/>
                       </c:forEach>
                            <div class="row">
                                <input type="submit" value="read more of selected Receipt"/>
                            </div>
                        </form>
                    </div>
                    <div class = "col-sm">
                        <p>Showing receipt with ID: ${requestScope.kvittering.getId()}</p>
                        <p>Email: ${requestScope.kvittering.getEmail()}</p>
                        <p>Status: ${requestScope.kvittering.getStatus()}</p>
                        <p>Made the date of : ${requestScope.kvittering.getDatoOprettet()}</p>
                        <p>Total cost of: ${requestScope.kvittering.getTotal()}</p>
                        <c:forEach items="${requestScope.kvittering.getOrderStringList()}" var="order">
                            <p>${order}</p>
                        </c:forEach>
                    </div>
                </div>
            <div class = "row">
                <p>Write a users email, and amount to add money to his wallet:</p>
                <form action="addMoneyToAccount" method="post">
                    <input type="text" name="email" id="email" value="users email">
                    <input type="number"  name="deposit" id="deposit" value="users email" value="0.0" step="0.01">
                    <input type="submit" name="addMoney" id="addMoney" value="add money">
                </form>
            </div>

            </div>

        </c:if>
        <c:if test="${!sessionScope.user.isAdmin()}">
            <p>Only admins are allowed on this page, click here to go back:<a
                    href="index">go back</a></p>
        </c:if>
        </c:if>



        <c:if test="${sessionScope.user == null}">
            <p>You are not logged in yet. You can do it here: <a
                    href="login.jsp">Login</a></p>
            <p>Otherwise if you don't have a user, create one here: <a
                    href="createUser.jsp">Create Account</a></p>
        </c:if>




    </jsp:body>

</t:pagetemplate>
