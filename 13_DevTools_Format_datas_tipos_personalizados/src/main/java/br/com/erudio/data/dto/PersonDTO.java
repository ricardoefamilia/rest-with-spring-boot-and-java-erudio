package br.com.erudio.data.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.erudio.serializer.GenderSerializer;

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//mudando a ordem dos dados 
//@JsonPropertyOrder({"id", "address", "first_name", "last_name", "gender"})
@JsonFilter("PersonFilter")
public class PersonDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	//@JsonProperty("first_name")
	private String firstName;
	
	//@JsonProperty("last_name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lastName;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String phoneNumber;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date birthDay;
	private String address;
	
	//@JsonIgnore
	@JsonSerialize(using = GenderSerializer.class)
	private String gender;
	
	
	private String sensitiveData;
	
	public PersonDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSensitiveData() {
		return sensitiveData;
	}

	public void setSensitiveData(String sensitiveData) {
		this.sensitiveData = sensitiveData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, birthDay, firstName, gender, id, lastName, phoneNumber, sensitiveData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDTO other = (PersonDTO) obj;
		return Objects.equals(address, other.address) && Objects.equals(birthDay, other.birthDay)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(gender, other.gender)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(phoneNumber, other.phoneNumber) && Objects.equals(sensitiveData, other.sensitiveData);
	}

}
