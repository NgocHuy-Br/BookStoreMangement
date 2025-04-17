Feature: Đăng nhập
  Là 1 người dùng
  Tôi muốn đăng nhập vào hệ thống
  Qua đó tôi có thể sử dụng các chức năng của hệ thống

  Background:
    Given người dùng đang ở trang đăng nhập
    And Tồn tại 1 tài khoản có <username> với pass <password> trong database với phân quyền <phân quyền>

    |username |password     | phân quyền  |
    |admin    |admin123     | admin       |
    |employee |employee123  | employee    | 

  Scenario: Đăng nhập thành công với vai trò Admin
    When người dùng nhập tên đăng nhập "admin"
    And nhập mật khẩu "admin123"
    And nhấn nút "Đăng nhập"
    Then hệ thống sẽ chuyển hướng đến trang "/dashboard"
    And hiển thị thông báo đăng nhập thành công

  Scenario: Đăng nhập thành công với vai trò Employee
    When người dùng nhập tên đăng nhập "employee"
    And nhập mật khẩu "employee123"
    And nhấn nút "Đăng nhập"
    Then hệ thống sẽ chuyển hướng đến trang "/dashboard"
    And hiển thị thông báo đăng nhập thành công

  Scenario: Đăng nhập thất bại do sai mật khẩu
    When người dùng nhập tên đăng nhập "admin"
    And nhập mật khẩu "sai_mat_khau"
    And nhấn nút "Đăng nhập"
    Then hệ thống sẽ hiển thị thông báo "Tên đăng nhập hoặc mật khẩu không đúng!"
    And vẫn ở lại trang đăng nhập

  Scenario: Đăng nhập thất bại do tài khoản không tồn tại
    When người dùng nhập tên đăng nhập "khong_ton_tai"
    And nhập mật khẩu "123456"
    And nhấn nút "Đăng nhập"
    Then hệ thống sẽ hiển thị thông báo "Tên đăng nhập hoặc mật khẩu không đúng!"
    And vẫn ở lại trang đăng nhập

  Scenario: Đăng xuất
    Given người dùng đã đăng nhập thành công
    When người dùng nhấn nút "Đăng xuất"
    Then hệ thống sẽ chuyển hướng đến trang đăng nhập
   