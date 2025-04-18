package dao;

import entity.VaiTro;

import java.util.List;
import java.util.Optional;

public interface VaiTroDAO {
    VaiTro findById(String id);
    List<VaiTro> findAll();
    boolean save(VaiTro vaiTro);
    boolean update(VaiTro vaiTro);
    boolean delete(String id);
    Optional<VaiTro> findByTenVaiTro(String tenVaiTro);
}