<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- nếu dùng format -->
            <html>

            <head>
                <title>Category List</title>
                <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" />
            </head>

            <body class="container mt-4">
                <h2>Categories</h2>
                <a href="${pageContext.request.contextPath}/categories/create" class="btn btn-success mb-3">+ Add
                    New</a>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${categories}" var="category">
                            <tr>
                                <td>${category.id}</td>
                                <td>${category.name}</td>
                                <td>
                                    <a class="btn btn-primary btn-sm"
                                        href="${pageContext.request.contextPath}/categories/edit/${category.id}">Edit</a>
                                    <a class="btn btn-danger btn-sm"
                                        href="${pageContext.request.contextPath}/categories/delete/${category.id}"
                                        onclick="return confirm('Delete this category?')">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </body>

            </html>