package jp.co.axa.apidemo.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Employee entity class
 *
 * @author Laxmi
 */
@Entity
@Table(name="EMPLOYEE")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    /** employee id */
    @Id
    @NotNull(message = "id cannot be null")
    @Min(value = 1, message = "id must be greater than 1")
    @Column(name="EMPLOYEE_ID")
    private Long id;

    /** employee name */
    @NotNull(message = "name cannot be null")
    @Size(min = 1, message = "name must be greater than 1 character")
    @Column(name="EMPLOYEE_NAME")
    private String name;

    /** employee salary */
    @NotNull(message = "salary cannot be null")
    @Min(value = 1000, message = "salary must be greater than 1000")
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    /** employee department */
    @NotNull(message = "department cannot be null")
    @Size(min = 1, message = "department must be greater than 1 character")
    @Column(name="DEPARTMENT")
    private String department;

}
