package dao;

import entity.NhanVien;
import entity.TaiKhoan;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanDAO {
    TaiKhoan findById(String tenTaiKhoan);
    List<TaiKhoan> findAll();
    boolean save(TaiKhoan taiKhoan);
    boolean update(TaiKhoan taiKhoan);
    boolean delete(String tenTaiKhoan);
    Optional<TaiKhoan> findByMatKhau(String matKhau);
    Optional<TaiKhoan> findByNhanVien(NhanVien nhanVien);
}