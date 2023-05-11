package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.utils.JsonConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/employees", produces = "application/json;charset=UTF-8")
public class EmployeeController {

    private EmployeeService employeeService;

    private JsonConverter jsonConverter;

    public EmployeeController(EmployeeService employeeService, JsonConverter jsonConverter) {
        this.employeeService = employeeService;
        this.jsonConverter = jsonConverter;
    }

    @GetMapping
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/{employeeId}")
    public String getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp == null){
            return jsonConverter.convert("Employee not found");
        }
        return jsonConverter.convert(emp);
    }

    @PostMapping
    public String saveEmployee(@RequestBody Employee employee) {
        Employee emp = employeeService.getEmployee(employee.getId());
        if (emp != null) {
            return jsonConverter.convert("Employee already exists");
        }
        String validationMsg = isValid(employee);
        if(!StringUtils.isEmpty(validationMsg)) {
            return jsonConverter.convert(validationMsg);
        }
        return jsonConverter.convert(employeeService.saveEmployee(employee));
    }

    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp == null) {
            return jsonConverter.convert("Employee does not exist");
        }
        return  jsonConverter.convert(employeeService.deleteEmployee(employeeId));
    }

    @PutMapping("/{employeeId}")
    public String updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId) {
        Employee emp = employeeService.getEmployee(employeeId);
        if (emp == null) {
            return jsonConverter.convert("Employee does not exist");
        }
        if(!employeeId.equals(employee.getId())){
            return jsonConverter.convert("Employee id cannot be change, Please use same employee id");
        }
        String validationMsg = isValid(employee);
        if (!StringUtils.isEmpty(validationMsg)) {
            return jsonConverter.convert(validationMsg);
        }
        return jsonConverter.convert(employeeService.saveEmployee(employee));
    }

  private String isValid(Employee employee){
        StringBuilder violationMessage = new StringBuilder();
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
      if(!CollectionUtils.isEmpty(violations)) {
          for (ConstraintViolation<Employee> violation : violations) {
              violationMessage.append(violation.getMessage()).append(", ");
          }
      }
      return violationMessage.toString();
  }

}
