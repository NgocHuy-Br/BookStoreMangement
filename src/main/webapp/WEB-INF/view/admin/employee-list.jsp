<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

            <html>

            <head>
                <title>Danh sách nhân viên</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            </head>

            <body>
                <div class="container mt-5">
                    <div class="text-center mb-4">
                        <h3>📋 Danh sách nhân viên</h3>
                    </div>

                    <form method="get" action="/admin/employee" class="row mb-3 justify-content-center">
                        <div class="col-md-4">
                            <input type="text" name="keyword" value="${keyword}" class="form-control"
                                placeholder="Tìm theo tên đăng nhập..." />
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                        </div>
                    </form>

                    <div class="mb-3 text-end">
                        <a href="/admin/employee/create" class="btn btn-success">➕ Thêm mới tài khoản nhân viên</a>
                    </div>

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
                                    <td>${e.role}</td>
                                    <td>${e.bookstore.name}</td>
                                    <td>*****</td>
                                    <td>
                                        <a href="/admin/employee/edit/${e.id}" class="btn btn-sm btn-warning">Sửa</a>
                                        <a href="/admin/employee/delete/${e.id}" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Bạn chắc chắn muốn xóa?')">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </body>

            </html>