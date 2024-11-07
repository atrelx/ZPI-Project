package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.Employee;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeDTO(
        UUID employeeId,
        UserDTO user,
        ContactPersonDTO contactPerson,
        PersonDTO person,
        RoleInCompany roleInCompany,
        LocalDate employmentDate
) {
    public static EmployeeDTO toEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getEmployeeId(),
                UserDTO.toUserDTO(employee.getUser()),
                ContactPersonDTO.toContactPersonDTO(employee.getContactPerson()),
                PersonDTO.toPersonDTO(employee.getPerson()),
                employee.getRoleInCompany(),
                employee.getEmploymentDate()
        );
    }
}

