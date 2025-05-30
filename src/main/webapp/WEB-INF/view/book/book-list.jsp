<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <meta charset="UTF-8">
                    <title>Quản lý sách</title>
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
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
                            margin-top: 20px;
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
                        <div class="text-center mb-3">
                            <a href="${pageContext.request.contextPath}/book/create" class="btn btn-success">
                                <i class="bi bi-plus-circle"></i> ➕ Thêm sách mới
                            </a>
                        </div>

                        <div class="d-flex justify-content-center mb-3">
                            <form class="d-flex" method="get" action="${pageContext.request.contextPath}/book">
                                <input type="text" class="form-control me-2" name="keyword" style="width: 400px;"
                                    placeholder="🔍 Tìm theo tên sách, tác giả, danh mục" value="${keyword}">
                                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                            </form>
                        </div>
                        <!-- Form Top bán chạy -->
                        <form method="get" class=" row mb-3" action="${pageContext.request.contextPath}/book">
                            <div class="text-center mb-1">
                                <a href="${pageContext.request.contextPath}/book?top=10"
                                    class="btn btn-outline-primary">
                                    📈 Thống kê top 10 sách bán chạy
                                </a>
                            </div>
                            <div class="text-center mb-1">
                                <a href="/book/inventory-asc" class="btn btn-outline-primary mt-2">
                                    🗂️ Lọc sách theo tồn kho tăng dần
                                </a>
                            </div>
                        </form>

                        <h3 class="text-center mb-4"><i class="bi bi-book"></i>📋 Danh mục sách</h3>

                        <c:if test="${not empty sessionScope.bookMessage}">
                            <div class="alert-container">
                                <div class="alert alert-success custom-alert">
                                    ${sessionScope.bookMessage}
                                </div>
                            </div>
                            <c:remove var="bookMessage" scope="session" />
                        </c:if>

                        <table class="table table-bordered table-hover">
                            <thead class="table-secondary">
                                <tr>
                                    <th style="width: 5%;">STT</th>
                                    <th style="width: 20%;">Tên sách</th>
                                    <th style="width: 8%;">Tác giả</th>
                                    <th style="width: 15%;">Danh mục</th>
                                    <th style="width: 10%;">Giá nhập trung bình</th>
                                    <th style="width: 10%;">Giá bán ra</th>
                                    <th style="width: 8%;">Tồn kho</th>
                                    <th style="width: 8%;">Đã bán ra</th>

                                    <th style="width: 18%;">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="book" items="${books}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td>${book.title}</td>
                                        <td>${book.author}</td>
                                        <td>${book.category.name}</td>
                                        <td>
                                            <fmt:formatNumber value="${book.averageImportPrice}" pattern="#,##0" />
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${book.price}" type="currency" currencySymbol=""
                                                maxFractionDigits="0" />
                                        </td>

                                        <td>${book.inventory}</td>
                                        <td>
                                            <c:out value="${book.soldQuantity}" default="0" />
                                        </td>

                                        <td>
                                            <a href="/book/edit/${book.id}" class="btn btn-warning btn-sm">✏️ Sửa</a>
                                            <a href="/book/delete/${book.id}"
                                                onclick="return confirm('Bạn có chắc chắn muốn xóa sách này?')"
                                                class="btn btn-danger btn-sm">🗑️ Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </div>
                </body>

                </html>