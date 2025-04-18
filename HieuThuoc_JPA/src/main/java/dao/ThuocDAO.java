package dao;

import entity.NhaSanXuat;
import entity.Thuoc;

import java.util.List;
import java.util.Optional;

public interface ThuocDAO {
    Thuoc findById(String maThuoc);
    List<Thuoc> findAll();
    boolean save(Thuoc thuoc);
    boolean update(Thuoc thuoc);
    boolean delete(String maThuoc);
    Optional<Thuoc> findByTenThuoc(String tenThuoc);
    List<Thuoc> findByDanhMuc(String danhMuc);
    List<Thuoc> findByNhaSanXuat(NhaSanXuat nhaSanXuat);

    void updateSoLuongTon(Thuoc thuoc, int updatedSoLuongTon);
}