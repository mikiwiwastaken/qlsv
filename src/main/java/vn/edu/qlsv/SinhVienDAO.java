package vn.edu.qlsv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {
    public List<SinhVien> getAll(String keyword) throws SQLException {
        String sql = "SELECT sv.MaSV,sv.HoTen,sv.NgaySinh,sv.GioiTinh,sv.Email,sv.SoDienThoai,sv.MaLop,l.TenLop "
                   + "FROM SINHVIEN sv JOIN LOP l ON sv.MaLop=l.MaLop "
                   + "WHERE sv.MaSV LIKE ? OR sv.HoTen LIKE ? ORDER BY sv.MaSV";
        List<SinhVien> list = new ArrayList<>();
        String key = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        try (Connection cn=DatabaseConnection.getConnection(); PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setString(1,key); ps.setString(2,key);
            try(ResultSet rs=ps.executeQuery()) {
                while(rs.next()) {
                    java.sql.Date d=rs.getDate("NgaySinh");
                    list.add(new SinhVien(rs.getString("MaSV"),rs.getString("HoTen"),
                        d==null?"":d.toString(),rs.getString("GioiTinh"),rs.getString("Email"),
                        rs.getString("SoDienThoai"),rs.getString("MaLop"),rs.getString("TenLop")));
                }
            }
        }
        return list;
    }
    public void insert(SinhVien s) throws SQLException {
        String sql="INSERT INTO SINHVIEN(MaSV,HoTen,NgaySinh,GioiTinh,Email,SoDienThoai,MaLop) VALUES(?,?,?,?,?,?,?)";
        try(Connection cn=DatabaseConnection.getConnection(); PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setString(1,s.getMaSV()); ps.setString(2,s.getHoTen()); setDate(ps,3,s.getNgaySinh());
            ps.setString(4,s.getGioiTinh()); setNullableString(ps,5,s.getEmail());
            setNullableString(ps,6,s.getSoDienThoai());
            ps.setString(7,s.getMaLop()); ps.executeUpdate();
        }
    }
    public void update(SinhVien s) throws SQLException {
        String sql="UPDATE SINHVIEN SET HoTen=?,NgaySinh=?,GioiTinh=?,Email=?,SoDienThoai=?,MaLop=? WHERE MaSV=?";
        try(Connection cn=DatabaseConnection.getConnection(); PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setString(1,s.getHoTen()); setDate(ps,2,s.getNgaySinh());
            ps.setString(3,s.getGioiTinh()); setNullableString(ps,4,s.getEmail());
            setNullableString(ps,5,s.getSoDienThoai());
            ps.setString(6,s.getMaLop()); ps.setString(7,s.getMaSV());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Không tìm thấy sinh viên để cập nhật.");
            }
        }
    }
    public void delete(String ma) throws SQLException {
        try(Connection cn=DatabaseConnection.getConnection();
            PreparedStatement ps=cn.prepareStatement("DELETE FROM SINHVIEN WHERE MaSV=?")) {
            ps.setString(1,ma);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Không tìm thấy sinh viên để xóa.");
            }
        }
    }

    private static void setDate(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, java.sql.Date.valueOf(LocalDate.parse(value.trim())));
        }
    }

    private static void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }

    public List<String[]> getClasses() throws SQLException {
        List<String[]> out=new ArrayList<>();
        try(Connection cn=DatabaseConnection.getConnection();
            PreparedStatement ps=cn.prepareStatement("SELECT MaLop,TenLop FROM LOP ORDER BY MaLop");
            ResultSet rs=ps.executeQuery()) {
            while(rs.next()) out.add(new String[]{rs.getString(1),rs.getString(2)});
        }
        return out;
    }
}
