<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <html>

        <head>
            <title>Category Form</title>
            <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" />
        </head>

        <body class="container mt-4">
            <h2>Category Form</h2>
            <form method="post" action="${pageContext.request.contextPath}/categories/save">
                <input type="hidden" name="id" value="${category.id}" />
                <div class="mb-3">
                    <label>Name</label>
                    <input type="text" name="name" class="form-control" value="${category.name}" required />
                </div>
                <button type="submit" class="btn btn-success">Save</button>
                <a href="${pageContext.request.contextPath}/categories" class="btn btn-secondary">Cancel</a>
            </form>
        </body>

        </html>