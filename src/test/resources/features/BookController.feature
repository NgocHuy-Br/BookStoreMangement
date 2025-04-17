Feature: Quản lý sách
  Là 1 quản trị viên
  I muốn quản lý thông tin sách trong hệ thống
  Qua đó tôi có thể cập nhật danh mục sách và theo dõi tồn kho

  Background:
    Given quản trị viên đã đăng nhập thành công
    And đang ở trang quản lý sách
    And Đã tồn tại <danh mục sách> trong hệ thống
    |danh mục sách|
    |trinh thám   |
    |sách giáo khoa|

  Scenario: Thêm sách mới thành công
    When quản trị viên chọn nút "Thêm sách mới"
    Then Trang "Thêm sách mới" sẽ hiển thị
    When quản trị viên nhập thông tin của sách mới gồm danh mục="trinh thám", tên sách ="truyện thám hiểm",tác giả="nguyễn văn A", giá sách="90,000"
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Thêm sách thành công"
    And danh sách sách được cập nhật

  Scenario: Cập nhật thông tin sách thành công
    Given Hệ thống có sách tên sách ="truyện thám hiểm"
    When quản trị viên cập nhật thông tin tên sách= "truyện thám hiểm phiên bản 2"
    And nhấn nút "Lưu"
    Then hệ thống sẽ hiển thị thông báo "Cập nhật thành công"
    And tên sách= "truyện thám hiểm phiên bản 2" được hiển thị trên danh sách sách

  Scenario: Xóa sách thành công
    Given có sách với tên sách= "truyện thám hiểm phiên bản 2"
    When quản trị viên nhấn nút "Xóa"
    And xác nhận xóa
    Then hệ thống sẽ hiển thị thông báo "Xóa sách thành công"
    And danh sách sách được cập nhật, tên sách= "truyện thám hiểm phiên bản 2" không còn hiển thị trên danh sách

  Scenario: Xóa sách thất bại do có đơn hàng liên quan
    Given có sách với tên sách= "truyện thám hiểm phiên bản 2" đã có đơn hàng
    When quản trị viên nhấn nút "Xóa"
    Then hệ thống sẽ hiển thị thông báo "Không thể xóa sách vì đã có đơn hàng liên quan"
    And vẫn ở lại trang quản lý sách

  Scenario: Tìm kiếm sách
    When quản trị viên nhập từ khóa tìm kiếm "phiên bản"
    And nhấn nút "Tìm kiếm"
    Then hệ thống sẽ hiển thị danh sách sách phù hợp với từ khóa "phiên bản"

  Scenario: Truy cập trang quản lý sách với quyền nhân viên
    Given người dùng đã đăng nhập với vai trò nhân viên
    When người dùng truy cập trang quản lý sách
    Then Người dùng chỉ được xem, thêm, không được sửa, xóa 