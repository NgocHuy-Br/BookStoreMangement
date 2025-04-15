<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>

                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <meta charset="UTF-8">
                    <title>Chăm sóc khách hàng</title>
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

                        .divider {
                            border-top: 2px solid #ccc;
                            margin: 40px 0;
                        }
                    </style>
                </head>

                <body>
                    <div class="container mt-5">

                        <!-- Tiêu đề -->
                        <h3 class="text-center mb-4"><i class="bi bi-bullseye"></i> Thiết lập Chăm sóc
                            Khách hàng</h3>

                        <!-- Thông báo thành công -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-info text-center">${message}</div>
                        </c:if>

                        <!-- Form cập nhật CustomerSetting -->
                        <form:form method="post" modelAttribute="customerSetting" action="/customer/setting/save">
                            <div class="mb-3">
                                <label class="form-label">Phần trăm giảm giá cho thành viên (%):</label>
                                <form:input path="discountRate" class="form-control" type="number" step="0.1"
                                    required="required" />
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Số điểm cần thiết để trở thành thành
                                    viên:</label>
                                <form:input path="requiredPointsForMembership" class="form-control" type="number"
                                    required="required" />
                            </div>
                            <button type="submit" class="btn btn-success">💾 Lưu</button>
                        </form:form>

                        <!-- Gạch phân cách -->
                        <div class="divider"></div>

                        <!-- Danh sách khách hàng -->
                        <h3 class="text-center mb-4"><i class="bi bi-people"></i> Danh sách khách hàng
                        </h3>

                        <!-- Form tìm kiếm và nút thêm khách hàng -->
                        <div class="row g-3 justify-content-between align-items-center mb-3">
                            <form class="col-md-6 col-12 d-flex" method="get"
                                action="${pageContext.request.contextPath}/customer">
                                <input type="text" class="form-control me-2" name="keyword"
                                    placeholder="🔍 Tìm theo tên khách hàng..." value="${keyword}">
                                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                            </form>

                            <div class="col-md-auto text-end">
                                <a href="${pageContext.request.contextPath}/customer/create" class="btn btn-success">
                                    ➕ Thêm khách hàng mới
                                </a>
                            </div>
                        </div>
                        <c:if test="${not empty deleteError}">
                            <div class="alert alert-danger text-center">
                                ${deleteError}
                            </div>
                        </c:if>


                        <table class="table table-bordered table-hover">
                            <thead class="table-secondary">
                                <tr>
                                    <th>STT</th>
                                    <th>Tên</th>
                                    <th>Email</th>
                                    <th>SĐT</th>
                                    <th>Điểm tích lũy</th>
                                    <th>Thành viên</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="customer" items="${customers}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td>${customer.name}</td>
                                        <td>${customer.email}</td>
                                        <td>${customer.phone}</td>
                                        <td>${customer.loyaltyPoints}</td>
                                        <td>
                                            <c:choose>
                                                <c:when
                                                    test="${customer.loyaltyPoints >= customerSetting.requiredPointsForMembership}">
                                                    ✅ Thành viên
                                                </c:when>
                                                <c:otherwise>
                                                    ❌ Chưa đủ
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="/customer/delete/${customer.id}" class="btn btn-danger btn-sm"
                                                onclick="return confirm('Bạn có chắc muốn xóa khách hàng này?')">
                                                Xóa
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </body>

                </html>