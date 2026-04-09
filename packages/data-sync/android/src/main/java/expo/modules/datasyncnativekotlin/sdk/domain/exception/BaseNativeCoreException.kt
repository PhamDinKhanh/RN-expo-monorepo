package expo.modules.datasyncnativekotlin.sdk.domain.exception

/**
 * Base class for all native core exceptions in the data sync module.
 * Stays framework-agnostic so domain and platform layers are not coupled to Expo.
 *
 * @property message The error message.
 * @property details Additional details related to the exception.
 */
open class BaseNativeCoreException(
    override val message: String,
    val className: String? = null,
    val methodName: String? = null,
    val details: Map<String, Any>? = null,
) : RuntimeException(message) {
    override fun toString(): String = "[$className.$methodName] $message | Details: $details"
}

object ExceptionUtils {
    fun getCallerInfo(): Pair<String, String> {
        // Lấy stack trace hiện tại
        val stackTrace = Thread.currentThread().stackTrace
        // Index 3 hoặc 4 thường là nơi gọi hàm throw (tùy vào cấu trúc wrap)
        val element = stackTrace.getOrNull(4)

        val fullClassName = element?.className?.substringAfterLast('.') ?: "UnknownClass"
        val methodName = element?.methodName ?: "UnknownMethod"

        return Pair(fullClassName, methodName)
    }
}

/**
 * Exception thrown when the current Activity screen was not found.
 */
class ActivityNotFoundException :
    BaseNativeCoreException(
        message = "The current Activity screen was not found. Please restart the application.",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )

/**
 * Exception thrown when this device does not support NFC hardware.
 */
class NfcNotSupportedException :
    BaseNativeCoreException(
        message = "This device does not support NFC hardware.",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )

/**
 * Exception thrown when NFC functionality is disabled on the device.
 */
class NfcDisabledCoreException :
    BaseNativeCoreException(
        message = "NFC is turned off on the device.",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )

/**
 * Exception thrown when an NFC reader operation times out.
 */
class NfcReaderTimeoutCoreException :
    BaseNativeCoreException(
        message = "NFC reader timeout.",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )

/**
 * Exception thrown when a database record with the specified [id] cannot be found.
 *
 * @param id The identifier of the missing record.
 */
class DbRecordNotFoundCoreException(
    id: String,
) : BaseNativeCoreException(
        message = "No data found matching ID: $id",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )

/**
 * Exception thrown when a database operation fails due to a constraint violation.
 *
 * @param detail Specific information about the constraint violation.
 */
class DbConstraintViolationCoreException(
    detail: String,
) : BaseNativeCoreException(
        message = "Duplicate data or constraint violation: $detail",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )

/**
 * Exception thrown when the device's storage is full, preventing database operations.
 */
class DbStorageFullCoreException :
    BaseNativeCoreException(
        message = "The tablet's memory is full.",
        className = ExceptionUtils.getCallerInfo().first,
        methodName = ExceptionUtils.getCallerInfo().second,
    )
