package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.utils.JsonConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * Employee Controller
 *
 * @author  Laxmi
 */
@RestController
@RequestMapping(value = "/api/v1/employees", produces = "application/json;charset=UTF-8")
public class EmployeeController {

    /* Employee Service */
    private final EmployeeService employeeService;

    /* json converter */
    private final JsonConverter jsonConverter;

    /* constructor */
    public EmployeeController(final EmployeeService employeeService, final JsonConverter jsonConverter) {
        this.employeeService = employeeService;
        this.jsonConverter = jsonConverter;
    }

    /**
     * Save employee
     *
     * @param employee
     * @return
     */
    @PostMapping
    public String saveEmployee(@RequestBody Employee employee) {
        return jsonConverter.convert(employeeService.saveEmployee(employee));
    }

    /**
     * Get list of all employees
     *
     * @return employeeList
     */
    @GetMapping
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    /**
     * Get employee by id
     *
     * @param employeeId
     * @return employee
     */
    @GetMapping("/{employeeId}")
    public String getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp == null){
            return jsonConverter.convert("Employee not found");
        }
        return jsonConverter.convert(emp);
    }

    /**
     * Update employee by id
     *
     * @param employee
     * @param employeeId
     * @return
     */
    @PutMapping("/{employeeId}")
    public String updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId) {
        return jsonConverter.convert(employeeService.updateEmployee(employee,employeeId));
    }

    /**
     * Delete employee
     *
     * @param employeeId
     * @return
     */
    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        return  jsonConverter.convert(employeeService.deleteEmployee(employeeId));
    }
}
