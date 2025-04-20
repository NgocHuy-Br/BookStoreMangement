<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

                    <html>

                    <head>
                        <meta charset="UTF-8">
                        <title>Danh sách danh mục</title>
                        <link rel="stylesheet"
                            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
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
                            <div class="row justify-content-center mb-4">
                                <div class="col-md-8">
                                    <form:form method="post" action="/category/create" modelAttribute="category">
                                        <c:if test="${not empty returnUrl}">
                                            <input type="hidden" name="returnUrl" value="${returnUrl}" />
                                        </c:if>

                                        <div class="d-flex justify-content-center align-items-center gap-3 mb-4">
                                            <form:input path="name" placeholder="Nhập tên danh mục thêm mới"
                                                class="form-control w-50" required="required" />
                                            <button type="submit" class="btn btn-success">➕ Thêm danh mục</button>
                                            <a href="${returnUrl}" class="btn btn-secondary">⬅ Quay lại</a>
                                        </div>

                                    </form:form>
                                </div>
                            </div>

                            <!-- THÔNG BÁO -->
                            <c:if test="${not empty error}">
                                <div class="alert-container">
                                    <div class="alert alert-danger custom-alert">${error}</div>
                                </div>
                            </c:if>

                            <c:if test="${not empty success}">
                                <div class="alert-container">
                                    <div class="alert alert-success custom-alert">${success}</div>
                                </div>
                            </c:if>

                            <!-- Tiêu đề -->
                            <div class="row justify-content-center mb-3">
                                <div class="col-md-8 text-center">
                                    <h3 class="text-center mb-4"><i class="bi bi-book"></i> 📋 Danh mục hiện tại</h3>
                                </div>
                            </div>

                            <!-- Bảng danh sách -->
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

                                                        <!-- Sửa -->
                                                        <c:url var="editUrl" value="/category/edit/${cate.id}">
                                                            <c:if test="${not empty returnUrl}">
                                                                <c:param name="returnUrl" value="${returnUrl}" />
                                                            </c:if>
                                                        </c:url>
                                                        <a href="${editUrl}" class="btn btn-warning btn-sm">✏️ Sửa</a>

                                                        <!-- Xóa -->
                                                        <c:url var="deleteUrl" value="/category/delete/${cate.id}">
                                                            <c:if test="${not empty returnUrl}">
                                                                <c:param name="returnUrl" value="${returnUrl}" />
                                                            </c:if>
                                                        </c:url>
                                                        <a href="${deleteUrl}" class="btn btn-danger btn-sm"
                                                            onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">🗑️
                                                            Xóa</a>
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