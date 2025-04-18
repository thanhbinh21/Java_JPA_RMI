package dao;

import entity.NhaSanXuat;

import java.util.List;
import java.util.Optional;

public interface NhaSanXuatDAO {
    NhaSanXuat findById(String maNhaSanXuat);
    List<NhaSanXuat> findAll();
    boolean save(NhaSanXuat nhaSanXuat);
    boolean update(NhaSanXuat nhaSanXuat);
    boolean delete(String maNhaSanXuat);
    Optional<NhaSanXuat> findByTenNhaSanXuat(String tenNhaSanXuat);
}