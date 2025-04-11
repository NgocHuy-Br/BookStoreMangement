<%@ include file="admin-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Quản lý sách</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                <style>
                    .table th,
                    .table td {
                        vertical-align: middle;
                        text-align: center;
                    }

                    .btn-action {
                        margin: 0 2px;
                    }
                </style>
            </head>

            <body>
                <div class="container mt-5">
                    <h3 class="text-center mb-4"><i class="bi bi-book"></i> Danh sách sách</h3>

                    <form class="row g-3 justify-content-center mb-3" method="get"
                        action="${pageContext.request.contextPath}/books">
                        <div class="col-auto">
                            <input type="text" class="form-control" name="keyword" placeholder="Tìm theo tên sách..."
                                value="${keyword}">
                        </div>
                        <div class="col-auto">
                            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                        </div>
                    </form>

                    <div class="text-end mb-3">
                        <a href="${pageContext.request.contextPath}/books/create" class="btn btn-success">
                            <i class="bi bi-plus-circle"></i> Thêm sách mới
                        </a>
                    </div>

                    <table class="table table-bordered table-hover">
                        <thead class="table-secondary">
                            <tr>
                                <th>Thứ tự</th>
                                <th>Tên sách</th>
                                <th>Tác giả</th>
                                <th>Giá</th>
                                <th>Số lượng</th>
                                <th>Danh mục</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="book" items="${books}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.price}</td>
                                    <td>${book.quantity}</td>
                                    <td>${book.category.name}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/books/edit/${book.id}"
                                            class="btn btn-warning btn-sm btn-action">Sửa</a>
                                        <a href="${pageContext.request.contextPath}/books/delete/${book.id}"
                                            class="btn btn-danger btn-sm btn-action"
                                            onclick="return confirm('Bạn có chắc chắn muốn xóa sách này không?')">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </body>

            </html>