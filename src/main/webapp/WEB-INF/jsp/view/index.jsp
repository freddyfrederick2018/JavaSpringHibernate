<html>
    <head>
        <title>Course Forum</title>
    </head>
    <body>
        <h2>Course Forum</h2>

        <security:authorize access="isAnonymous()">
            <a href="<c:url value="/login" />">Login</a><br /><br />
            <a href="<c:url value="/login" />">register</a><br /><br />
        </security:authorize>

        <security:authorize access="hasAnyRole('USER','ADMIN')">
            <h2>Welcome back</h2>
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>

            
        <security:authorize access="hasRole('ADMIN')">
            <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
        </security:authorize>


        <security:authorize access="hasAnyRole('USER','ADMIN')">
            <a href="<c:url value="/ticket/create" />">Create a Thread</a><br /><br />
        </security:authorize>

        <ul>
            <li><a href="<c:url value="/ticket/lecture" />">Lecture</a><br /><br /></li>
            <li><a href="<c:url value="/ticket/lab" />">Lab</a><br /><br /></li>
            <li><a href="<c:url value="/ticket/other" />">Other</a><br /><br /></li>
        </ul>

    </body>
</html>
