package jp.co.axa.apidemo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test for EmployeeController security settings
 *
 * @author Laxmi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeControllerTest {

    /** Object Mapper */
    private static final ObjectMapper om = new ObjectMapper();

    /** Test rest template */
    @Autowired
    private TestRestTemplate restTemplate;

    /** Mock of EmployeeService */
    @MockBean
    private EmployeeService mockEmployeeService;

    Employee emp1 = Employee.builder()
            .id(1L)
            .name("abc")
            .salary(120000)
            .department("IT")
            .build();
    Employee emp2 = Employee.builder()
            .id(2L)
            .name("def")
            .salary(130000)
            .department("CSE")
            .build();
    Employee emp3 = Employee.builder()
            .id(3L)
            .name("ghi")
            .salary(140000)
            .department("Mech")
            .build();

    /**
     * save employee with admin credentials
     *
     * @throws Exception
     */
    @Test
    public void saveEmployee_admin_success() throws Exception {

        when(mockEmployeeService.getEmployee(1L)).thenReturn(null);
        when(mockEmployeeService.saveEmployee(emp1)).thenReturn("Employee saved successfully");

        JSONObject employeeJson = new JSONObject();
        employeeJson.put("id","1");
        employeeJson.put("name","abc");
        employeeJson.put("salary","120000");
        employeeJson.put("department","IT");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin","password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(employeeJson.toString(), headers);

        final ResponseEntity<String> postResponse = restTemplate.exchange(
                "/api/v1/employees",
                HttpMethod.POST,
                request,
                String.class);
        printJSON(postResponse);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, postResponse.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Assert.assertEquals(true, postResponse.getBody().contains("Employee saved successfully"));

    }

    /**
     * save employee with user credentials
     *
     * @throws Exception
     */
    @Test
    public void saveEmployee_user_failure() throws Exception {
        when(mockEmployeeService.getEmployee(1L)).thenReturn(null);
        when(mockEmployeeService.saveEmployee(emp1)).thenReturn("Employee saved successfully");

        JSONObject employeeJson = new JSONObject();
        employeeJson.put("id","1");
        employeeJson.put("name","abc");
        employeeJson.put("salary","120000");
        employeeJson.put("department","IT");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user","password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(employeeJson.toString(), headers);

        final ResponseEntity<String> postResponse = restTemplate.exchange(
                "/api/v1/employees",
                HttpMethod.POST,
                request,
                String.class);
        printJSON(postResponse);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, postResponse.getHeaders().getContentType());
        assertEquals(HttpStatus.FORBIDDEN, postResponse.getStatusCode());
    }

    /**
     * Get employee by id, with user credentials
     */
    @Test
    public void getEmpById_user_success(){
        String expected = "{\"id\":1,\"name\":\"abc\",\"salary\":120000,\"department\":\"IT\"}";

        when(mockEmployeeService.getEmployee(1L)).thenReturn(emp1);
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/v1/employees/1", String.class);

        printJSON(response);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    /**
     * Get employee by id, with no credentials
     */
    @Test
    public void getEmpById_nologin_failure() {
        when(mockEmployeeService.getEmployee(1L)).thenReturn(emp1);
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/v1/employees/1", String.class);

        printJSON(response);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Get all employees, with user credentials
     */
    @Test
    public void getAllEmpRecords_user_success() {
        List<Employee> employeeList = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));
        when(mockEmployeeService.retrieveEmployees()).thenReturn(employeeList);
        ResponseEntity<JsonNode> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/v1/employees", JsonNode.class);
        printJSON(response);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3,response.getBody().size());
    }

    /**
     * Get all employees, with no credentials
     */
    @Test
    public void getAllEmpRecords_noLogin_failure() throws Exception {
        List<Employee> employeeList = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));
        when(mockEmployeeService.retrieveEmployees()).thenReturn(employeeList);
        ResponseEntity<JsonNode> response = restTemplate
                .getForEntity("/api/v1/employees", JsonNode.class);
        printJSON(response);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Update employee, with admin credentials
     *
     * @throws Exception
     */
    @Test
    public void updateEmployee_admin_success() throws Exception {
        emp1.setDepartment("updatedDept");
        when(mockEmployeeService.updateEmployee(emp1,1L)).thenReturn("Employee updated successfully");
        JSONObject employeeJson = new JSONObject();
        employeeJson.put("id","1");
        employeeJson.put("name","abc");
        employeeJson.put("salary","120000");
        employeeJson.put("department","updatedDept");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin","password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(employeeJson.toString(), headers);

        final ResponseEntity<String> putresponse = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.PUT,
                request,
                String.class);
        printJSON(putresponse);
        Assert.assertEquals(true, putresponse.getBody().contains("Employee updated successfully"));
    }

    /**
     * Update employee, with user credentials
     *
     * @throws Exception
     */
    @Test
    public void updateEmployee_user_failure() throws Exception {
        emp1.setDepartment("updatedDept");
        when(mockEmployeeService.updateEmployee(emp1,1L)).thenReturn("Employee updated successfully");
        JSONObject employeeJson = new JSONObject();
        employeeJson.put("id","1");
        employeeJson.put("name","abc");
        employeeJson.put("salary","120000");
        employeeJson.put("department","updatedDept");
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user","password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(employeeJson.toString(), headers);

        final ResponseEntity<String> putresponse = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.PUT,
                request,
                String.class);
        printJSON(putresponse);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, putresponse.getHeaders().getContentType());
        assertEquals(HttpStatus.FORBIDDEN, putresponse.getStatusCode());
    }

    /**
     * Delete employee, with admin credentials
     */
    @Test
    public void deleteEmployee_admin_success() {
        when(mockEmployeeService.getEmployee(1L)).thenReturn(emp1);
        when(mockEmployeeService.deleteEmployee(emp1.getId())).thenReturn("Employee deleted successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin","password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        final ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.DELETE,
                request,
                String.class);

        printJSON(deleteResponse);
        Assert.assertEquals(true, deleteResponse.getBody().contains("Employee deleted successfully"));
    }

    /**
     * Delete employee, with user credentials
     */
    @Test
    public void deleteEmployee_user_failure() {
        when(mockEmployeeService.getEmployee(1L)).thenReturn(emp1);
        when(mockEmployeeService.deleteEmployee(emp1.getId())).thenReturn("Employee deleted successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user","password");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        final ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.DELETE,
                request,
                String.class);

        printJSON(deleteResponse);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, deleteResponse.getHeaders().getContentType());
        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());
    }

    /**
     * Delete employee, with no credentials
     */
    @Test
    public void deleteEmployee_noLogin_failure() {
        when(mockEmployeeService.getEmployee(1L)).thenReturn(emp1);
        when(mockEmployeeService.deleteEmployee(emp1.getId())).thenReturn("Employee deleted successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request =
                new HttpEntity<String>(headers);

        final ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/v1/employees/1",
                HttpMethod.DELETE,
                request,
                String.class);

        printJSON(deleteResponse);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, deleteResponse.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, deleteResponse.getStatusCode());
    }

    /**
     * Print api response
     *
     * @param object
     */
    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
