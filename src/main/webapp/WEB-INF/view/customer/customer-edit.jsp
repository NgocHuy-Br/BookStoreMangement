<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
        <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Chỉnh sửa khách hàng</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                <style>
                    input[readonly] {
                        background-color: #f2f2f2;
                        color: #555;
                        border: 1px solid #ccc;
                    }
                </style>
            </head>

            <body>
                <div class="container mt-5">
                    <div class="row justify-content-center">
                        <div class="col-md-4 col-12">
                            <h3 class="text-center mb-4">Chỉnh sửa khách hàng</h3>

                            <form:form modelAttribute="customer" method="post" action="/customer/update">
                                <form:hidden path="id" />

                                <div class="mb-3">
                                    <label class="form-label">Tên:</label>
                                    <form:input path="name" class="form-control" required="required" />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Email:</label>
                                    <form:input path="email" class="form-control" />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">SĐT:</label>
                                    <form:input path="phone" class="form-control" />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Điểm tích lũy:</label>
                                    <form:input path="loyaltyPoints" class="form-control" type="number"
                                        readonly="true" />
                                </div>
                                <div class="container mt-3">
                                    <div class="d-flex justify-content-between">
                                        <button type="submit" class="btn btn-success">💾 Cập nhật</button>
                                        <a href="/customer" class="btn btn-secondary">⬅️ Quay lại</a>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </body>

            </html>