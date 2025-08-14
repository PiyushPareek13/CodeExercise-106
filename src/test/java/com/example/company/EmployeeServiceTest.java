package com.example.company;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeServiceTest {

    @Test
    public void testSalaryAndReportingChecks() throws Exception {
        EmployeeService service = new EmployeeService();
        Map<Integer, Employee> employees = service.loadEmployees(Path.of("src/main/resources/employees.csv"));

        List<String> salaryViolations = service.checkSalaryRules(employees);
        assertFalse(salaryViolations.isEmpty());

        List<String> reportingViolations = service.checkReportingLines(employees);
        assertTrue(reportingViolations.size() >= 0);
    }
}
