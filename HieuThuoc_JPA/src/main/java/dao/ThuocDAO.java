package dao;

import entity.DanhMuc;
import entity.KhachHang;
import entity.Thuoc;
import entity.TaiKhoan;
import entity.HoaDon;

import java.util.List;

public interface ThuocDAO extends GenericDAO<Thuoc, String> {
    List<Thuoc> selectByDanhMuc(DanhMuc danhMuc);
    List<Thuoc> searchByKeyword(String keyword);
    Thuoc getReference(String id);
}