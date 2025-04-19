<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <html>

            <head>
                <title>Thêm khách hàng</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
            </head>

            <body>
                <div class="container mt-5">
                    <div class="row justify-content-center">
                        <div class="col-md-4 col-12">
                            <h3 class="text-center mb-4">Thêm khách hàng</h3>
                            <form:form method="post" modelAttribute="customer">
                                <div class="mb-3">
                                    <label class="form-label">Tên khách hàng:</label>
                                    <form:input path="name" class="form-control" required="required" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Số điện thoại:</label>
                                    <form:input path="phone" class="form-control" required="required" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Email:</label>
                                    <form:input path="email" class="form-control" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Điểm tích lũy:</label>
                                    <form:input path="loyaltyPoints" type="number" class="form-control" value="0"
                                        readonly="true" />
                                </div>

                                <div class="d-flex justify-content-between">
                                    <button type="submit" class="btn btn-success">💾 Tạo mới</button>
                                    <a href="/customer" class="btn btn-secondary">⬅️ Quay lại</a>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </body>

            </html>