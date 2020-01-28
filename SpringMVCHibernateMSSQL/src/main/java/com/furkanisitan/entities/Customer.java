package com.furkanisitan.entities;

import com.furkanisitan.core.Decryption;
import com.furkanisitan.core.Encryption;
import com.furkanisitan.core.Encryptor;
import com.furkanisitan.core.entities.IEntity;


import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Customers")
@DynamicUpdate
public class Customer implements IEntity, Serializable {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "Salary")
    private int salary;

    @Column(name = "Fullname")
    private String fullname;

    @Column(name = "Email",length=4000)
    private String email;

    @Column(name = "PhoneNumber",length=4000)
    private String phoneNumber;

    @Column(name = "DateOfBirth")
    private String dateOfBirth;

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
    	Encryptor encryptor = Encryptor.getCryptoInstance();
    	String decryptedEmail = "default@default.com";
    	try {
    		decryptedEmail = encryptor.base64Decode(email);
			return decryptedEmail;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.out);
			return null;
		}
//        return email;
    }

    public void setEmail(String email) {
//        this.email = email;
    	Encryptor encryptor = Encryptor.getCryptoInstance();      
    	try {
//			this.email = Encryption.encrypt(email);
			this.email = encryptor.base64Encode(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.out);
		}
        
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
