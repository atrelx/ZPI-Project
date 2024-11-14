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

        @Schema(description = "Dane użytkownika przypisane do pracownika",
                example = "{ \"userId\": \"118443418389427414498\", \"systemRole\": \"REGULAR\" }")
        UserDTO user,

        @Schema(description = "Dane kontaktowe osoby odpowiedzialnej za kontakt z pracownikiem",
                example = "{ \"contactPersonId\": \"e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed\", \"contactNumber\": \"+48123456789\", \"emailAddress\": \"contact@company.com\" }")
        ContactPersonDTO contactPerson,

        @Schema(description = "Dane osobowe pracownika",
                example = "{ \"personId\": \"1f3d4d5e-3c11-4e53-8f82-e2f0d8c94c89\", \"name\": \"Jan\", \"surname\": \"Kowalski\", \"dateOfBirth\": \"1985-04-23\", \"sex\": \"M\" }")
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



