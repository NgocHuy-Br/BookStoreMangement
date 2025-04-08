<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page session="true" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Trang quản trị</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            </head>

            <body>

                <div class="container mt-5">
                    <div class="card shadow">
                        <div class="card-header bg-success text-white">
                            <h4>Chào mừng đến với trang quản trị Nhà sách</h4>
                        </div>
                        <div class="card-body">
                            <p><strong>Xin chào,</strong>
                                <c:out value="${sessionScope.loggedInUser.username}" /> 👋
                            </p>
                            <p>Vai trò: <span class="badge bg-info">
                                    <c:out value="${sessionScope.loggedInUser.role}" />
                                </span></p>

                            <hr>
                            <a href="/" class="btn btn-primary">Trang chủ</a>
                            <a href="/auth/logout" class="btn btn-danger">Đăng xuất</a>
                        </div>
                    </div>
                </div>

            </body>

            </html>