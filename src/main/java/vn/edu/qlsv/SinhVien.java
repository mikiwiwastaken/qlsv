package vn.edu.qlsv;

public class SinhVien {
    private final String maSV, hoTen, ngaySinh, gioiTinh, email, soDienThoai, maLop, tenLop;

    public SinhVien(String maSV, String hoTen, String ngaySinh, String gioiTinh,
            String email, String soDienThoai, String maLop, String tenLop) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.maLop = maLop;
        this.tenLop = tenLop;
    }

    public String getMaSV() {
        return maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getEmail() {
        return email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getMaLop() {
        return maLop;
    }

    public String getTenLop() {
        return tenLop;
    }
}
