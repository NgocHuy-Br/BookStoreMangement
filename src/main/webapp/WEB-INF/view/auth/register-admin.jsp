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
            <div class="container mt-5">
                <div class="row">
                    <div class="col-md-6 col-12 mx-auto">
                        <h3 class="text-center">Đăng ký tài khoản Admin</h3>
                        <hr />
                        <form:form method="post" action="/auth/register" modelAttribute="user">
                            <div class="mb-3">
                                <label class="form-label">Tên đăng nhập:</label>
                                <form:input path="username" class="form-control" />
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Mật khẩu:</label>
                                <form:password path="password" class="form-control" />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Tên nhà sách:</label>
                                <input type="text" name="bookstoreName" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Địa chỉ nhà sách:</label>
                                <input type="text" name="bookstoreAddress" class="form-control" required>
                            </div>

                            <button type="submit" class="btn btn-primary">Đăng ký</button>
                        </form:form>

                        <c:if test="${not empty message}">
                            <div class="alert alert-info mt-3">${message}</div>
                        </c:if>
                    </div>
                </div>
            </div>
        </body>

        </html>