<%@ include file="/WEB-INF/view/common/dashboard-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                <!DOCTYPE html>
                <html>

                <head>
                    <title>Tạo đơn nhập hàng</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <style>
                        .table th,
                        .table td {
                            vertical-align: middle;
                            text-align: center;
                        }

                        .book-column {
                            width: 280px;
                        }

                        .price-column {
                            width: 130px;
                        }

                        .qty-column {
                            width: 100px;
                        }
                    </style>
                </head>

                <body>
                    <div class="container mt-4">
                        <h3 class="text-center mb-4">Tạo đơn nhập hàng</h3>

                        <c:if test="${not empty success}">
                            <div class="alert alert-success text-center">${success}</div>
                        </c:if>

                        <div class="text-center mb-3">
                            <a href="/supplier/create?returnUrl=/import" class="btn btn-outline-primary mb-3">
                                <i class="bi bi-plus-circle"></i> ➕ Thêm nhà cung cấp
                            </a>
                        </div>

                        <form method="post" action="/import/save">
                            <!-- Nhà cung cấp áp dụng cho cả đơn -->
                            <div class="mb-3 row">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Chọn nhà cung cấp:</label>
                                <div class="col-sm-6">
                                    <select name="supplierId" class="form-select" required>
                                        <option value="">-- Chọn --</option>
                                        <c:forEach var="sup" items="${suppliers}">
                                            <option value="${sup.id}">${sup.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <hr>

                            <table class="table table-bordered align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>STT</th>
                                        <th class="book-column">Tên sách</th>
                                        <th>Tác giả</th>
                                        <th>Danh mục</th>
                                        <th class="price-column">Đơn giá</th>
                                        <th class="qty-column">Số lượng</th>
                                        <th>Xóa dòng</th>
                                    </tr>
                                </thead>
                                <tbody id="importTableBody">
                                    <tr>
                                        <td class="stt">1</td>
                                        <td>
                                            <select name="bookId" class="form-select book-select"
                                                onchange="updateDetails(this)" required>
                                                <option value="">-- Chọn sách --</option>
                                                <c:forEach var="book" items="${books}">
                                                    <option value="${book.id}" data-author="${book.author}"
                                                        data-category="${book.category.name}">
                                                        ${book.title}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="author">-</td>
                                        <td class="category">-</td>
                                        <td><input type="number" name="unitPrice" step="1000" class="form-control price"
                                                required onchange="calculateTotal()"></td>
                                        <td><input type="number" name="quantity" class="form-control quantity" required
                                                onchange="calculateTotal()"></td>
                                        <td><button type="button" class="btn btn-danger"
                                                onclick="removeRow(this)">X</button></td>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- Tổng cộng và VAT -->
                            <div class="row mb-2">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Tổng cộng:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="totalAmount" readonly>
                                </div>
                            </div>
                            <div class="row mb-2">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Thuế VAT (%):</label>
                                <div class="col-sm-2">
                                    <input type="number" id="vatPercent" class="form-control" value="0"
                                        onchange="calculateTotal()">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-2 col-form-label text-end fw-bold">Thành tiền:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="grandTotal" readonly>
                                </div>
                            </div>

                            <div class="mb-3">
                                <button type="button" class="btn btn-outline-primary" onclick="addRow()">➕ Thêm
                                    dòng</button>
                                <button type="button" class="btn btn-outline-secondary" onclick="removeLastRow()">➖ Xóa
                                    dòng cuối</button>
                            </div>

                            <div class="d-flex justify-content-end">
                                <a href="/" class="btn btn-secondary me-2">Hủy</a>
                                <button type="submit" class="btn btn-success">✔ Nhập hàng</button>
                            </div>
                        </form>
                    </div>

                    <script>
                        function updateDetails(select) {
                            const row = select.closest('tr');
                            const author = select.options[select.selectedIndex].dataset.author || '-';
                            const category = select.options[select.selectedIndex].dataset.category || '-';
                            row.querySelector('.author').innerText = author;
                            row.querySelector('.category').innerText = category;
                        }

                        function addRow() {
                            const tbody = document.getElementById("importTableBody");
                            const newRow = tbody.rows[0].cloneNode(true);
                            newRow.querySelectorAll("input, select").forEach(el => el.value = "");
                            newRow.querySelector('.author').innerText = "-";
                            newRow.querySelector('.category').innerText = "-";
                            tbody.appendChild(newRow);
                            updateSTT();
                        }

                        function removeRow(button) {
                            const tbody = document.getElementById("importTableBody");
                            if (tbody.rows.length > 1) {
                                button.closest("tr").remove();
                                updateSTT();
                                calculateTotal();
                            }
                        }

                        function removeLastRow() {
                            const tbody = document.getElementById("importTableBody");
                            if (tbody.rows.length > 1) {
                                tbody.deleteRow(tbody.rows.length - 1);
                                updateSTT();
                                calculateTotal();
                            }
                        }

                        function updateSTT() {
                            document.querySelectorAll("#importTableBody .stt").forEach((cell, idx) => {
                                cell.innerText = idx + 1;
                            });
                        }

                        function calculateTotal() {
                            let total = 0;
                            document.querySelectorAll("#importTableBody tr").forEach(row => {
                                const price = parseFloat(row.querySelector("input[name='unitPrice']").value) || 0;
                                const quantity = parseInt(row.querySelector("input[name='quantity']").value) || 0;
                                total += price * quantity;
                            });
                            document.getElementById("totalAmount").value = total.toFixed(0);

                            const vatPercent = parseFloat(document.getElementById("vatPercent").value) || 0;
                            const vatAmount = total * vatPercent / 100;
                            document.getElementById("grandTotal").value = (total + vatAmount).toFixed(0);
                        }
                    </script>
                </body>

                </html>