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
                </head>

                <body>
                    <div class="container mt-5">

                        <!-- Tiêu đề -->
                        <h3 class="text-center mb-4"><i class="bi bi-bullseye"></i> Thiết lập giảm giá cho
                            khách hàng thành viên</h3>

                        <c:if test="${not empty message}">
                            <div class="alert-container">
                                <div class="alert alert-success custom-alert">
                                    ${message}
                                </div>
                            </div>
                        </c:if>

                        <div class="row justify-content-center mb-4">
                            <div class="col-md-12">
                                <form:form method="post" modelAttribute="customerSetting"
                                    action="/customer/setting/save">
                                    <form:hidden path="id" />

                                    <!-- Input: Giảm giá -->
                                    <div class="row justify-content-center mb-3">
                                        <label class="col-sm-5 col-form-label text-end fw-bold">
                                            Phần trăm giảm giá cho thành viên (%):
                                        </label>

                                        <div class="col-sm-2">
                                            <form:input path="discountRate" type="number" step="0.1" min="0"
                                                class="form-control text-center"
                                                readonly="${sessionScope.loggedInUser.role == 'EMPLOYEE'}" />
                                        </div>
                                    </div>

                                    <!-- Input: Số điểm để là thành viên -->
                                    <div class="row justify-content-center">
                                        <label class="col-sm-5 col-form-label text-end fw-bold">
                                            Số điểm để là thành viên (1,000đ tương đương 1 điểm):
                                        </label>

                                        <div class="col-sm-2">
                                            <form:input path="requiredPointsForMembership" type="number" step="1"
                                                min="1" class="form-control text-center"
                                                readonly="${sessionScope.loggedInUser.role == 'EMPLOYEE'}" />
                                        </div>
                                    </div>

                                    <div class="text-center">
                                        <button type="submit" class="btn btn-success">💾 Lưu cài đặt</button>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                        <!-- Gạch phân cách -->
                        <div class="divider"></div>

                        <!-- Danh sách khách hàng -->
                        <h3 class="text-center mb-4"><i class="bi bi-people"></i> Danh sách khách hàng
                        </h3>

                        <c:if test="${not empty customerMessage}">
                            <div class="alert-container">
                                <div class="alert alert-info custom-alert">
                                    ${customerMessage}
                                </div>
                            </div>
                        </c:if>

                        <!-- Form tìm kiếm và nút thêm khách hàng -->
                        <div class="row g-3 justify-content-between align-items-center mb-3">
                            <form class="col-md-6 col-12 d-flex" method="get"
                                action="${pageContext.request.contextPath}/customer">
                                <input type="text" class="form-control me-2" name="keyword"
                                    placeholder="🔍 Tìm theo tên khách hàng..." value="${keyword}"
                                    style="width: 300px;" />

                                <button type="submit" class="btn btn-primary" style="white-space: nowrap;">Tìm
                                    kiếm</button>

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
                                    <th style="width: 5%;">STT</th>
                                    <th style="width: 20%;">Tên</th>
                                    <th style="width: 20%;">Email</th>
                                    <th style="width: 15%;">Số điện thoại</th>
                                    <th style="width: 10%;">Điểm tích lũy</th>
                                    <th style="width: 15%;">Thành viên</th>
                                    <th style="width: 15%;">Hành động</th>

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

                                            <a href="/customer/update/${customer.id}" class="btn btn-warning btn-sm">
                                                ✏️Sửa
                                            </a>

                                            <a href="/customer/delete/${customer.id}" class="btn btn-danger btn-sm"
                                                onclick="return confirm('Bạn có chắc muốn xóa khách hàng này?')">
                                                🗑️ Xóa
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </body>

                </html>