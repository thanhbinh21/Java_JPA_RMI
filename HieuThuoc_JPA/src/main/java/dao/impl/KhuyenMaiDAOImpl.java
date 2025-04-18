package dao.impl;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import java.util.List;
import java.util.Optional;

public class KhuyenMaiDAOImpl implements KhuyenMaiDAO {
    @Override
    public KhuyenMai findById(String maKhuyenMai) {
        return null;
    }

    @Override
    public List<KhuyenMai> findAll() {
        return List.of();
    }

    @Override
    public boolean save(KhuyenMai khuyenMai) {
        return false;
    }

    @Override
    public boolean update(KhuyenMai khuyenMai) {
        return false;
    }

    @Override
    public boolean delete(String maKhuyenMai) {
        return false;
    }

    @Override
    public Optional<KhuyenMai> findByTenKhuyenMai(String tenKhuyenMai) {
        return Optional.empty();
    }
}
