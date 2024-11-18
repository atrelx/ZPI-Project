package com.example.amoz.api.repositories

import com.example.amoz.api.models.Employee
import com.example.amoz.api.services.EmployeeService
import java.util.UUID
import javax.inject.Inject

class EmployeeRepository @Inject constructor(
    private val employeeService: EmployeeService
) : BaseRepository() {

    suspend fun acceptInvitationToCompany(token: String) {
        performRequest {
            employeeService.acceptInvitationToCompany(token)
        }
    }

    suspend fun inviteEmployeeToCompany(employeeEmail: String) {
        performRequest {
            employeeService.inviteEmployeeToCompany(employeeEmail)
        }
    }

    suspend fun kickEmployeeFromCompany(employeeId: UUID) {
        performRequest {
            employeeService.kickEmployeeFromCompany(employeeId)
        }
    }

    suspend fun leaveCompany() {
        performRequest {
            employeeService.leaveCompany()
        }
    }

    suspend fun fetchEmployees(): List<Employee> {
        return performRequest {
            employeeService.fetchEmployees()
        } ?: listOf()
    }
}
