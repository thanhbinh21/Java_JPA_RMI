package dao;

import entity.NhaSanXuat;

import java.util.List;
import java.util.Optional;

public interface NhaSanXuatDAO extends GenericDAO<NhaSanXuat, String> {
    NhaSanXuat findByTenNhaSanXuat(String tenNhaSanXuat);
}