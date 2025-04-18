<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
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
                        action="${pageContext.request.contextPath}/book">
                        <div class="col-auto">
                            <input type="text" class="form-control" name="keyword"
                                placeholder="Tìm theo tên sách, tác giả, danh mục..." value="${keyword}">
                        </div>
                        <div class="col-auto">
                            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                        </div>
                    </form>

                    <!-- Form Top bán chạy -->
                    <form method="get" class=" row mb-3" action="${pageContext.request.contextPath}/book">
                        <div class="text-center mb-4">
                            <a href="${pageContext.request.contextPath}/book?top=2" class="btn btn-outline-primary">
                                📈 Thống kê top 2 bán chạy
                            </a>
                        </div>
                    </form>

                    <c:if test="${not empty sessionScope.bookDeleteError}">
                        <div class="alert alert-danger text-center">
                            ${sessionScope.bookDeleteError}
                        </div>
                        <c:remove var="bookDeleteError" scope="session" />
                    </c:if>

                    <div class="text-end mb-3">
                        <a href="${pageContext.request.contextPath}/book/create" class="btn btn-success">
                            <i class="bi bi-plus-circle"></i> Thêm sách mới
                        </a>
                    </div>

                    <table class="table table-bordered table-hover">
                        <thead class="table-secondary">
                            <tr>
                                <th style="width: 5%;">STT</th>
                                <th style="width: 24%;">Tên sách</th>
                                <th style="width: 12%;">Tác giả</th>
                                <th style="width: 17%;">Danh mục</th>
                                <th style="width: 12%;">Giá</th>
                                <th style="width: 9%;">Tồn kho</th>
                                <th style="width: 9%;">Đã bán ra</th>

                                <th style="width: 12%;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="book" items="${books}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.category.name}</td>
                                    <td>${book.price}</td>
                                    <td>${book.inventory}</td>
                                    <td>
                                        <c:out value="${book.soldQuantity}" default="0" />
                                    </td>

                                    <td>
                                        <a href="/book/edit/${book.id}" class="btn btn-warning btn-sm">Sửa</a>
                                        <a href="/book/delete/${book.id}"
                                            onclick="return confirm('Bạn có chắc chắn muốn xóa sách này?')"
                                            class="btn btn-danger btn-sm">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                </div>
            </body>

            </html>