<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>

                <html>

                <head>
                    <title>Thiết lập Chăm sóc Khách hàng</title>
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                </head>

                <body>
                    <div class="container mt-5">
                        <div class="row justify-content-center">
                            <div class="col-md-6 col-12">
                                <h3 class="text-center mb-4">🎯 Thiết lập Chăm sóc Khách hàng</h3>

                                <c:if test="${param.success != null}">
                                    <div class="alert alert-success">Cập nhật thành công!</div>
                                </c:if>

                                <form:form method="post" modelAttribute="setting">
                                    <form:hidden path="id" />
                                    <form:hidden path="bookstore.id" />

                                    <div class="mb-3">
                                        <label class="form-label">Phần trăm giảm giá cho thành viên (%):</label>
                                        <form:input path="discountRate" type="number" class="form-control" min="0"
                                            step="0.1" required="required" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Số điểm cần thiết để trở thành thành viên:</label>
                                        <form:input path="requiredPointsForMembership" type="number"
                                            class="form-control" min="0" required="required" />
                                    </div>

                                    <button type="submit" class="btn btn-success">💾 Lưu</button>
                                    <a href="/admin/dashboard" class="btn btn-secondary">⬅ Trở về</a>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </body>

                </html>