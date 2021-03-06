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


        <p>message: ${requestScope.message}</p>

        <c:if test="${sessionScope.user != null}">
            <p>You are logged in with the role of "${sessionScope.user.role}".</p>
            <form action="makeKvittering" form = "post">
                <input type="submit" value="Shop">
            </form>




        </c:if>

        <c:if test="${sessionScope.user == null}">
            <p>You are not logged in yet. You can do it here: <a
                    href="login.jsp">Login</a></p>
            <p>Otherwise if you don't have a user, create one here: <a
                    href="createUser.jsp">Create Account</a></p>
        </c:if>

        <c:if test="${sessionScope.user == null || sessionScope.user != null}">

            <p>TestServlet: <a
                    href="doTest.jsp">test</a></p>
            <form action="testServlet" method="post">
                <input type="submit"  value="run test"/>
            </form>
        </c:if>

    </jsp:body>

</t:pagetemplate>