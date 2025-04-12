<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page session="true" %>

        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Dashboard Nhân viên</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        </head>

        <body>

            <div class="container mt-5">
                <div class="alert alert-info">
                    <h4>Chào mừng nhân viên <strong>${sessionScope.loggedInUser.username}</strong>!</h4>
                    <p>Bạn đã đăng nhập thành công vào hệ thống quản lý nhà sách.</p>
                </div>
                <a href="/auth/logout" class="btn btn-danger">Đăng xuất</a>
            </div>

        </body>

        </html>