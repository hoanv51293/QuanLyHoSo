package com.quanlyhoso.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quanlyhoso.entity.HoSo;

@Repository
public interface IHoSoRepository extends JpaRepository<HoSo, String> {

	@Query(value = "SELECT e FROM HoSo e WHERE "
			+ "(:id = '' or e.soLt like %:id% or e.cbnl like %:id% "
			+ "or e.maDvNl like %:id%  "
			+ "or e.ngayDk like %:id% or e.ngayNl like %:id% "
			+ "or e.thoiHanBaoQuan like %:id% or e.ngayKt like %:id% "
			+ "or e.ngayLap like %:id% or e.soDk like %:id% ) "
			+ "AND (:maDvNl = '' or e.maDvNl = :maDvNl) "
			+ "AND (:maLoai = '' or e.maLoai = :maLoai) "
			+ "order by e.maLoai, e.stt ")
	public Page<HoSo> findList(@Param("id") String id, @Param("maDvNl")String maDvNl, @Param("maLoai")String maLoai, Pageable pageable);

	@Query(value = "SELECT coalesce(MAX(e.stt),0) FROM HoSo e where e.maLoai = :maLoai")
	public int maxByMaLoai(@Param("maLoai") String maLoai);

	@Query(value = "SELECT e FROM HoSo e where e.soDk = :soDk")
	public List<HoSo> findBySoDk(@Param("soDk") String soDk);
}