package dao;

import entity.NhanVien;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface NhanVienDAO extends GenericDAO<NhanVien, String> {
    NhanVien findByTen(String ten);
}