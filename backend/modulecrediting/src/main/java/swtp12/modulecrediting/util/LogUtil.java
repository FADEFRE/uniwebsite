package swtp12.modulecrediting.util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

/**
 * This {@link Component} includes functions to print Logs with System.out.print with a timestamp.
 */
@Component
public class LogUtil {

    /**
     * This function is the basic printLog function and will just System.out.println the given {@link String} with timestamp.
     * @param stringToPrint {@code String}
     */
    public static void printLog(String stringToPrint) {
        System.out.println(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS) + "  " + stringToPrint);
    }

    /**
     * This function will create a {@link String stringToPrint} with more infomation about which method was called and then calls {@link #printLog( stringToPrint )}.
     * @param type {@link CourseType} Which method was this Log called on 
     * @param courseName {@code String}
     * @param optionalName {@code String}
     */
    public static void printCourseLog(CourseType type, String courseName, String optionalName) {
        String typeString = "";
        String logString = "";
        switch (type) {
            case ADDED:
                typeString = CourseType.ADDED.toString();
                logString = " - Module: " + optionalName;
                break;
            case CREATED:
                typeString = CourseType.CREATED.toString();
                //no more log needed
                break;
            case DEACTIVATED:
                typeString = CourseType.DEACTIVATED.toString();
                //no more log needed
                break;
            case DELETED:
                typeString = CourseType.DELETED.toString();
                //no more log needed
                break;
            case REACTIVATED:
                typeString = CourseType.REACTIVATED.toString();
                //no more log needed
                break;
            case REMOVED:
                typeString = CourseType.REMOVED.toString();
                logString = " - Module: " + optionalName;
                break;
            case RENAMED:
                typeString = CourseType.RENAMED.toString();
                logString = " => " + optionalName;
                break;
            
            default:
                logString = "printCourseLog was called with insufficient infomation";
                break;
        }
        String log = "Type: " + typeString + " - Course: " + courseName + logString;
        printLog(log);
    }

    /**
     * This function will create a {@link String stringToPrint} with more infomation about which method was called and then calls {@link #printLog( stringToPrint )}.
     * @param type {@link ModuleType} Which method was this Log called on 
     * @param moduleName {@code String}
     * @param code {@code String}
     * @param optionalName {@code String}
     * @param optionalCode {@code String}
     */
    public static void printModuleLog(ModuleType type, String moduleName, String code, String optionalName, String optionalCode) {
        String typeString = "";
        String logString = "";
        String codeString = "";
        if (code != null && !code.isBlank()) { codeString = " - Code: " + code; }
        switch (type) {
            case CREATED:
                typeString = ModuleType.CREATED.toString();
                logString = codeString;
                break;
            case DEACTIVATED:
                typeString = ModuleType.DEACTIVATED.toString();
                logString = codeString;
                break;
            case DELETED:
                typeString = ModuleType.DELETED.toString();
                logString = codeString;
                break;
            case FOUND:
                typeString = ModuleType.FOUND.toString();
                logString = codeString;
                break;
            case REACTIVATED:
                typeString = ModuleType.REACTIVATED.toString();
                logString = codeString;
                break;
            case UPDATED:
                typeString = ModuleType.UPDATED.toString();
                logString = " => " + optionalName + getLogStringForCode(code, optionalCode);
                break;
            
            default:
                logString = "printModuleLog was called with insufficient infomation";
                break;
        }
        String log = "Type: " + typeString + " - Module: " + moduleName + logString;
        printLog(log);
    }

    /**
     * This function will create a {@link String stringToPrint} with more infomation about which method was called and then calls {@link #printLog( stringToPrint )}.
     * @param type {@link ApplicationType} Which method was this Log called on 
     * @param id {@code String}
     */
    public static void printApplicationLog(ApplicationType type, String id) {
        String typeString = "";
        String logString = "";
        switch (type) {
            case CREATED:
                typeString = ApplicationType.CREATED.toString();
                break;
            case FINISHED:
                typeString = ApplicationType.FINISHED.toString();
                break;
            case FORMAL_REJECTION:
                typeString = ApplicationType.FORMAL_REJECTION.toString();
                break;
            case PASSED_ON:
                typeString = ApplicationType.PASSED_ON.toString();
                break;
            case RESUBMIT:
                typeString = ApplicationType.RESUBMIT.toString();
                break;
            
            default:
                logString = "printApplicationLog was called with insufficient infomation";
                break;
        }
        String log = "Type: " + typeString + " - Application: " + id + logString;
        printLog(log);
    }

    /**
     * This function will create a {@link String stringToPrint} with more infomation about which method was called and then calls {@link #printLog( stringToPrint )}.
     * @param type {@link UserType} Which method was this Log called on 
     * @param username {@code String}
     * @param role {@code String}
     * @param optionalName {@code String}
     * @param optionalRole {@code String}
     */
    public static void printUserLog(UserType type, String username, String role, String optionalName, String optionalRole) {
        String typeString = "";
        String logString = "";
        String roleString = "";
        if (role != null && !role.isBlank()) { roleString = " - Role: " + role; }
        switch (type) {
            case CREATED:
                typeString = UserType.CREATED.toString();
                logString = roleString;
                break;
            case DELETED:
                typeString = UserType.DELETED.toString();
                logString = roleString;
                break;
            case RENAMED:
                typeString = UserType.RENAMED.toString();
                logString = " => " + optionalName;
                break;
            case ROLE_CHANGED:
                typeString = UserType.ROLE_CHANGED.toString();
                logString = roleString + " => " + optionalRole;
                break;
            
            default:
                logString = "printUserLog was called with insufficient infomation";
                break;
        }
        String log = "Type: " + typeString + " - User: " + username + logString;
        printLog(log);
    }

    /**
     * This function will create a {@link String stringToPrint} with more infomation about the error and then calls {@link #printLog( stringToPrint )}.
     * @param location {@code String}
     * @param errorToPrint {@code String}
     * @param ex {@code Exception}
     */
    public static void printErrorLog(String location, String errorToPrint, Exception ex) {
        String locationString = "";
        String stackTraceString = "";
        if (location != null && !location.isBlank()) {
            if (ex != null && ex.getStackTrace() != null) {
                for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
                    stackTraceString = stackTraceString + "StackTrace: " + stackTraceElement.toString() + "\n";
                }
            }
            else {
                locationString = "in " + location + ": \n    ";
            }
            
        }
        else if (ex != null && ex.getStackTrace() != null) {
            for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
                stackTraceString = stackTraceString + "StackTrace: " + stackTraceElement.toString() + "\n";
            }
        }
        printLog("ERROR: " + locationString + errorToPrint + stackTraceString);
    }


    /**
     * This helper function will create a {@link String stringToPrint} with more infomation about which code was replace with which newCode for {@link #printModuleLog()}.
     * @param code {@code String}
     * @param newCode {@code String}
     */
    private static String getLogStringForCode(String code, String newCode) {
        String codeLogString = "";
        if (code == null || code.isBlank()) { 
            if (newCode == null || newCode.isBlank()) {
                codeLogString = " - Code: " + "No Code" + " => " + "No Code"; 
            }
            else {
                codeLogString = " - Code: " + "No Code" + " => " + newCode; 
            }
        }
        else {
            if (newCode == null || newCode.isBlank()) {
                codeLogString = " - Code: " + code + " => " + "No Code";
            }
            else {
                codeLogString = " - Code: " + code + " => " + newCode; 
            }
        }
        return codeLogString;
    }
    
    /**
     * Type of action performed related to Course
     */
    public enum CourseType {
        ADDED,
        CREATED,
        DEACTIVATED,
        DELETED,
        REACTIVATED,
        REMOVED,
        RENAMED,
    }

    /**
     * Type of action performed related to Module
     */
    public enum ModuleType {
        CREATED, 
        DEACTIVATED,
        DELETED, 
        FOUND,
        REACTIVATED, 
        UPDATED, 
    }

    /**
     * Type of action performed related to Application
     */
    public enum ApplicationType {
        CREATED,
        FINISHED,
        FORMAL_REJECTION,
        PASSED_ON,
        RESUBMIT,
    }

    /**
     * Type of action performed related to User
     */
    public enum UserType {
        CREATED, 
        DELETED, 
        RENAMED,
        ROLE_CHANGED,
    }
}
