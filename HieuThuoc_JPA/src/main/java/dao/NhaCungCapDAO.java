package dao;

import entity.NhaCungCap;

import java.util.List;
import java.util.Optional;

public interface NhaCungCapDAO {
    NhaCungCap findById(String id);
    List<NhaCungCap> findAll();
    boolean save(NhaCungCap nhaCungCap);
    boolean update(NhaCungCap nhaCungCap);
    boolean delete(String id);
    Optional<NhaCungCap> findByTen(String ten);
}