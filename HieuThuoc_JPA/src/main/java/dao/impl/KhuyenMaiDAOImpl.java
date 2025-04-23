package dao.impl;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import entity.Thuoc;

import java.util.List;
import java.util.Optional;

public class KhuyenMaiDAOImpl extends GenericDAOImpl<KhuyenMai, String> implements KhuyenMaiDAO {

    public KhuyenMaiDAOImpl() {
        super(KhuyenMai.class);
    }
    @Override
    public Optional<KhuyenMai> findByTenKhuyenMai(String tenKhuyenMai) {
        return Optional.empty();
    }
}
