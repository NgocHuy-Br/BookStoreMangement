<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html>

        <head>
            <title>Lỗi chưa đăng nhập</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        </head>

        <body>
            <div class="container mt-5 text-center">
                <h2 class="text-danger">🚫 Bạn chưa đăng nhập</h2>
                <p>Vui lòng nhấn vào nút đăng nhập để truy cập hệ thống !</p>
                <a href="/auth/login" class="btn btn-primary">🔐 Đăng nhập</a>
            </div>
        </body>

        </html>