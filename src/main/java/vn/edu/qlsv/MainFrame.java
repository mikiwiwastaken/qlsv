package vn.edu.qlsv;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private final SinhVienDAO dao=new SinhVienDAO();
    private final JTextField ma=new JTextField(12), ten=new JTextField(18), ngay=new JTextField(12);
    private final JTextField email=new JTextField(18), sdt=new JTextField(13), search=new JTextField(18);
    private final JComboBox<String> gioiTinh=new JComboBox<>(new String[]{"Nam","Nu","Khac"});
    private final JComboBox<LopItem> lop=new JComboBox<>();
    private final DefaultTableModel model=new DefaultTableModel(
        new String[]{"Mã SV","Họ tên","Ngày sinh","Giới tính","Email","SĐT","Mã lớp","Tên lớp"},0) {
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);

    public MainFrame() {
        setTitle("PHẦN MỀM QUẢN LÝ SINH VIÊN");
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1050,620); setLocationRelativeTo(null);
        buildUI(); loadClasses(); loadStudents("");
    }
    private void buildUI() {
        JPanel form=new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        addField(form,0,0,"Mã sinh viên:",ma); addField(form,2,0,"Họ và tên:",ten);
        addField(form,0,1,"Ngày sinh (yyyy-mm-dd):",ngay); addField(form,2,1,"Giới tính:",gioiTinh);
        addField(form,0,2,"Email:",email); addField(form,2,2,"Số điện thoại:",sdt);
        addField(form,0,3,"Lớp:",lop);
        JButton add=new JButton("Thêm"), edit=new JButton("Sửa"), del=new JButton("Xóa"), clear=new JButton("Làm mới");
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(add); buttons.add(edit); buttons.add(del); buttons.add(clear);
        JPanel north=new JPanel(new BorderLayout()); north.add(form,BorderLayout.CENTER); north.add(buttons,BorderLayout.SOUTH);
        JPanel south=new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setBorder(BorderFactory.createTitledBorder("Tìm theo mã hoặc họ tên"));
        JButton find=new JButton("Tìm kiếm"), all=new JButton("Hiển thị tất cả");
        south.add(search); south.add(find); south.add(all);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); table.setAutoCreateRowSorter(true);
        setLayout(new BorderLayout(6,6)); add(north,BorderLayout.NORTH); add(new JScrollPane(table),BorderLayout.CENTER); add(south,BorderLayout.SOUTH);
        add.addActionListener(e->addStudent()); edit.addActionListener(e->editStudent());
        del.addActionListener(e->deleteStudent()); clear.addActionListener(e->clearForm());
        find.addActionListener(e->loadStudents(search.getText()));
        all.addActionListener(e->{search.setText("");loadStudents("");});
        table.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting() && table.getSelectedRow()>=0) {
                int r=table.convertRowIndexToModel(table.getSelectedRow());
                ma.setText(value(r,0)); ten.setText(value(r,1)); ngay.setText(value(r,2));
                gioiTinh.setSelectedItem(value(r,3)); email.setText(value(r,4)); sdt.setText(value(r,5));
                selectClass(value(r,6)); ma.setEditable(false);
            }
        });
    }
    private void addField(JPanel p,int x,int y,String label,JComponent field) {
        GridBagConstraints c=new GridBagConstraints(); c.insets=new Insets(5,7,5,7); c.fill=GridBagConstraints.HORIZONTAL;
        c.gridx=x;c.gridy=y;c.weightx=0;p.add(new JLabel(label),c);
        c.gridx=x+1;c.weightx=1;p.add(field,c);
    }
    private String value(int r,int c){return String.valueOf(model.getValueAt(r,c));}
    private void loadClasses() {
        lop.removeAllItems();
        try { for(String[] a:dao.getClasses()) lop.addItem(new LopItem(a[0],a[1])); }
        catch(SQLException e){error(e);}
    }
    private void loadStudents(String key) {
        model.setRowCount(0);
        try { for(SinhVien s:dao.getAll(key)) model.addRow(new Object[]{s.getMaSV(),s.getHoTen(),
            s.getNgaySinh(),s.getGioiTinh(),s.getEmail(),s.getSoDienThoai(),s.getMaLop(),s.getTenLop()}); }
        catch(SQLException e){error(e);}
    }
    private SinhVien readForm() {
        String id=ma.getText().trim(), name=ten.getText().trim(), date=ngay.getText().trim();
        String mail=email.getText().trim(); LopItem selected=(LopItem)lop.getSelectedItem();
        String phone=sdt.getText().trim();
        if(id.isEmpty()||name.isEmpty()||selected==null)
            throw new IllegalArgumentException("Vui lòng nhập mã, họ tên và chọn lớp.");
        if(id.length()>10||name.length()>100||mail.length()>100||phone.length()>15)
            throw new IllegalArgumentException("Mã sinh viên tối đa 10 ký tự, họ tên/email tối đa 100 ký tự và số điện thoại tối đa 15 ký tự.");
        if(!date.isEmpty()) {
            try {
                java.time.LocalDate.parse(date);
            } catch(java.time.format.DateTimeParseException e) {
                throw new IllegalArgumentException("Ngày sinh phải theo định dạng yyyy-mm-dd.",e);
            }
        }
        return new SinhVien(id,name,date,(String)gioiTinh.getSelectedItem(),mail,phone,selected.code,selected.name);
    }
    private void addStudent() {
        try {dao.insert(readForm()); JOptionPane.showMessageDialog(this,"Thêm sinh viên thành công!");
            loadStudents(search.getText());clearForm();}
        catch(SQLException|IllegalArgumentException e){error(e);}
    }
    private void editStudent() {
        try {if(ma.getText().trim().isEmpty()) throw new IllegalArgumentException("Hãy chọn sinh viên trong bảng.");
            dao.update(readForm());JOptionPane.showMessageDialog(this,"Cập nhật thành công!");
            loadStudents(search.getText());clearForm();}
        catch(SQLException|IllegalArgumentException e){error(e);}
    }
    private void deleteStudent() {
        String id=ma.getText().trim();
        if(id.isEmpty()){JOptionPane.showMessageDialog(this,"Hãy chọn sinh viên cần xóa.");return;}
        if(JOptionPane.showConfirmDialog(this,"Xóa sinh viên "+id+"?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            try {dao.delete(id);JOptionPane.showMessageDialog(this,"Đã xóa sinh viên.");
                loadStudents(search.getText());clearForm();}
            catch(SQLException e){error(e);}
        }
    }
    private void selectClass(String code) {
        for(int i=0;i<lop.getItemCount();i++) if(lop.getItemAt(i).code.equals(code)){lop.setSelectedIndex(i);return;}
    }
    private void clearForm() {
        ma.setText("");ten.setText("");ngay.setText("");email.setText("");sdt.setText("");
        gioiTinh.setSelectedIndex(0);if(lop.getItemCount()>0)lop.setSelectedIndex(0);
        ma.setEditable(true);table.clearSelection();ma.requestFocus();
    }
    private void error(Exception e) {
        JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
    }
    private static class LopItem {
        final String code,name;
        LopItem(String c,String n){code=c;name=n;}
        public String toString(){return code+" - "+name;}
    }
}
