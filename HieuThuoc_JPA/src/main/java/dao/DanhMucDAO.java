package dao;

import entity.DanhMuc;
import java.util.List;

public interface DanhMucDAO extends GenericDAO<DanhMuc, String> {
    // Các phương thức đặc thù cho DanhMucDAO nếu có
    List<DanhMuc> findByTenDanhMuc(String tenDanhMuc);

    DanhMuc findByTen(String tenDM);
}