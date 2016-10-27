package iris.contacts;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "HASTA", schema = "ADMIN")
public class Contact {
	
	@Id
	@GenericGenerator(name = "mygen1", strategy = "increment")
	@GeneratedValue(generator = "mygen1")
	private int id;
	@Column
	private String ad;
	@Column
	private Date dogumtarihi;
	@Column
	private String cinsiyet;
	@Column
	private String adres;
	@Column
	private String evtel;
	@Column
	private String ceptel;
	@Column
	private String email;
	@Column
	private Date gelistarihi;
	@Column
	private Date sonrakirandevu;
	@Column
	private String sikayet;
	@Column
	private Date sikayet_bas_tar;
	@Column
	private String meslek;
	@Column
	private String bilg_sure;
	@Column
	private String havalandirma;
	@Column
	private String lens;
	@Column
	private String operasyon;
	@Column
	private String uygulamalar;
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public Contact() {
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getGelistarihiStr() {
		return gelistarihi == null? "" : dateFormat.format(gelistarihi);
	}

	public String getSonrakirandevuStr() {
		return sonrakirandevu == null? "" : dateFormat.format(sonrakirandevu);
	}

	public int getYas() {
		Date today = new Date();
		return dogumtarihi == null? 0 : today.getYear() - dogumtarihi.getYear();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public Date getDogumtarihi() {
		return dogumtarihi;
	}

	public void setDogumtarihi(Date dogumtarihi) {
		this.dogumtarihi = dogumtarihi;
	}

	public String getCinsiyet() {
		return cinsiyet;
	}

	public void setCinsiyet(String cinsiyet) {
		this.cinsiyet = cinsiyet;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public String getEvtel() {
		return evtel;
	}

	public void setEvtel(String evtel) {
		this.evtel = evtel;
	}

	public String getCeptel() {
		return ceptel;
	}

	public void setCeptel(String ceptel) {
		this.ceptel = ceptel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getGelistarihi() {
		return gelistarihi;
	}

	public void setGelistarihi(Date gelistarihi) {
		this.gelistarihi = gelistarihi;
	}

	public Date getSonrakirandevu() {
		return sonrakirandevu;
	}

	public void setSonrakirandevu(Date sonrakirandevu) {
		this.sonrakirandevu = sonrakirandevu;
	}

	public String getSikayet() {
		return sikayet;
	}

	public void setSikayet(String sikayet) {
		this.sikayet = sikayet;
	}

	public Date getSikayet_bas_tar() {
		return sikayet_bas_tar;
	}

	public void setSikayet_bas_tar(Date sikayet_bas_tar) {
		this.sikayet_bas_tar = sikayet_bas_tar;
	}

	public String getMeslek() {
		return meslek;
	}

	public void setMeslek(String meslek) {
		this.meslek = meslek;
	}

	public String getBilg_sure() {
		return bilg_sure;
	}

	public void setBilg_sure(String bilg_sure) {
		this.bilg_sure = bilg_sure;
	}

	public String getHavalandirma() {
		return havalandirma;
	}

	public void setHavalandirma(String havalandirma) {
		this.havalandirma = havalandirma;
	}

	public String getLens() {
		return lens;
	}

	public void setLens(String lens) {
		this.lens = lens;
	}

	public String getOperasyon() {
		return operasyon;
	}

	public void setOperasyon(String operasyon) {
		this.operasyon = operasyon;
	}

	public String getUygulamalar() {
		return uygulamalar;
	}

	public void setUygulamalar(String uygulamalar) {
		this.uygulamalar = uygulamalar;
	}
}
