<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Thêm danh mục</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            </head>

            <body>
                <div class="container mt-5">
                    <div class="row justify-content-center">
                        <div class="col-md-6 col-12">
                            <h3 class="text-center mb-4">Thêm danh mục</h3>

                            <!-- ✅ Gửi form với returnUrl -->
                            <form:form method="post" modelAttribute="category" action="/category/create">
                                <form:hidden path="id" />

                                <c:if test="${not empty returnUrl}">
                                    <input type="hidden" name="returnUrl" value="${returnUrl}" />
                                </c:if>

                                <div class="mb-3">
                                    <label class="form-label">Tên danh mục:</label>
                                    <form:input path="name" class="form-control" required="required" />
                                </div>

                                <button type="submit" class="btn btn-success">Lưu</button>

                                <c:choose>
                                    <c:when test="${not empty returnUrl}">
                                        <a href="${returnUrl}" class="btn btn-secondary">Hủy</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/book/create" class="btn btn-secondary">Hủy</a>
                                    </c:otherwise>
                                </c:choose>
                            </form:form>

                        </div>
                    </div>
                </div>
            </body>

            </html>