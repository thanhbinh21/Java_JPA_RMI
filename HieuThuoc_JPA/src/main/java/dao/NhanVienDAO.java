package dao;

import entity.NhanVien;

import java.util.List;
import java.util.Optional;

public interface NhanVienDAO {
    NhanVien findById(String maNhanVien);
    List<NhanVien> findAll();
    boolean save(NhanVien nhanVien);
    boolean update(NhanVien nhanVien);
    boolean delete(String maNhanVien);
    Optional<NhanVien> findByTen(String ten);
}