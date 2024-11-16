package com.example.amoz.services

import com.example.amoz.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface EmployeeService {

    @POST("api/employees/acceptInvitation")
    suspend fun acceptInvitationToCompany(@Query("token") token: String): Response<MessageResponse>

    @POST("api/employees/invite")
    suspend fun inviteEmployeeToCompany(
        @Query("employeeEmail") employeeEmail: String
    ): Response<MessageResponse>

    @PATCH("api/employees/kick/{employeeId}")
    suspend fun kickEmployeeFromCompany(
        @Path("employeeId") employeeId: UUID
    ): Response<MessageResponse>

    @PATCH("api/employees/leave")
    suspend fun leaveCompany(): Response<MessageResponse>

    @GET("api/employees")
    suspend fun fetchEmployees(): Response<JsonElement>
}
