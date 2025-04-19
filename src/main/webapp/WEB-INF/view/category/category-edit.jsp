<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                <html>

                <head>
                    <title>Chỉnh sửa danh mục</title>
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                        
                </head>

                <body>
                    <div class="container mt-5">
                        <div class="row justify-content-center">
                            <div class="col-md-4 col-12">
                                <h3 class="text-center mb-4">Chỉnh sửa danh mục</h3>

                                <form:form method="post" modelAttribute="category" action="/category/edit">
                                    <form:hidden path="id" />

                                    <c:if test="${not empty returnUrl}">
                                        <input type="hidden" name="returnUrl" value="${returnUrl}" />
                                    </c:if>

                                    <div class="mb-3">
                                        <label class="form-label">Tên danh mục:</label>
                                        <form:input path="name" class="form-control" required="required" />
                                    </div>

                                    <div class="d-flex justify-content-between">
                                        <button type="submit" class="btn btn-success">💾 Cập nhật</button>
                                        <a href="${returnUrl != null ? returnUrl : '/category'}"
                                            class="btn btn-secondary">⬅️ Quay lại</a>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </body>

                </html>