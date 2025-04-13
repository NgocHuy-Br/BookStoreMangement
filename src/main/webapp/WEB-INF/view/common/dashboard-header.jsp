<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <!-- Header chính -->
    <div
      style="background-color: #f8f9fa; border-bottom: 1px solid #ddd; padding: 10px 20px; display: flex; justify-content: space-between; align-items: center;">
      <!-- Bên trái: logo và tiêu đề -->
      <a href="/admin/dashboard" style="font-size: 24px; font-weight: bold; color: #2c7be5; text-decoration: none;">
        📚 Quản trị Nhà sách
      </a>

      <!-- Bên phải: thông tin tài khoản -->
      <div style="text-align: right;">
        <div style="font-size: 16px;">👋 Xin chào, <strong>${sessionScope.loggedInUser.username}</strong></div>
        <div style="font-size: 14px;">Tài khoản:
          <span style="font-weight: bold; color: #0d6efd;">${sessionScope.loggedInUser.role}</span>
        </div>
        <div style="margin-top: 5px;">
          <a href="/auth/logout"
            style="padding: 5px 12px; background-color: #dc3545; color: white; text-decoration: none; border-radius: 5px; font-size: 14px;">
            Đăng xuất
          </a>
        </div>
      </div>
    </div>

    <!-- Dòng tab chức năng -->
    <div style="background-color: #e9ecef; padding: 12px 0; display: flex; justify-content: center; gap: 30px;">
      <a href="/admin/employee" style="text-decoration: none; font-size: 16px; color: black; padding: 5px 10px; 
              <c:if test='${activeTab eq " employee"}'>background-color: #dee2e6; font-weight: bold; border-radius:
        4px;</c:if>'">
        🎥 Quản lý nhân viên
      </a>

      <a href="/book" style="text-decoration: none; font-size: 16px; color: black; padding: 5px 10px; 
              <c:if test='${activeTab eq " book"}'>background-color: #dee2e6; font-weight: bold; border-radius: 4px;
        </c:if>'">
        📚 Quản lý sách
      </a>

      <a href="/import" style="text-decoration: none; font-size: 16px; color: black; padding: 5px 10px; 
              <c:if test='${activeTab eq " import"}'>background-color: #dee2e6; font-weight: bold; border-radius: 4px;
        </c:if>'">
        🛳️ Nhập hàng
      </a>

      <a href="/admin/invoice" style="text-decoration: none; font-size: 16px; color: black; padding: 5px 10px; 
              <c:if test='${activeTab eq " invoice"}'>background-color: #dee2e6; font-weight: bold; border-radius: 4px;
        </c:if>'">
        🧾 Hóa đơn bán hàng
      </a>

      <a href="/admin/stats" style="text-decoration: none; font-size: 16px; color: black; padding: 5px 10px; 
              <c:if test='${activeTab eq " stats"}'>background-color: #dee2e6; font-weight: bold; border-radius: 4px;
        </c:if>'">
        📈 Thống kê
      </a>

      <a href="/admin/customer" style="text-decoration: none; font-size: 16px; color: black; padding: 5px 10px; 
              <c:if test='${activeTab eq " customer"}'>background-color: #dee2e6; font-weight: bold; border-radius:
        4px;</c:if>'">
        💳 Chăm sóc khách hàng
      </a>
    </div>