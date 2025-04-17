Feature: Quản lý nhân viên
  As a quản trị viên
  I want to quản lý thông tin nhân viên trong hệ thống
  So that tôi có thể phân quyền và quản lý nhân sự hiệu quả

  Background:
    Given quản trị viên đã đăng nhập thành công
    And đang ở trang quản lý nhân viên

  Scenario: Thêm nhân viên mới thành công
    When quản trị viên nhập thông tin nhân viên mới:
      | Họ và tên | Tên đăng nhập | Mật khẩu | Email | Số điện thoại | Địa chỉ | Vai trò | Trạng thái | Ghi chú |
      | Nguyễn Văn A | nva | 123456 | nva@gmail.com | 0987654321 | 123 Đường ABC, Quận 1, TP.HCM | Nhân viên bán hàng | Đang làm việc | Nhân viên mới |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Thêm nhân viên thành công"
    And danh sách nhân viên được cập nhật

  Scenario: Thêm nhân viên thất bại do tên đăng nhập đã tồn tại
    Given đã tồn tại tài khoản với tên đăng nhập "nva"
    When quản trị viên nhập thông tin nhân viên mới:
      | Họ và tên | Tên đăng nhập | Mật khẩu | Email | Số điện thoại | Địa chỉ | Vai trò | Trạng thái | Ghi chú |
      | Nguyễn Văn B | nva | 123456 | nvb@gmail.com | 0987654322 | 456 Đường XYZ, Quận 2, TP.HCM | Nhân viên bán hàng | Đang làm việc | Nhân viên mới |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Tên đăng nhập đã tồn tại"
    And vẫn ở lại trang thêm nhân viên

  Scenario: Cập nhật thông tin nhân viên thành công
    Given có nhân viên với ID "1"
    When quản trị viên cập nhật thông tin nhân viên:
      | Họ và tên | Email | Số điện thoại | Địa chỉ | Vai trò | Trạng thái | Ghi chú |
      | Nguyễn Văn A | nva_new@gmail.com | 0987654321 | 789 Đường DEF, Quận 3, TP.HCM | Quản lý | Đang làm việc | Đã thăng chức |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Cập nhật thành công"
    And danh sách nhân viên được cập nhật

  Scenario: Xóa nhân viên thành công
    Given có nhân viên với ID "1"
    When quản trị viên nhấn nút "Xóa"
    And xác nhận xóa
    Then hệ thống sẽ hiển thị thông báo "Xóa nhân viên thành công"
    And danh sách nhân viên được cập nhật

  Scenario: Tìm kiếm nhân viên
    When quản trị viên nhập từ khóa tìm kiếm "nguyen"
    And nhấn nút "Tìm kiếm"
    Then hệ thống sẽ hiển thị danh sách nhân viên phù hợp với từ khóa

  Scenario: Phân quyền cho nhân viên
    Given có nhân viên với ID "1"
    When quản trị viên cập nhật quyền hạn:
      | Quản lý sách | Quản lý đơn hàng | Quản lý khách hàng | Quản lý nhân viên |
      | Có | Có | Có | Không |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Cập nhật quyền hạn thành công"
    And danh sách quyền hạn của nhân viên được cập nhật

  Scenario: Truy cập trang quản lý nhân viên không có quyền
    Given người dùng đã đăng nhập với vai trò nhân viên
    When người dùng truy cập trang quản lý nhân viên
    Then hệ thống sẽ chuyển hướng đến trang đăng nhập 