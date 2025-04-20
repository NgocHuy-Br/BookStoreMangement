<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <meta charset="UTF-8">
                    <title>Thêm tài khoản nhân viên</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <style>
                        .custom-alert {
                            display: inline-block;
                            padding: 10px 20px;
                            border-radius: 6px;
                            font-weight: 500;
                        }

                        .alert-container {
                            display: flex;
                            justify-content: center;
                            margin-bottom: 20px;
                        }
                    </style>
                </head>

                <body>
                    <div class="container mt-3">
                        <div class="row">
                            <div class="col-md-4 col-12 mx-auto">
                                <h3 class="text-center">Thêm tài khoản nhân viên</h3>
                                <hr />

                                <!-- Hiển thị thông báo -->
                                <div class="alert-container">
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger custom-alert">${error}</div>
                                    </c:if>

                                    <c:if test="${not empty success}">
                                        <div class="alert alert-success custom-alert">${success}</div>
                                    </c:if>
                                </div>


                                <form:form method="post" action="/admin/employee/create" modelAttribute="employee">
                                    <div class="mb-3">
                                        <label class="form-label">👤 Tên đăng nhập:</label>
                                        <form:input path="username" class="form-control" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">🔒 Mật khẩu:</label>
                                        <form:password path="password" class="form-control" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">⚙️ Vai trò:</label>
                                        <input type="text" class="form-control" value="Nhân viên cửa hàng" readonly />
                                        <form:hidden path="role" value="EMPLOYEE" />
                                    </div>

                                    <div class="d-flex justify-content-between">
                                        <button type="submit" class="btn btn-success">💾 Tạo tài khoản</button>
                                        <a href="/admin/employee" class="btn btn-secondary">⬅️ Quay lại</a>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </body>

                </html>