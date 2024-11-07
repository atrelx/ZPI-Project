package com.zpi.amoz.responses;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.models.Person;
import com.zpi.amoz.models.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record EmployeeDetailsResponse(
        UUID employeeId,
        User user,
        ContactPerson contactPerson,
        Person person,
        RoleInCompany roleInCompany,
        Optional<LocalDate> employmentDate
) { }
