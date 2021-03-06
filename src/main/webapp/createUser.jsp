<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page errorPage="error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
             Login
    </jsp:attribute>

    <jsp:attribute name="footer">
            Login
    </jsp:attribute>

    <jsp:body>

        <h3>Create Account Here:</h3>

        <form action="createAccount" method="post">
            <label for="email">Username: </label>
            <input type="text" id="email" name="email"/>

            <label for="password">Password: </label>
            <input type="password" id="password" name="password"/>

            <input type="submit"  value="Create Account"/>
        </form>

    </jsp:body>
</t:pagetemplate>