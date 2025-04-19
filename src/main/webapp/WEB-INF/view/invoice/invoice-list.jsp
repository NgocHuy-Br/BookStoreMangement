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
                            <a href="${pageContext.request.contextPath}/invoice/create" class="btn btn-success">
                                ➕ Tạo đơn bán hàng mới
                            </a>
                        </div>

                        <form method="get" class="mb-3">
                            <div class="row justify-content-center">
                                <div class="col-md-4">
                                    <input type="text" name="customer" class="form-control"
                                        placeholder="🔍 Tìm theo tên khách hàng..." value="${customer}">
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

                        <h3 class="row justify-content-center">📋 Danh sách bán hàng</h3>

                        <table class="table table-bordered table-hover">
                            <thead class="table-secondary">
                                <tr>
                                    <th style="width: 5%;">STT</th>
                                    <th style="width: 12%;">Mã đơn hàng</th>
                                    <th style="width: 18%;">Khách hàng</th>
                                    <th style="width: 18%;">Thời gian tạo</th>
                                    <th style="width: 15%;">Người tạo</th>
                                    <th style="width: 15%;">Tổng thành tiền</th>
                                    <th style="width: 15%;">Xem đơn hàng</th>
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
                                            <fmt:formatNumber value="${invoiceTotals[inv.id]}" type="currency"
                                                currencySymbol="" groupingUsed="true" maxFractionDigits="0" />
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
                            Tổng cộng:
                            <fmt:formatNumber value="${totalValue}" type="currency" currencySymbol=""
                                groupingUsed="true" maxFractionDigits="0" /> đ
                        </div>
                    </div>
                </body>

                </html>