<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Danh sách danh mục</title>
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                </head>

                <body>
                    <div class="container mt-5">

                        <!-- Form thêm danh mục mới -->
                        <div class="row justify-content-center mb-4">
                            <div class="col-md-8">
                                <form:form method="post" action="/category/create" modelAttribute="category">
                                    <div class="d-flex gap-2">
                                        <form:input path="name" placeholder="Nhập tên danh mục" class="form-control"
                                            required="required" />
                                        <button type="submit" class="btn btn-success">
                                            ➕ Thêm danh mục
                                        </button>
                                    </div>
                                </form:form>
                            </div>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <c:if test="${not empty success}">
                            <div class="alert alert-success mt-3">${success}</div>
                        </c:if>

                        <!-- Tiêu đề danh sách -->
                        <div class="row justify-content-center mb-3">
                            <div class="col-md-8 text-center">
                                <h5 class="text-primary mt-3"><i class="bi bi-folder"></i> Danh mục hiện tại</h5>
                            </div>
                        </div>

                        <!-- Bảng danh sách danh mục -->
                        <div class="row justify-content-center">
                            <div class="col-md-8">
                                <table class="table table-bordered table-hover">
                                    <thead class="table-secondary text-center">
                                        <tr>
                                            <th>STT</th>
                                            <th>Tên danh mục</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="cate" items="${categories}" varStatus="loop">
                                            <tr>
                                                <td class="text-center">${loop.index + 1}</td>
                                                <td>${cate.name}</td>
                                                <td class="text-center">
                                                    <a href="/category/edit/${cate.id}"
                                                        class="btn btn-warning btn-sm">Sửa</a>
                                                    <a href="/category/delete/${cate.id}?returnUrl=${returnUrl}"
                                                        class="btn btn-danger btn-sm"
                                                        onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">Xóa</a>

                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </body>

                </html>