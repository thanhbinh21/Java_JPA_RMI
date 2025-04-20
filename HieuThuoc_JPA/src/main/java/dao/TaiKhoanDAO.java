package dao;

import entity.TaiKhoan;
import jakarta.persistence.EntityManager;

public interface TaiKhoanDAO extends GenericDAO<TaiKhoan, String> {
    TaiKhoan findByIdAndPassword(String id, String password);
}
