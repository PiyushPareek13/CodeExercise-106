package com.example.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class EmployeeService {

    public Map<Integer, Employee> loadEmployees(Path filePath) throws IOException {
        Map<Integer, Employee> employees = new HashMap<>();

        //Load data from CVS file
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                double salary = Double.parseDouble(parts[3].trim());
                Integer managerId = parts.length > 4 && !parts[4].trim().isEmpty() ? Integer.parseInt(parts[4].trim()) : null;

                employees.put(id, new Employee(id, firstName, lastName, salary, managerId));
            }
        }

        for (Employee e : employees.values()) {
            if (e.getManagerId() != null) {
                employees.get(e.getManagerId()).getSubordinates().add(e);
            }
        }
        return employees;
    }

    public List<String> checkSalaryRules(Map<Integer, Employee> employees) {
        List<String> violations = new ArrayList<>();

        for (Employee manager : employees.values()) {
            if (manager.getSubordinates().isEmpty()) continue;

            double avgSubSalary = manager.getSubordinates().stream().mapToDouble(Employee::getSalary).average().orElse(0);

            double minAllowed = avgSubSalary * 1.20;
            double maxAllowed = avgSubSalary * 1.50;

            if (manager.getSalary() < minAllowed) {
                violations.add(manager.getFullName() + " earns LESS than allowed by " + String.format("%.2f", minAllowed - manager.getSalary()));
            } else if (manager.getSalary() > maxAllowed) {
                violations.add(manager.getFullName() + " earns MORE than allowed by " + String.format("%.2f", manager.getSalary() - maxAllowed));
            }
        }
        return violations;
    }

    public List<String> checkReportingLines(Map<Integer, Employee> employees) {
        List<String> tooLong = new ArrayList<>();
        employees.values().stream().filter(e -> e.getManagerId() == null).findFirst().orElseThrow(() -> new RuntimeException("No CEO found"));

        for (Employee e : employees.values()) {
            int distance = countManagers(e, employees);
            if (distance > 4) {
                tooLong.add(e.getFullName() + " reporting line too long by " + (distance - 4));
            }
        }
        return tooLong;
    }

    private int countManagers(Employee e, Map<Integer, Employee> employees) {
        int count = 0;
        Integer managerId = e.getManagerId();
        while (managerId != null) {
            count++;
            managerId = employees.get(managerId).getManagerId();
        }
        return count;
    }
}
