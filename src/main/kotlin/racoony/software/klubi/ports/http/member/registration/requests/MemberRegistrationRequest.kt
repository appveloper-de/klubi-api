package racoony.software.klubi.ports.http.member.registration.requests

import com.fasterxml.jackson.annotation.JsonInclude
import racoony.software.klubi.domain.member_registration.AccountOwner
import racoony.software.klubi.domain.member_registration.Address
import racoony.software.klubi.domain.member_registration.AssignedDepartment
import racoony.software.klubi.domain.member_registration.BankDetails
import racoony.software.klubi.domain.member_registration.Bic
import racoony.software.klubi.domain.member_registration.Contact
import racoony.software.klubi.domain.member_registration.Department
import racoony.software.klubi.domain.member_registration.EmailAddress
import racoony.software.klubi.domain.member_registration.IBAN
import racoony.software.klubi.domain.member_registration.MemberStatus
import racoony.software.klubi.domain.member_registration.Name
import racoony.software.klubi.domain.member_registration.PhoneNumber
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
class MemberRegistrationRequest(
    private val firstName: String,
    private val lastName: String,
    private val streetAddress: String,
    private val streetNumber: String,
    private val postalCode: String,
    private val city: String,
    val birthday: LocalDate,
    private val phone: String?,
    private val email: String?,
    private val department: String,
    private val entryDate: LocalDate,
    private val memberStatus: String,
    private val accountOwnerFirstName: String?,
    private val accountOwnerLastName: String?,
    private val iban: String?,
    private val bic: Bic?,
    val paymentMethod: String
) {
    private fun email(): EmailAddress? = this.email?.let { EmailAddress(it) }
    private fun phone(): PhoneNumber? = this.phone?.let { PhoneNumber(it) }

    val address: Address = Address(
        this.streetAddress,
        this.streetNumber,
        this.postalCode,
        this.city
    )

    val name: Name = Name(this.firstName, this.lastName)

    val contact: Contact = Contact(phone(), email())

    val bankDetails: BankDetails?
        get() {
            if (this.accountOwnerFirstName != null && this.accountOwnerLastName != null && this.iban != null) {
                return BankDetails(
                    AccountOwner(this.accountOwnerFirstName, this.accountOwnerLastName),
                    IBAN(this.iban),
                    this.bic
                )
            }
            return null
        }

    private val accountOwner: AccountOwner?
        get() {
            if (this.accountOwnerLastName != null && this.accountOwnerFirstName != null) {
                return AccountOwner(this.accountOwnerFirstName, this.accountOwnerLastName)
            }
            return null
        }

    val assignedDepartment: AssignedDepartment = AssignedDepartment(
        Department(this.department),
        MemberStatus.valueOf(this.memberStatus.toUpperCase()),
        this.entryDate
    )
}
