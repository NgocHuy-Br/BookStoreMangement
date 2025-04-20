<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <title>Danh sách nhân viên</title>
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
                        margin-bottom: 20px;
                    }
                </style>

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
                    <div class="mb-4 text-center">
                        <a href="/admin/employee/create" class="btn btn-success">➕ Thêm mới tài khoản nhân viên</a>
                    </div>

                    <div class="text-center mt-5">
                        <form method="get" action="/admin/employee" class="row mb-3 justify-content-center">
                            <div class="col-md-4">
                                <input type="text" name="keyword" value="${keyword}" class="form-control"
                                    placeholder="🔍 Tìm theo tên đăng nhập..." />
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                            </div>
                        </form>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center mt-3">${error}</div>
                    </c:if>


                    <div class="text-center mt-5">
                        <div class="text-center mb-3">
                            <h3>📋 Danh sách nhân viên bán hàng</h3>
                        </div>
                    </div>

                    <c:if test="${not empty message}">
                        <div class="alert-container">
                            <div class="alert alert-success custom-alert">
                                ${message}
                            </div>
                        </div>
                    </c:if>

                    <table class="table table-bordered table-hover text-center align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Thứ tự</th>
                                <th>Tên đăng nhập</th>
                                <th>Vai trò</th>
                                <th>Nhà sách</th>
                                <th>Mật khẩu</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${employees}" var="e" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${e.username}</td>
                                    <td>Nhân viên cửa hàng</td>
                                    <td>${e.bookstore.name}</td>
                                    <td>*****</td>
                                    <td>
                                        <a href="/admin/employee/edit/${e.id}" class="btn btn-sm btn-warning">✏️ Sửa</a>
                                        <a href="/admin/employee/delete/${e.id}" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Bạn chắc chắn muốn xóa?')">🗑️ Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </body>

            </html>