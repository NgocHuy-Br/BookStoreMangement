<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <title>Thêm sách mới</title>
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
                </head>

                <body>

                    <div class="container mt-5">
                        <div class="row justify-content-center">
                            <div class="col-md-4 col-16">
                                <h3 class="text-center mb-3">Thêm sách mới</h3>
                                <form:form method="post" modelAttribute="book">
                                    <div class="mb-3">
                                        <label class="form-label">Danh mục:</label>
                                        <div class="d-flex justify-content-start gap-3 align-items-center mb-3">
                                            <form:select path="category.id" class="form-select w-50">
                                                <form:options items="${categories}" itemValue="id" itemLabel="name" />
                                            </form:select>
                                            <a href="/category?returnUrl=/book/create" class="btn btn-success">➕
                                                Thêm danh mục</a>
                                        </div>

                                        <label class="form-label">Tên sách:</label>
                                        <form:input path="title" class="form-control" required="required" />
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Tác giả:</label>
                                        <form:input path="author" class="form-control" required="required" />
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Giá:</label>
                                        <form:input path="price" type="number" step="0.01" class="form-control"
                                            required="required" />
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Tồn kho:</label>
                                        <form:input path="inventory" type="number" class="form-control"
                                            readonly="true" />

                                        <div class="d-flex justify-content-between mt-4">
                                            <button type="submit" class="btn btn-success">💾 Tạo mới</button>
                                            <a href="/book" class="btn btn-secondary">⬅️ Quay lại</a>
                                        </div>
                                </form:form>
                            </div>
                        </div>
                    </div>

                </body>

                </html>