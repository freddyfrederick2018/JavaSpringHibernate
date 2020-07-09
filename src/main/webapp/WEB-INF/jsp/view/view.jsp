<!DOCTYPE html>
<html>
    <head>
        <title>Post</title>
    </head>
    <body>
        <security:authorize access="isAnonymous()">
            <a href="<c:url value="/login" />">Login</a><br /><br />
            <a href="<c:url value="/register" />">register</a><br /><br />
        </security:authorize>
        
        <security:authorize access="hasAnyRole('USER','ADMIN')">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>
         
        <h2>Post #${ticket.id}: <c:out value="${ticket.subject}" /></h2>
        <security:authorize access="hasRole('ADMIN') or principal.username=='${ticket.customerName}'">
            [<a href="<c:url value="/ticket/edit/${ticket.id}" />">Edit</a>]
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')">
            [<a href="<c:url value="/ticket/delete/${ticket.id}" />">Delete</a>]
        </security:authorize>
        <security:authorize access="hasAnyRole('USER','ADMIN')">
            [<a href="<c:url value="/ticket/reply/${ticket.id}" />">Reply</a>]
        </security:authorize>
        <br /><br />
        <i>Original poster - <c:out value="${ticket.customerName}" /></i><br /><br />
        <i>Category - <c:out value="${ticket.category}" /></i><br /><br />
        <c:out value="${ticket.body}" /><br /><br />
        <c:if test="${fn:length(ticket.attachments) > 0}">
            Attachments:
            <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
                <c:if test="${!status.first}">, </c:if>
                <a href="<c:url value="/ticket/${ticket.id}/attachment/${attachment.name}" />">
                    <c:out value="${attachment.name}" /></a>
            </c:forEach><br /><br />
        </c:if>
        <c:if test="${fn:length(ticket.replies) > 0}">
            Replies:
            <table>
                <c:forEach items="${ticket.replies}" var="reply" >
                    <tr><td> ${reply.customerName}: </td>
                    <td>${reply.body}</td></tr>
                    <security:authorize access="hasRole('ADMIN')">
                        <tr>
                            <td>
                                [<a href="<c:url value="/ticket/reply/delete/${reply.id}" />">Delete</a>]<br><br>
                            </td>
                        </tr>
                    </security:authorize>
                </c:forEach><br /><br />
            </table>
        </c:if>
        <a href="<c:url value="/index" />">Return to list tickets</a>
    </body>
</html>
