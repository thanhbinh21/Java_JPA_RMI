package dao;

import entity.KhachHang;
import java.util.List;

public interface KhachHangDAO extends GenericDAO<KhachHang, String> {
    KhachHang selectBySdt(String sdt);
    
    List<KhachHang> findByGender(boolean genderFilter);
    
    List<KhachHang> findByName(String searchValue);
    
    List<KhachHang> findByPhone(String searchValue);
}