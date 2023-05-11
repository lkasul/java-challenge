package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

/**
 * Employee service interface
 *
 * @author Laxmi
 */
public interface EmployeeService {

    /**
     * Get all employees list
     *
     * @return
     */
    public List<Employee> retrieveEmployees();

    /**
     * Get employee by id
     *
     * @param employeeId
     * @return
     */
    public Employee getEmployee(Long employeeId);

    /**
     * Save Employee record
     *
     * @param employee
     * @return
     */
    public String saveEmployee(Employee employee);

    /**
     * Delete Employee record
     *
     * @param employeeId
     * @return
     */
    public String deleteEmployee(Long employeeId);

    /**
     * Update employee
     *
     * @param employee
     * @param employeeId
     * @return
     */
    public String updateEmployee(Employee employee, Long employeeId);

}