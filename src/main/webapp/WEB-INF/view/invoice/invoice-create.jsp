<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <html>

                <head>
                    <title>Tạo hóa đơn bán hàng</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <style>
                        .table th,
                        .table td {
                            vertical-align: middle;
                            text-align: center;
                        }

                        .book-column {
                            width: 300px;
                        }

                        .price-column {
                            width: 130px;
                        }

                        .qty-column {
                            width: 100px;
                        }

                        .stt-column {
                            width: 50px;
                        }

                        .select-customer {
                            width: 350px;
                            margin: auto;
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
                    <div class="container mt-4">
                        <h3 class="text-center mb-4">Tạo hóa đơn bán hàng</h3>

                        <c:if test="${not empty success}">
                            <div class="alert-container">
                                <div class="alert alert-success custom-alert">
                                    ${success}
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert-container">
                                <div class="alert alert-danger custom-alert">
                                    ${error}
                                </div>
                            </div>
                        </c:if>

                        <form method="post" action="/invoice/save">
                            <div class="row mb-3 justify-content-center">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Chọn khách hàng:</label>
                                <div class="col-sm-4">
                                    <select name="customerId" id="customerSelect" class="form-select select-customer"
                                        required onchange="calculateTotal()">
                                        <option value="">-- Chọn --</option>
                                        <c:forEach var="cus" items="${customers}">
                                            <option value="${cus.id}" data-points="${cus.loyaltyPoints}">
                                                ${cus.name} - ${cus.phone}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <hr>

                            <!-- Bảng sách -->
                            <table class="table table-bordered align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th class="stt-column">STT</th>
                                        <th class="book-column">Tên sách</th>
                                        <th>Tác giả</th>
                                        <th>Thể loại</th>
                                        <th class="price-column">Giá bán</th>
                                        <th class="qty-column">Số lượng</th>
                                        <th>Xóa</th>
                                    </tr>
                                </thead>
                                <tbody id="invoiceTableBody">
                                    <tr>
                                        <td class="stt">1</td>
                                        <td>
                                            <select name="bookId" class="form-select book-select"
                                                onchange="updateDetails(this)" required>
                                                <option value="">-- Chọn sách --</option>
                                                <c:forEach var="book" items="${books}">
                                                    <option value="${book.id}" data-author="${book.author}"
                                                        data-category="${book.category.name}"
                                                        data-price="${book.price}">
                                                        ${book.title}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="author">-</td>
                                        <td class="category">-</td>
                                        <td>
                                            <input type="number" name="unitPrice" min="1" class="form-control price"
                                                readonly>


                                        </td>
                                        <td><input type="number" name="quantity" min="1" value="1"
                                                class="form-control quantity" required onchange="calculateTotal()"></td>
                                        <td><button type="button" class="btn btn-danger"
                                                onclick="removeRow(this)">X</button></td>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- Tổng cộng -->
                            <div class="row mb-2">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Tổng cộng:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="totalAmount" readonly>
                                </div>
                            </div>

                            <div class="row mb-2">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Giảm giá thành viên:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="discountAmount" readonly>
                                </div>
                            </div>

                            <div class="row mb-2">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Thuế VAT (%):</label>
                                <div class="col-sm-2">
                                    <input type="number" id="vatPercent" name="vat" min="0" value="10"
                                        class="form-control" value="0" onchange="calculateTotal()" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Thành tiền:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="grandTotal" readonly>
                                </div>
                            </div>

                            <!-- Hidden -->
                            <input type="hidden" id="discountRate" value="${discountRate}" />
                            <input type="hidden" id="requiredPoints" value="${requiredPoints}" />

                            <!-- Nút -->
                            <div class="mb-3">
                                <button type="button" class="btn btn-outline-primary" onclick="addRow()">➕ Thêm
                                    dòng</button>
                                <button type="button" class="btn btn-outline-secondary" onclick="removeLastRow()">➖ Xóa
                                    dòng
                                    cuối</button>
                            </div>

                            <div class="d-flex justify-content-end">
                                <button type="submit" class="btn btn-success">✔ Tạo hóa đơn</button>
                                <c:if test="${not empty lastInvoiceId}">
                                    <a href="/invoice/pdf/${lastInvoiceId}" class="btn btn-outline-dark ms-2">⬇ Xuất
                                        PDF</a>
                                </c:if>
                                <a href="/invoice" class="btn btn-secondary ms-2">⬅️ Quay lại</a>
                            </div>
                        </form>
                    </div>

                    <script>
                        function updateDetails(select) {
                            const row = select.closest('tr');
                            const option = select.options[select.selectedIndex];
                            row.querySelector('.author').innerText = option.dataset.author || '-';
                            row.querySelector('.category').innerText = option.dataset.category || '-';
                            row.querySelector('.price').value = option.dataset.price || '0';
                            calculateTotal();
                        }

                        function addRow() {
                            const tbody = document.getElementById("invoiceTableBody");
                            const newRow = tbody.rows[0].cloneNode(true);
                            newRow.querySelectorAll("input, select").forEach(el => el.value = "");
                            newRow.querySelector('.author').innerText = "-";
                            newRow.querySelector('.category').innerText = "-";
                            tbody.appendChild(newRow);
                            updateSTT();
                        }

                        function removeRow(button) {
                            const tbody = document.getElementById("invoiceTableBody");
                            if (tbody.rows.length > 1) {
                                button.closest("tr").remove();
                                updateSTT();
                                calculateTotal();
                            }
                        }

                        function removeLastRow() {
                            const tbody = document.getElementById("invoiceTableBody");
                            if (tbody.rows.length > 1) {
                                tbody.deleteRow(tbody.rows.length - 1);
                                updateSTT();
                                calculateTotal();
                            }
                        }

                        function updateSTT() {
                            document.querySelectorAll("#invoiceTableBody .stt").forEach((cell, idx) => {
                                cell.innerText = idx + 1;
                            });
                        }

                        function calculateTotal() {
                            let total = 0;
                            document.querySelectorAll("#invoiceTableBody tr").forEach(row => {
                                const price = parseFloat(row.querySelector("input[name='unitPrice']").value) || 0;
                                const quantity = parseInt(row.querySelector("input[name='quantity']").value) || 0;
                                total += price * quantity;
                            });

                            document.getElementById("totalAmount").value = total.toFixed(0);

                            const vat = parseFloat(document.getElementById("vatPercent").value) || 0;
                            const discountRate = parseFloat(document.getElementById("discountRate").value) || 0;
                            const requiredPoints = parseInt(document.getElementById("requiredPoints").value) || 0;

                            const customerSelect = document.getElementById("customerSelect");
                            const selectedOption = customerSelect.options[customerSelect.selectedIndex];
                            const points = parseInt(selectedOption?.dataset?.points || "0");

                            let discount = 0;
                            if (points >= requiredPoints) {
                                discount = total * discountRate / 100;
                            }

                            document.getElementById("discountAmount").value = discount.toFixed(0);
                            const grandTotal = (total - discount) * (1 + vat / 100);
                            document.getElementById("grandTotal").value = grandTotal.toFixed(0);
                        }

                        document.addEventListener("DOMContentLoaded", calculateTotal);
                    </script>
                </body>

                </html>