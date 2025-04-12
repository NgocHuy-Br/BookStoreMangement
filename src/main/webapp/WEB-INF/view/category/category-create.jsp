<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Thêm danh mục</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
        </head>

        <body>
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <h3 class="text-center mb-4">Thêm danh mục</h3>
                        <form:form method="post" modelAttribute="category" class="border p-4 rounded bg-light">
                            <div class="mb-3">
                                <label class="form-label">Tên danh mục:</label>
                                <form:input path="name" class="form-control" />
                            </div>
                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-success">Lưu</button>
                                <a href="/book/create" class="btn btn-secondary">Hủy</a>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </body>

        </html>