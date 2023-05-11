package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Employee Service Implementation class
 *
 * @author Laxmi
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{

    /** Employee repository */
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Get all employees list
     *
     * @return employeeList
     */
    public List<Employee> retrieveEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return employees;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get employee by id
     *
     * @param employeeId
     * @return
     */
    public Employee getEmployee(Long employeeId) {
        try {
            Optional<Employee> optEmp = employeeRepository.findById(employeeId);
            return optEmp.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Save Employee record
     *
     * @param employee
     * @return
     */
    public String saveEmployee(Employee employee){
        try {
            Employee emp = getEmployee(employee.getId());
            if (emp != null) {
                return "Employee already exists";
            }
            String validationMsg = isValid(employee);
            if(!StringUtils.isEmpty(validationMsg)) {
                return validationMsg;
            }
            employeeRepository.save(employee);
            return "Employee saved successfully";
        } catch (Exception e) {
            return "Exception occured";
        }
    }

    /**
     * Delete Employee record
     *
     * @param employeeId
     * @return
     */
    public String deleteEmployee(Long employeeId){
        try {
            Employee emp = getEmployee(employeeId);
            if (emp == null) {
                return "Employee does not exist";
            }
            employeeRepository.deleteById(employeeId);
            return "Employee deleted successfully";
        } catch (Exception e) {
            return "Exception occured";
        }
    }

    /**
     * Update employee
     *
     * @param employee
     * @param employeeId
     * @return
     */
    public String updateEmployee(Employee employee, Long employeeId) {
        try {
            Employee emp = getEmployee(employeeId);
            if (emp == null) {
                return "Employee does not exist";
            }
            String validationMsg = isValid(employee);
            if (!StringUtils.isEmpty(validationMsg)) {
                return validationMsg;
            }
            if (!employeeId.equals(employee.getId())) {
                return "Employee id cannot be changed";
            }
            employeeRepository.save(employee);
            return "Employee updated successfully";
        } catch (Exception e) {
            return "Exception occured";
        }
    }

    /**
     * employee validation
     *
     * @param employee
     * @return
     */
    private String isValid(Employee employee){
        StringBuilder violationMessage = new StringBuilder();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        if(!CollectionUtils.isEmpty(violations)) {
            for (ConstraintViolation<Employee> violation : violations) {
                violationMessage.append(violation.getMessage()).append(" ");
            }
        }
        return violationMessage.toString();
    }
}