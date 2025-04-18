package dao;

import entity.KhuyenMai;

import java.util.List;
import java.util.Optional;

public interface KhuyenMaiDAO {
    KhuyenMai findById(String maKhuyenMai);
    List<KhuyenMai> findAll();
    boolean save(KhuyenMai khuyenMai);
    boolean update(KhuyenMai khuyenMai);
    boolean delete(String maKhuyenMai);
    Optional<KhuyenMai> findByTenKhuyenMai(String tenKhuyenMai);
}