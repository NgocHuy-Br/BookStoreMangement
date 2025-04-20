<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>

                <html>

                <head>
                    <title>Danh sách nhập hàng</title>
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
                            margin-top: 20px;
                            font-size: 18px;
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
                        <div class="text-center mb-5">
                            <a href="${pageContext.request.contextPath}/import/create" class="btn btn-success">
                                <i class="bi bi-plus-lg"></i> ➕ Tạo đơn nhập hàng mới
                            </a>
                        </div>

                        <form method="get" class="mb-3">
                            <div class="row justify-content-center">
                                <div class="col-md-4">
                                    <input type="text" name="supplier" class="form-control"
                                        placeholder="🔍 Tìm theo tên nhà cung cấp..." value="${supplier}">
                                </div>
                                <div class="col-md-2">
                                    <input type="date" name="from" class="form-control" value="${from}">
                                </div>
                                <div class="col-auto">Đến</div>
                                <div class="col-md-2">
                                    <input type="date" name="to" class="form-control" value="${to}">
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                                </div>
                            </div>
                        </form>

                        <h3 class="row justify-content-center">📋 Danh sách nhập hàng</h3>

                        <div class="text-end total-section mb-1">
                            Tổng giá trị nhập hàng (sau VAT):
                            <fmt:formatNumber value="${totalValue}" type="currency" currencySymbol=""
                                groupingUsed="true" maxFractionDigits="0" /> đ
                        </div>

                        <table class="table table-bordered table-hover">
                            <thead class="table-secondary">
                                <tr>
                                    <th style="width: 5%;">STT</th>
                                    <th style="width: 10%;">Mã đơn hàng</th>
                                    <th style="width: 20%;">Nhà cung cấp</th>
                                    <th style="width: 20%;">Thời gian tạo</th>
                                    <th style="width: 15%;">Người tạo</th>
                                    <th style="width: 15%;">Tổng thành tiền (sau VAT)</th>
                                    <th style="width: 25%;">Xem đơn hàng</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${orders}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td>${order.id}</td>
                                        <td>${order.supplier.name}</td>
                                        <td>
                                            <fmt:formatDate value="${order.createdAtAsDate}"
                                                pattern="dd/MM/yyyy HH:mm:ss" />
                                        </td>
                                        <td>${order.createdBy.username}</td>
                                        <td>
                                            <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                                currencySymbol="" groupingUsed="true" maxFractionDigits="0" />
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/import/pdf/${order.id}"
                                                class="btn btn-outline-dark btn-sm">
                                                Xem chi tiết
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </body>

                </html>