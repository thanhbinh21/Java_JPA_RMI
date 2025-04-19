package dao;

import entity.KhachHang;

public interface KhachHangDAO extends GenericDAO<KhachHang, String> {
    KhachHang selectBySdt(String sdt);
}