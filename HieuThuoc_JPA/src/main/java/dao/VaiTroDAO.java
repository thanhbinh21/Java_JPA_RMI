package dao;

import entity.VaiTro;

import java.util.List;
import java.util.Optional;

public interface VaiTroDAO extends GenericDAO<VaiTro, String> {
    VaiTro findByTenVaiTro(String tenVaiTro);
}