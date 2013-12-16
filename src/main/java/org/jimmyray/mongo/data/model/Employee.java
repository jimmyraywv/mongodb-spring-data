package org.jimmyray.mongo.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.Strings;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mysema.query.annotations.QueryEntity;

/**
 * Employee model object with MongoDB meta-mapping annotations.
 * 
 * @author jimmyray
 * @version 1.0
 */
@JsonIgnoreProperties({"dirty"})
@QueryEntity
@Document(collection = EmployeeProperties.COLLECTION)
public class Employee extends Person {
	private static final long serialVersionUID = 6684888389354589761L;

	@Field(EmployeeProperties.ADDRESS)
	private Address address;
	@Indexed(sparse = true, unique = true)
	@Field(EmployeeProperties.EMPLOYEE_ID)
	private String employeeId;
	@Indexed(sparse = true)
	@Field(EmployeeProperties.HIRE_DATE)
	private Date hireDate;
	@Field(EmployeeProperties.DEPARTMENT)
	private Department department;
	@Indexed(sparse = true)
	@Field(EmployeeProperties.TITLE)
	private String title;
	@Field(EmployeeProperties.SALARY)
	private int salary;
	@Transient
	private boolean dirty;

	public Employee() {

	}

	/*
	 * Private copy constructor for use in "Cloning" Employee objects.
	 * 
	 * @param id
	 * 
	 * @param lastName
	 * 
	 * @param firstName
	 * 
	 * @param gender
	 * 
	 * @param birthDate
	 * 
	 * @param address
	 * 
	 * @param employeeId
	 * 
	 * @param hireDate
	 * 
	 * @param department
	 * 
	 * @param title
	 * 
	 * @param salary
	 */
	private Employee(String id, String lastName, String firstName,
			String gender, Date birthDate, Address address, String employeeId,
			Date hireDate, Department department, String title, int salary) {
		super(id, lastName, firstName, gender, birthDate);
		this.address = address;
		this.employeeId = employeeId;
		this.hireDate = hireDate;
		this.department = department;
		this.title = title;
		this.salary = salary;
	}

	/**
	 * Public copy constructor for "cloning" without using clone() and
	 * Cloneable.
	 * 
	 * @param employee
	 */
	public Employee(Employee employee) {
		this(employee.getId(), employee.getLastName(), employee.getFirstName(),
				employee.getGender(), employee.getBirthDate(), employee
						.getAddress(), employee.getEmployeeId(), employee
						.getHireDate(), employee.getDepartment(), employee
						.getTitle(), employee.getSalary());
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public final Date getHireDate() {
		return (null != this.hireDate) ? new Date(this.hireDate.getTime())
				: null;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = (Date) hireDate.clone();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return this.getClass().getSimpleName()
				+ "|"
				+ Strings.nullValue(this.getId(), Strings.EMPTY_STRING)
				+ "|"
				+ Strings.nullValue(this.getEmployeeId(), Strings.EMPTY_STRING)
				+ "|"
				+ ((null != this.getHireDate()) ? sdf
						.format(this.getHireDate()) : Strings.EMPTY_STRING)
				+ "|"
				+ Strings.nullValue(this.getFirstName(), Strings.EMPTY_STRING)
				+ "|"
				+ Strings.nullValue(this.getLastName(), Strings.EMPTY_STRING)
				+ "|"
				+ Strings.nullValue(this.getGender(), Strings.EMPTY_STRING)
				+ "|"
				+ Strings.nullValue(this.getTitle(), Strings.EMPTY_STRING)
				+ "|"
				+ ((this.getSalary() == 0) ? Strings.EMPTY_STRING : this
						.getSalary())
				+ "|"
				+ ((null == this.getDepartment()) ? Strings.EMPTY_STRING : this
						.getDepartment().toString())
				+ "|"
				+ ((null == this.getBirthDate()) ? Strings.EMPTY_STRING : sdf
						.format(this.getBirthDate()))
				+ "|"
				+ ((null == this.getAddress()) ? Strings.EMPTY_STRING : this
						.getAddress().toString());
	}
}
