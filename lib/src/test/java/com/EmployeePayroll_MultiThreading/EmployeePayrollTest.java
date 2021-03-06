package com.EmployeePayroll_MultiThreading;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.EmployeePayroll_MultiThreading.EmployeePayrollService.IOService;

public class EmployeePayrollTest {
	private static Logger log = Logger.getLogger(EmployeePayrollTest.class.getName());

	@Test
	public void given6Employees_WhenAddedToDB_ShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(1, "Jeff Bezos", "M", 100000.0, LocalDate.now()),
				new EmployeePayrollData(2, "Bill Gates", "M", 200000.0, LocalDate.now()),
				new EmployeePayrollData(3, "Mark Zuckerberg", "M", 300000.0, LocalDate.now()),
				new EmployeePayrollData(4, "Sunder Pichai", "M", 400000.0, LocalDate.now()),
				new EmployeePayrollData(5, "Mukesh", "M", 600000.0, LocalDate.now()),
				new EmployeePayrollData(6, "Anil", "M", 700000.0, LocalDate.now()) };
		// creating object and reading data
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		// It records event time-stamps in the application
		Instant start = Instant.now();
		employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmps)); // adding employee to the payroll
		Instant end = Instant.now();
		log.info("Duration without thread : " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayrollService.addEmployeeToPayrollWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		log.info("Duartion with Thread : " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(12, employeePayrollService.countEntries(IOService.DB_IO));
	}

	@Test
	public void givenNewSalariesForMultipleEmployee_WhenUpdated_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> employeeSalaryMap = new HashMap<>();
		Instant threadStart = Instant.now();
		employeeSalaryMap.put("Anil", 3000000.00);
		employeeSalaryMap.put("Mukesh", 2000000.00);
		employeeSalaryMap.put("Sunder Pichai", 5000000.00);
		employeePayrollService.updateSalaryOfMultipleEmployees(employeeSalaryMap);
		Instant threadEnd = Instant.now();
		log.info("Duartion with Thread : " + Duration.between(threadStart, threadEnd));
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mukesh");
		Assert.assertTrue(result);
	}
}