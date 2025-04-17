Feature: Quản lý đơn nhập hàng
  Là 1 quản trị viên
  Tôi muốn quản lý đơn nhập hàng
  Qua đó tôi có thể theo dõi và quản lý việc nhập hàng vào kho

  Background:
    Given quản trị viên đã đăng nhập thành công
    And đang ở trang quản lý đơn nhập hàng

  Scenario: Tạo đơn nhập hàng mới thành công
    When quản trị viên chọn nhà cung cấp "Nhà sách Phương Nam"
    And thêm sách vào đơn hàng với thông tin:
      | Tên sách | Tác giả | Thể loại | Số lượng | Đơn giá |
      | Nhà Giả Kim | Paulo Coelho | Tiểu thuyết | 50 | 100000 |
      | Đắc Nhân Tâm | Dale Carnegie | Kỹ năng sống | 30 | 150000 |
    And nhập thông tin đơn hàng:
      | Ngày nhập | Ghi chú | Thuế VAT |
      | 2024-03-15 | Nhập hàng quý 1 | 10% |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Tạo đơn nhập hàng thành công"
    And danh sách đơn nhập hàng được cập nhật
    And tổng tiền đơn hàng được tính toán đúng

  Scenario: Xem chi tiết đơn nhập hàng
    Given có đơn nhập hàng với ID "1"
    When quản trị viên nhấn nút "Xem chi tiết"
    Then hệ thống sẽ hiển thị thông tin chi tiết đơn hàng
    And hiển thị danh sách sách trong đơn hàng
    And hiển thị tổng tiền

  Scenario: In phiếu nhập hàng
    Given có đơn nhập hàng với ID "1"
    When quản trị viên nhấn nút "In phiếu"
    Then hệ thống sẽ tạo file PDF chứa thông tin đơn hàng
    And hiển thị thông báo "In phiếu thành công"

  Scenario: Tìm kiếm đơn nhập hàng
    When quản trị viên nhập từ khóa tìm kiếm "NCC001"
    And nhấn nút "Tìm kiếm"
    Then hệ thống sẽ hiển thị danh sách đơn hàng phù hợp với từ khóa

  Scenario: Tạo đơn nhập hàng thất bại do số lượng không hợp lệ
    When quản trị viên chọn nhà cung cấp "Nhà sách Phương Nam"
    And thêm sách vào đơn hàng với thông tin:
      | Tên sách | Tác giả | Thể loại | Số lượng | Đơn giá |
      | Nhà Giả Kim | Paulo Coelho | Tiểu thuyết | 0 | 100000 |
    And nhập thông tin đơn hàng:
      | Ngày nhập | Ghi chú | Thuế VAT |
      | 2024-03-15 | Nhập hàng quý 1 | 10% |
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Số lượng phải lớn hơn 0"
    And vẫn ở lại trang tạo đơn hàng

  Scenario: Truy cập trang quản lý đơn nhập hàng không có quyền
    Given người dùng đã đăng nhập với vai trò nhân viên
    When người dùng truy cập trang quản lý đơn nhập hàng
    Then hệ thống sẽ chuyển hướng đến trang đăng nhập 