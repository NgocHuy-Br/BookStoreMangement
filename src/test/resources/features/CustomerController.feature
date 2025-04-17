Feature: Quản lý khách hàng
  Là 1 quản trị viên
  Tôi muốn quản lý thông tin khách hàng
  Qua đó tôi có thể theo dõi và chăm sóc khách hàng tốt hơn

  Background:
    Given quản trị viên đã đăng nhập thành công
    And đang ở trang quản lý khách hàng

  Scenario: Thêm khách hàng mới thành công
    When quản trị viên nhập thông tin khách hàng mới:
      | Họ và tên | Số điện thoại | Email | Địa chỉ | Loại khách hàng | Ghi chú |
      | Nguyễn Văn A | 0987654321 | nva@gmail.com | 123 Đường ABC, Quận 1, TP.HCM | Thường xuyên | Khách hàng VIP |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Thêm khách hàng thành công"
    And danh sách khách hàng được cập nhật

  Scenario: Cập nhật thông tin khách hàng thành công
    Given có khách hàng với ID "1"
    When quản trị viên cập nhật thông tin khách hàng:
      | Họ và tên | Số điện thoại | Email | Địa chỉ | Loại khách hàng | Ghi chú |
      | Nguyễn Văn A | 0987654321 | nva@gmail.com | 456 Đường XYZ, Quận 2, TP.HCM | VIP | Khách hàng thân thiết |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Cập nhật thành công"
    And danh sách khách hàng được cập nhật

  Scenario: Xem lịch sử mua hàng của khách hàng
    Given có khách hàng với ID "1"
    When quản trị viên nhấn nút "Xem lịch sử mua hàng"
    Then hệ thống sẽ hiển thị danh sách các đơn hàng của khách hàng
    And hiển thị tổng số tiền đã mua

  Scenario: Tìm kiếm khách hàng
    When quản trị viên nhập từ khóa tìm kiếm "nguyen"
    And nhấn nút "Tìm kiếm"
    Then hệ thống sẽ hiển thị danh sách khách hàng phù hợp với từ khóa

  Scenario: Gửi thông báo khuyến mãi cho khách hàng
    Given có danh sách khách hàng thân thiết
    When quản trị viên chọn gửi thông báo khuyến mãi
    And nhập nội dung thông báo "Chương trình khuyến mãi tháng 3: Giảm 20% cho đơn hàng từ 500.000đ"
    And nhấn nút "Gửi"
    Then hệ thống sẽ hiển thị thông báo "Gửi thông báo thành công"
    And danh sách khách hàng đã nhận thông báo được cập nhật

  Scenario: Truy cập trang quản lý khách hàng không có quyền
    Given người dùng đã đăng nhập với vai trò nhân viên
    When người dùng truy cập trang quản lý khách hàng
    Then hệ thống sẽ chuyển hướng đến trang đăng nhập 