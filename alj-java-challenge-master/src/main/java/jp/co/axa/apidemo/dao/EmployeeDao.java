package jp.co.axa.apidemo.dao;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Dao layer for Employee
 *
 * @author Laxmi
 */
@Repository
public class EmployeeDao {

    /** Employee repository */
    private final EmployeeRepository employeeRepository;

    public EmployeeDao(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Cache db call to get all employees
     *
     * @return
     */
    @Cacheable("employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Cache db call to get employee by id
     *
     * @param id
     * @return
     */
    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Cache db call to save employee
     *
     * @param employee
     * @return
     */
    @CacheEvict(value = "employees", allEntries=true)
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Cache db call to update employee
     *
     * @param employee
     * @return
     */
    @CachePut(value = "employees", key = "#employee.id")
    public Employee updateOneEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Cache db call to delete employee
     *
     * @param id
     */
    @CacheEvict(value = "employees", key = "#id")
    public void deleteOneEmployee(Long id) {
        employeeRepository.deleteById(id);
    }


}
