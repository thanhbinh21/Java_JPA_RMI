package dao;

import entity.DanhMuc;

import java.util.List;
import java.util.Optional;

public interface DanhMucDAO {
    DanhMuc findById(String maDanhMuc);
    List<DanhMuc> findAll();
    boolean save(DanhMuc danhMuc);
    boolean update(DanhMuc danhMuc);
    boolean delete(String maDanhMuc);
    Optional<DanhMuc> findByTenDanhMuc(String tenDanhMuc);
}