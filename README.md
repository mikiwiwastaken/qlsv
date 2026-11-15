# Quản lý sinh viên - Java Swing + MariaDB

## Chức năng

- Hiển thị danh sách sinh viên.
- Thêm, sửa, xóa sinh viên.
- Tìm kiếm theo mã sinh viên hoặc họ tên.
- Chọn lớp từ dữ liệu trong MariaDB.

## Chuẩn bị

Cài JDK 21, MariaDB, Maven và DBeaver.

## Tạo cơ sở dữ liệu

Trong DBeaver, kết nối MariaDB rồi mở và chạy file:
`src/main/resources/qlsv.sql`

## Cấu hình kết nối

Mặc định ứng dụng kết nối `localhost:3306`, database `qlsv`, tài khoản `root`
và mật khẩu trống. Có thể cấu hình qua biến môi trường `QLSV_DB_URL`,
`QLSV_DB_USER`, `QLSV_DB_PASSWORD` hoặc system properties tương ứng
`qlsv.db.url`, `qlsv.db.user`, `qlsv.db.password`.

## Chạy

Mở terminal tại thư mục dự án:

```bash
mvn clean compile
```

Sau khi Maven tải thư viện, chạy lớp `vn.edu.qlsv.Main`.

## Sử dụng

- Thêm: nhập thông tin rồi bấm **Thêm**.
- Sửa: chọn một dòng trong bảng, sửa thông tin rồi bấm **Sửa**.
- Xóa: chọn một dòng rồi bấm **Xóa**.
- Tìm kiếm: nhập mã hoặc tên, bấm **Tìm kiếm**.
- Hiển thị tất cả: bấm **Hiển thị tất cả**.
- Ngày sinh (nếu nhập) theo định dạng `yyyy-mm-dd`, ví dụ `2005-03-12`.
- Ngày sinh, email và số điện thoại được phép để trống theo cấu trúc database.

> [!NOTE]
> Không thể xóa sinh viên đã có bản ghi điểm do khóa ngoại đang dùng `ON DELETE RESTRICT`.
