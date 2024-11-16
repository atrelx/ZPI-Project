package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO reprezentujące dane pracownika, w tym dane użytkownika, dane kontaktowe, osobowe oraz rolę w firmie.")
public record EmployeeDTO(

        @Schema(description = "Identyfikator pracownika", example = "1c0b25d2-d77e-4f85-bd27-2672f72848d1")
        UUID employeeId,

        @Schema(description = "Dane użytkownika przypisane do pracownika", implementation = UserDTO.class)
        UserDTO user,

        @Schema(description = "Dane kontaktowe osoby odpowiedzialnej za kontakt z pracownikiem", implementation = ContactPersonDTO.class)
        ContactPersonDTO contactPerson,

        @Schema(description = "Dane osobowe pracownika", implementation = PersonDTO.class)
        PersonDTO person,

        @Schema(description = "Rola pracownika w firmie", example = "REGULAR")
        RoleInCompany roleInCompany,

        @Schema(description = "Data zatrudnienia pracownika", example = "2022-05-15")
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



