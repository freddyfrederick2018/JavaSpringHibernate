<!DOCTYPE html>
<html>
    <head>
        <title>Reply</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <h2>Reply</h2>
        <form:form method="POST"  modelAttribute="replyForm">
            <form:label path="body">Body</form:label><br/>
            <form:textarea path="body" required="true" rows="15" cols="60" /><br/><br/>
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>
