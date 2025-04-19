package dao;

import entity.TaiKhoan;

public interface TaiKhoanDAO extends GenericDAO<TaiKhoan, String> {
    TaiKhoan findByIdAndPassword(String id, String password);
}
