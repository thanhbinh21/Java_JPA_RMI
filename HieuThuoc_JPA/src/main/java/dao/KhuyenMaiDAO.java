package dao;

import entity.KhuyenMai;

import java.util.List;
import java.util.Optional;

public interface KhuyenMaiDAO extends GenericDAO<KhuyenMai, String> {
    Optional<KhuyenMai> findByTenKhuyenMai(String tenKhuyenMai);
}