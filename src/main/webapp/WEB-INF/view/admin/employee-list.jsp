<%@ include file="admin-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

            <div class="container mt-4">
                <h4><i class="fa-solid fa-users"></i> Danh sách nhân viên</h4>

                <form class="d-flex mb-3" method="get" action="/admin/employee">
                    <input type="text" name="keyword" value="${keyword}" class="form-control me-2"
                        placeholder="Tìm theo tên đăng nhập...">
                    <button type="submit" class="btn btn-secondary"><i class="fa-solid fa-magnifying-glass"></i> Tìm
                        kiếm</button>
                </form>

                <a href="/admin/employee/create" class="btn btn-outline-primary mb-3">
                    <i class="fa-solid fa-plus"></i> Thêm mới tài khoản nhân viên
                </a>

                <table class="table table-bordered table-striped text-center align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th><i class="fa-solid fa-user"></i> Tên đăng nhập</th>
                            <th><i class="fa-solid fa-user-shield"></i> Vai trò</th>
                            <th><i class="fa-solid fa-store"></i> Nhà sách</th>
                            <th><i class="fa-solid fa-lock"></i> Mật khẩu</th>
                            <th><i class="fa-solid fa-wrench"></i> Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${employees}" var="emp" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${emp.username}</td>
                                <td>${emp.role}</td>
                                <td>${emp.bookstore.name}</td>
                                <td>******</td>
                                <td>
                                    <a href="/admin/employee/edit/${emp.id}" class="btn btn-warning btn-sm">Sửa</a>
                                    <a href="/admin/employee/delete/${emp.id}" class="btn btn-danger btn-sm"
                                        onclick="return confirm('Bạn có chắc muốn xóa nhân viên này không?')">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>