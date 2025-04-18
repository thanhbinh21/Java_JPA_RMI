package dao;

import entity.KhachHang;

import java.util.List;
import java.util.Optional;

public interface KhachHangDAO {
    KhachHang findById(String id);
    List<KhachHang> findAll();
    boolean save(KhachHang khachHang);
    boolean update(KhachHang khachHang);
    boolean delete(String id);
    KhachHang findBySoDienThoai(String soDienThoai);
}