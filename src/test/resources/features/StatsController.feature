Feature: Báo cáo thống kê
  Là 1 quản trị viên
  Tôi muốn xem các báo cáo thống kê về hoạt động của nhà sách
  Qua đó tôi có thể đánh giá hiệu quả kinh doanh và đưa ra quyết định

  Background:
    Given quản trị viên đã đăng nhập thành công
    And đang ở trang báo cáo thống kê

  Scenario: Xem báo cáo doanh thu theo tháng
    When quản trị viên chọn loại báo cáo "Doanh thu theo tháng"
    And chọn tháng "12/2023"
    And nhấn nút "Xem báo cáo"
    Then hệ thống sẽ hiển thị biểu đồ doanh thu theo ngày
    And hiển thị tổng doanh thu của tháng

  Scenario: Xem báo cáo sách bán chạy
    When quản trị viên chọn loại báo cáo "Sách bán chạy"
    And chọn khoảng thời gian "Tháng 12/2023"
    And nhấn nút "Xem báo cáo"
    Then hệ thống sẽ hiển thị danh sách 10 sách bán chạy nhất
    And hiển thị số lượng đã bán của từng sách

  Scenario: Xem báo cáo khách hàng thân thiết
    When quản trị viên chọn loại báo cáo "Khách hàng thân thiết"
    And chọn khoảng thời gian "Năm 2023"
    And nhấn nút "Xem báo cáo"
    Then hệ thống sẽ hiển thị danh sách 10 khách hàng mua nhiều nhất
    And hiển thị tổng số tiền đã mua của từng khách hàng

  Scenario: Xuất báo cáo ra file Excel
    When quản trị viên chọn loại báo cáo "Doanh thu theo tháng"
    And chọn tháng "12/2023"
    And nhấn nút "Xuất Excel"
    Then hệ thống sẽ tạo file Excel chứa dữ liệu báo cáo
    And hiển thị thông báo "Xuất báo cáo thành công"

  Scenario: Xem báo cáo tồn kho
    When quản trị viên chọn loại báo cáo "Tồn kho"
    And nhấn nút "Xem báo cáo"
    Then hệ thống sẽ hiển thị danh sách sách sắp hết hàng
    And hiển thị số lượng tồn kho của từng sách 