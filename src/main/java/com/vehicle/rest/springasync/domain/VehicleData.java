package com.vehicle.rest.springasync.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Vechile")
@Table(name = "Vechile")
public class VehicleData {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
 
    @Column(name="vechile_model")
    private String vechileModel;
     
    @Column(name="vechile_color")
    private String vechileColor;
    
    @Column(name="vin")
    private String vin;
    
    @Column(name="year")
    private Integer year;
    
    @Column(name="make")
    private String make;
    
    @Column(name="model")
    private String model;
    
    @Column(name="transmissionType")
    private String transmissionType;

    
    
	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the transmissionType
	 */
	public String getTransmissionType() {
		return transmissionType;
	}

	/**
	 * @param transmissionType the transmissionType to set
	 */
	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @return the vechileModel
	 */
	public String getVechileModel() {
		return vechileModel;
	}

	/**
	 * @param vechileModel the vechileModel to set
	 */
	public void setVechileModel(String vechileModel) {
		this.vechileModel = vechileModel;
	}

	/**
	 * @return the vechileColor
	 */
	public String getVechileColor() {
		return vechileColor;
	}

	/**
	 * @param vechileColor the vechileColor to set
	 */
	public void setVechileColor(String vechileColor) {
		this.vechileColor = vechileColor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VechileData [id=" + id + ", vechileModel=" + vechileModel + ", vechileColor=" + vechileColor + "]";
	}
     
    
}