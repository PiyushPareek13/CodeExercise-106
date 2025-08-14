package com.example.company;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Path filePath = Path.of("src/main/resources/employees.csv");
        EmployeeService service = new EmployeeService();
        Map<Integer, Employee> employees = service.loadEmployees(filePath);

        System.out.println("=== Salary Violations ===");
        List<String> salaryViolations = service.checkSalaryRules(employees);
        salaryViolations.forEach(System.out::println);

        System.out.println("\n=== Long Reporting Lines ===");
        List<String> longLines = service.checkReportingLines(employees);
        longLines.forEach(System.out::println);
    }
}
