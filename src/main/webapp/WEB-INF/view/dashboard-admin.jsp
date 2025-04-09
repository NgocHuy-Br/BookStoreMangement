<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page session="true" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Admin Dashboard</title>
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

                            <div class="row text-center">
                                <div class="col-md-4 mb-3">
                                    <a href="/admin/employees" class="btn btn-outline-primary w-100">👥 Quản lý nhân
                                        viên</a>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <a href="/admin/books" class="btn btn-outline-primary w-100">📚 Quản lý sách</a>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <a href="/admin/imports" class="btn btn-outline-primary w-100">📥 Nhập hàng</a>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <a href="/admin/invoices" class="btn btn-outline-primary w-100">🧾 Hóa đơn bán
                                        hàng</a>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <a href="/admin/statistics" class="btn btn-outline-primary w-100">📈 Thống kê</a>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <a href="/admin/customers" class="btn btn-outline-primary w-100">💳 Chăm sóc khách
                                        hàng</a>
                                </div>
                            </div>

                            <hr>
                            <a href="/" class="btn btn-primary">Trang chủ</a>
                            <a href="/auth/logout" class="btn btn-danger">Đăng xuất</a>
                        </div>
                    </div>
                </div>
            </body>

            </html>