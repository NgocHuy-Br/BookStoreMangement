<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>

                <html>

                <head>
                    <title>Thêm nhà cung cấp</title>

                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                </head>

                <body>

                    <div class="container mt-5">
                        <div class="row justify-content-center">
                            <div class="col-md-4 col-8">
                                <h3 class="text-center mb-4">Thêm nhà cung cấp</h3>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger text-center">${error}</div>
                                </c:if>

                                <form:form method="post" modelAttribute="supplier">
                                    <form:hidden path="id" />
                                    <input type="hidden" name="returnUrl" value="${returnUrl}" />

                                    <div class="mb-3">
                                        <label class="form-label">Tên nhà cung cấp:</label>
                                        <form:input path="name" class="form-control" required="required" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Số điện thoại:</label>
                                        <form:input path="phone" class="form-control" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Địa chỉ:</label>
                                        <form:input path="address" class="form-control" />
                                    </div>

                                    <div class="d-flex justify-content-between">
                                        <button type="submit" class="btn btn-success">💾 Tạo mới</button>
                                        <a href="${returnUrl}" class="btn btn-secondary">⬅️ Quay lại</a>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>

                </body>

                </html>