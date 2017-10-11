package gov.va.ascent.framework.audit;

import gov.va.ascent.framework.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The Class AuditLogger.
 */
public class AuditLogger {

     static final Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);

    /**
     * Debug.
     *
     * @param auditable the auditable
     * @param activityDetail the activity detail
     */
    public static void debug(Auditable auditable, String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.debug(activityDetail);
        MDC.clear();
    }

    /**
     * Info.
     *
     * @param auditable the auditable
     * @param activityDetail the activity detail
     */
    public static void info(Auditable auditable, String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.info(activityDetail);
        MDC.clear();

    }

    /**
     * Warn.
     *
     * @param auditable the auditable
     * @param activityDetail the activity detail
     */
    public static void warn(Auditable auditable, String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.warn(activityDetail);
        MDC.clear();

    }
    
    /**
     * Error.
     *
     * @param auditable the auditable
     * @param activityDetail the activity detail
     */
    public static void error(Auditable auditable, String activityDetail) {
        addMdcSecurityEntries(auditable);
        LOGGER.error(activityDetail);
        MDC.clear();

    }

    /**
     * Adds the MDC security entries.
     *
     * @param auditable the auditable
     */
    private static void addMdcSecurityEntries(Auditable auditable) {
    	MDC.put("logType", "auditlogs");
        MDC.put("activity", auditable.activity());
        MDC.put("event", auditable.event().name());
        MDC.put("audit_class", auditable.auditClass());
        if(SecurityUtils.getPersonTraits() != null) {
            MDC.put("user", SecurityUtils.getPersonTraits().getUser());
            MDC.put("tokenId", SecurityUtils.getPersonTraits().getTokenId());
        }
    }

}
