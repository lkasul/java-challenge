package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void save_new_Employee_success() {
        when(employeeRepositoryMock.findById(1L)).thenReturn(null);
        Employee empOne = new Employee(1L, "John", 1250, "IT");
        assertEquals("Employee saved successfully", employeeService.saveEmployee(empOne));
    }

    @Test
    public void save_new_Employee_id_null_failure() {
        Employee empOne = Employee.builder().name("as").salary(1230).department("IT").build();
        System.out.println(employeeService.saveEmployee(empOne));
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("id cannot be null"));
    }

    @Test
    public void save_new_Employee_id_zero_failure() {
        Employee empOne = Employee.builder().id(0L).name("as").salary(1230).department("IT").build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("id must be greater than 1"));
    }

    @Test
    public void save_new_Employee_name_null_failure() {
        Employee empOne = Employee.builder().id(1L).salary(1230).department("IT").build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("name cannot be null"));
    }

    @Test
    public void save_new_Employee_name_blank_failure() {
        Employee empOne = Employee.builder().id(1L).name("").salary(1230).department("IT").build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("name must be greater than 1 character"));
    }

    @Test
    public void save_new_Employee_salary_null_failure() {
        Employee empOne = Employee.builder().id(1L).name("abc").department("IT").build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("salary cannot be null"));
    }

    @Test
    public void save_new_Employee_salary_less_than_1000_failure() {
        Employee empOne = Employee.builder().id(1L).name("as").salary(30).department("IT").build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("salary must be greater than 1000"));
    }

    @Test
    public void save_new_Employee_department_null_failure() {
        Employee empOne = Employee.builder().id(1L).name("abc").salary(1230).build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("department cannot be null"));
    }

    @Test
    public void save_new_Employee_department_empty_failure() {
        Employee empOne = Employee.builder().id(1L).name("as").salary(1230).department("").build();
        Assert.assertEquals(true, employeeService.saveEmployee(empOne).contains("department must be greater than 1 character"));
    }

    @Test
    public void save_already_Exists_employee_failure() {
        Employee empOne = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(empOne));
        assertEquals("Employee already exists", employeeService.saveEmployee(empOne));
    }

    @Test
    public void save_employee_exception_failure() {
        Employee empOne = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.save(empOne)).thenThrow(new ArrayIndexOutOfBoundsException());
        assertEquals("Exception occured", employeeService.saveEmployee(empOne));
    }

    @Test
    public void getAllEmployeesTest_success() {
        Employee emp1 = new Employee(1L, "John", 1250, "IT");
        Employee emp2 = new Employee(2L, "Alex", 1350, "CSE");
        Employee emp3 = new Employee(3L, "Steve", 1450, "MECH");
        List<Employee> employeeList = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));

        when(employeeRepositoryMock.findAll()).thenReturn(employeeList);

        List<Employee> empList = employeeService.retrieveEmployees();
        assertEquals(3, empList.size());
    }

    @Test
    public void getAllEmployeesTest_result_not_found() {
        when(employeeRepositoryMock.findAll()).thenReturn(null);
        List<Employee> empList = employeeService.retrieveEmployees();
        assertEquals(null, empList);
    }

    @Test
    public void getAllEmployeesTest_exception() {
        when(employeeRepositoryMock.findAll()).thenThrow(new ArrayIndexOutOfBoundsException());
        List<Employee> empList = employeeService.retrieveEmployees();
        assertEquals(null, empList);
    }

    @Test
    public void getEmpById_success() {
        Employee emp1 = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(emp1));
        Employee emp = employeeService.getEmployee(emp1.getId());
        Assert.assertEquals(true, emp.getName().contains("John"));
    }

    @Test
    public void getEmpById_not_found() {
        when(employeeRepositoryMock.findById(1L)).thenReturn(null);
        Employee emp = employeeService.getEmployee(1L);
        Assert.assertEquals(null, emp);
    }

    @Test
    public void getEmpById_exception() {
        when(employeeRepositoryMock.findById(1L)).thenThrow(new ArrayIndexOutOfBoundsException());
        Employee emp = employeeService.getEmployee(1L);
        Assert.assertEquals(null, emp);
    }

    @Test
    public void update_emp_success() {
        Employee emp = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(emp));
        Employee emp1 = new Employee(1L, "Jennifer", 1250, "IT");
        assertEquals("Employee updated successfully", employeeService.updateEmployee(emp1,1L));
    }

    @Test
    public void update_emp_not_found() {
        when(employeeRepositoryMock.findById(1L)).thenReturn(null);
        Employee emp1 = new Employee(1L, "Jennifer", 1250, "IT");
        assertEquals("Employee does not exist", employeeService.updateEmployee(emp1,1L));
    }

    @Test
    public void update_emp_id_changed() {
        Employee emp = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(emp));
        Employee emp1 = new Employee(2L, "Jennifer", 1250, "IT");
        assertEquals("Employee id cannot be changed", employeeService.updateEmployee(emp1,1L));
    }

    @Test
    public void update_emp_id_null() {
        Employee emp = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(emp));
        Employee emp1 = Employee.builder().name("avc").salary(1230).department("CSE").build();
        Assert.assertEquals(true, employeeService.updateEmployee(emp1,1L).contains("id cannot be null"));
    }

    @Test
    public void update_emp_exception() {
        Employee emp = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(emp));
        Employee emp1 = Employee.builder().id(1L).name("avc").salary(1230).department("CSE").build();
        when(employeeRepositoryMock.save(emp1)).thenThrow(new ArrayIndexOutOfBoundsException());
        assertEquals("Exception occured", employeeService.updateEmployee(emp1,1L));
    }

    @Test
    public void delete_emp_success() {
        Employee emp = new Employee(1L, "John", 1250, "IT");
        when(employeeRepositoryMock.findById(1L)).thenReturn(java.util.Optional.of(emp));
        assertEquals("Employee deleted successfully", employeeService.deleteEmployee(1L));
    }

    @Test
    public void delete_emp_not_found() {
        when(employeeRepositoryMock.findById(1L)).thenReturn(null);
        assertEquals("Employee does not exist", employeeService.deleteEmployee(1L));
    }

}
