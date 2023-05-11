package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> retrieveEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return employees;
        } catch (Exception e) {
            return null;
        }
    }

    public Employee getEmployee(Long employeeId) {
        try {
            Optional<Employee> optEmp = employeeRepository.findById(employeeId);
            return optEmp.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public String saveEmployee(Employee employee){
        try {
            employeeRepository.save(employee);
            return "Employee saved successfully ";
        } catch (Exception e) {
            return "Exception occured";
        }
    }

    public String deleteEmployee(Long employeeId){
        try {
        employeeRepository.deleteById(employeeId);
        return "Employee deleted successfully";
        } catch (Exception e) {
            return "Exception occured";
        }
    }
}