<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Đăng ký tài khoản Admin</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        </head>

        <body>
            <div class="container mt-2">
                <div class="row">
                    <div class="col-md-5 col-12 mx-auto">
                        <h3 class="text-center">📚 Đăng ký tài khoản Admin</h3>
                        <hr />
                        <div class="bg-light rounded shadow p-4">
                            <form:form method="post" action="/auth/register" modelAttribute="user">
                                <div class="mb-2">
                                    <label class="form-label">👤 Tên đăng nhập:</label>
                                    <form:input path="username" class="form-control" />
                                </div>
                                <div class="mb-2">
                                    <label class="form-label">🔒 Mật khẩu:</label>
                                    <form:password path="password" class="form-control" />
                                </div>

                                <div class="mb-2">
                                    <label class="form-label">⚙️ Vai trò:</label>
                                    <input type="text" class="form-control" value="Admin" readonly />
                                </div>

                                <div class="mb-2">
                                    <label class="form-label">🏪 Tên nhà sách:</label>
                                    <input type="text" name="bookstoreName" class="form-control" required>
                                </div>
                                <div class="mb-2">
                                    <label class="form-label">📍 Địa chỉ nhà sách:</label>
                                    <input type="text" name="bookstoreAddress" class="form-control" required>
                                </div>

                                <div class="container mt-4">
                                    <div class="d-flex justify-content-between">
                                        <button type="submit" class="btn btn-success">💾 Tạo tài khoản</button>
                                        <a href="/auth/login" class="btn btn-secondary">⬅️ Quay lại</a>
                                    </div>
                                </div>
                            </form:form>
                        </div>

                        <c:if test="${not empty message}">
                            <div class="alert alert-info mt-3 text-center">${message}</div>
                        </c:if>

                    </div>
                </div>
            </div>
        </body>

        </html>