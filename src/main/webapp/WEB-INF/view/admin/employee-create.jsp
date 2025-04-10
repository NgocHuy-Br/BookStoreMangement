<%@ include file="admin-header.jsp" %>
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
                </head>

                <body>
                    <div class="container mt-5">
                        <div class="row">
                            <div class="col-md-6 col-12 mx-auto">
                                <h3 class="text-center">Thêm tài khoản nhân viên</h3>
                                <hr />
                                <form:form method="post" action="/admin/employee/create" modelAttribute="employee">
                                    <div class="mb-3">
                                        <label class="form-label">Tên đăng nhập:</label>
                                        <form:input path="username" class="form-control" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Mật khẩu:</label>
                                        <form:password path="password" class="form-control" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Vai trò:</label>
                                        <input type="text" class="form-control" value="Nhân viên" readonly />
                                        <form:hidden path="role" value="EMPLOYEE" />
                                    </div>

                                    <button type="submit" class="btn btn-success">Lưu</button>
                                    <a href="/admin/employee" class="btn btn-secondary">Hủy</a>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </body>

                </html>