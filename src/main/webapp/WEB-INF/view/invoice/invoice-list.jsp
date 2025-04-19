<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>

                <html>

                <head>
                    <title>Danh sách hóa đơn</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                        rel="stylesheet" />
                    <style>
                        .table th,
                        .table td {
                            vertical-align: middle;
                            text-align: center;
                        }

                        .total-section {
                            font-weight: bold;
                            font-size: 18px;
                            margin-top: 20px;
                        }
                    </style>
                </head>

                <body>
                    <div class="container mt-4">
                        <div class="text-center mb-4">
                            <a href="${pageContext.request.contextPath}/invoice/create" class="btn btn-success">
                                ➕ Tạo hóa đơn mới
                            </a>
                        </div>

                        <!-- Form lọc -->
                        <form method="get" class="mb-3">
                            <div class="row g-2 align-items-center">
                                <div class="col-md-4">
                                    <input type="text" name="customer" class="form-control"
                                        placeholder="Tìm theo tên khách hàng..." value="${customer}">
                                </div>
                                <div class="col-md-2">
                                    <input type="date" name="from" class="form-control" value="${from}">
                                </div>
                                <div class="col-auto">Đến</div>
                                <div class="col-md-2">
                                    <input type="date" name="to" class="form-control" value="${to}">
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-primary">🔍 Tìm kiếm</button>
                                </div>
                            </div>
                        </form>

                        <h5 class="mb-3">📋 Danh sách hóa đơn</h5>

                        <table class="table table-bordered table-hover">
                            <thead class="table-secondary">
                                <tr>
                                    <th>STT</th>
                                    <th>Mã</th>
                                    <th>Khách hàng</th>
                                    <th>Ngày tạo</th>
                                    <th>Người tạo</th>
                                    <th>Tổng tiền (sau VAT)</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="inv" items="${invoices}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td>#${inv.id}</td>
                                        <td>${inv.customer.name}</td>
                                        <td>
                                            <fmt:formatDate value="${inv.createdAtAsDate}"
                                                pattern="dd/MM/yyyy HH:mm:ss" />

                                        </td>
                                        <td>${inv.user.username}</td>
                                        <td>
                                            <fmt:formatNumber value="${invoiceTotals[inv.id]}" type="number"
                                                groupingUsed="true" />
                                        </td>

                                        <td>
                                            <a href="${pageContext.request.contextPath}/invoice/pdf/${inv.id}"
                                                class="btn btn-outline-dark btn-sm">Xem chi tiết</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="text-end total-section">
                            Tổng giá trị các hóa đơn hiển thị:
                            <fmt:formatNumber value="${totalValue}" type="number" groupingUsed="true" /> VND
                        </div>
                    </div>
                </body>

                </html>