package com.quanlyhoso.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ho_so database table.
 * 
 */
@Entity
@Table(name="ho_so")
@NamedQuery(name="HoSo.findAll", query="SELECT h FROM HoSo h")
public class HoSo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="so_lt")
	private String soLt;

	private String cbnl;

	@Column(name="ma_dv_nl")
	private String maDvNl;

	@Column(name="ma_loai")
	private String maLoai;

	@Column(name="ngay_dk")
	private String ngayDk;

	@Column(name="ngay_nl")
	private String ngayNl;
	
	public String getNgayNl() {
		return ngayNl;
	}

	public void setNgayNl(String ngayNl) {
		this.ngayNl = ngayNl;
	}

	@Column(name="thoi_han_bao_quan")
	private String thoiHanBaoQuan;

	private int stt;
	public String getThoiHanBaoQuan() {
		return thoiHanBaoQuan;
	}

	public void setThoiHanBaoQuan(String thoiHanBaoQuan) {
		this.thoiHanBaoQuan = thoiHanBaoQuan;
	}

	@Column(name="ngay_kt")
	private String ngayKt;

	@Column(name="ngay_lap")
	private String ngayLap;

	@Column(name="so_dk")
	private String soDk;

	@Lob
	@Column(name="trich_yeu")
	private String trichYeu;

	public HoSo() {
	}

	public String getSoLt() {
		return this.soLt;
	}

	public void setSoLt(String soLt) {
		this.soLt = soLt;
	}

	public String getCbnl() {
		return this.cbnl;
	}

	public void setCbnl(String cbnl) {
		this.cbnl = cbnl;
	}

	public String getMaDvNl() {
		return this.maDvNl;
	}

	public void setMaDvNl(String maDvNl) {
		this.maDvNl = maDvNl;
	}

	public String getMaLoai() {
		return this.maLoai;
	}

	public void setMaLoai(String maLoai) {
		this.maLoai = maLoai;
	}

	public String getNgayDk() {
		return this.ngayDk;
	}

	public void setNgayDk(String ngayDk) {
		this.ngayDk = ngayDk;
	}

	public String getNgayKt() {
		return this.ngayKt;
	}

	public void setNgayKt(String ngayKt) {
		this.ngayKt = ngayKt;
	}

	public String getNgayLap() {
		return this.ngayLap;
	}

	public void setNgayLap(String ngayLap) {
		this.ngayLap = ngayLap;
	}

	public String getSoDk() {
		return this.soDk;
	}

	public void setSoDk(String soDk) {
		this.soDk = soDk;
	}

	public String getTrichYeu() {
		return this.trichYeu;
	}

	public void setTrichYeu(String trichYeu) {
		this.trichYeu = trichYeu;
	}

	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

}