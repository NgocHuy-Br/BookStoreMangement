<%@ include file="admin-header.jsp" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <div class="container mt-4">
            <h4>Chỉnh sửa thông tin nhân viên</h4>
            <form:form method="post" action="/admin/employee/edit" modelAttribute="employee">
                <form:hidden path="id" />
                <div class="mb-3">
                    <label class="form-label">Tên đăng nhập:</label>
                    <form:input path="username" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Email:</label>
                    <form:input type="email" path="email" class="form-control" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Số điện thoại:</label>
                    <form:input path="phone" class="form-control" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Địa chỉ:</label>
                    <form:input path="address" class="form-control" />
                </div>
                <button type="submit" class="btn btn-success">Cập nhật</button>
                <a href="/admin/employee" class="btn btn-secondary">Hủy</a>
            </form:form>
        </div>