<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Đăng nhập</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

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
                        margin-top: 20px;
                        margin-bottom: 20px;
                    }
                </style>
            </head>

            <body>
                <div class="container mt-6">
                    <div class="card col-md-5 offset-md-3 shadow">
                        <div class="card-header bg-success text-white text-center">
                            <h4>ĐĂNG NHẬP HỆ THỐNG</h4>
                        </div>
                        <div class="card-body">
                            <form:form method="post" action="/auth/login" modelAttribute="user">
                                <div class="mb-3">
                                    <label class="form-label">👤 Tên đăng nhập:</label>
                                    <form:input path="username" class="form-control" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">🔒 Mật khẩu:</label>
                                    <form:password path="password" class="form-control" />
                                </div>
                                <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
                            </form:form>

                            <c:if test="${not empty message}">
                                <div class="alert-container">
                                    <div class="alert alert-info custom-alert">
                                        ${message}
                                    </div>
                                </div>
                                <c:remove var="bookMessage" scope="session" />
                            </c:if>

                            <hr>
                            <div class="text-center">
                                <p>Bạn chưa có tài khoản? <a href="/auth/register">Tạo một nhà sách mới</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </body>

            </html>