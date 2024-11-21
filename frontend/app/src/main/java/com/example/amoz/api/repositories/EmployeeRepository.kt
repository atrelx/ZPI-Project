package com.example.amoz.api.repositories

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.models.Employee
import com.example.amoz.api.services.EmployeeService
import com.example.amoz.extensions.toImageBitmap
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

    suspend fun fetchEmployees(): List<com.example.amoz.models.Employee> {
        return performRequest {
            employeeService.fetchEmployees()
        } ?: listOf()
    }

    suspend fun getEmployeePicture(employeeId: UUID): ImageBitmap? {
        return performRequest {
            employeeService.getEmployeePicture(employeeId)
        }.toImageBitmap()
    }
}
