package swtp12.modulecrediting.util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class LogUtil {

    public static void printLog(String stringToPrint) {
        System.out.println(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS) + "  " + stringToPrint);
    }

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
            case MOVED_TO_CHAIRMAN:
                typeString = ApplicationType.MOVED_TO_CHAIRMAN.toString();
                break;
            case REAPPLIED:
                typeString = ApplicationType.REAPPLIED.toString();
                break;
            
            default:
                logString = "printApplicationLog was called with insufficient infomation";
                break;
        }
        String log = "Type: " + typeString + " - Application: " + id + logString;
        printLog(log);
    }

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
    
    public enum CourseType {
        ADDED,
        CREATED,
        DEACTIVATED,
        DELETED,
        REACTIVATED,
        REMOVED,
        RENAMED,
    }

    public enum ModuleType {
        CREATED, 
        DEACTIVATED,
        DELETED, 
        FOUND,
        REACTIVATED, 
        UPDATED, 
    }

    public enum ApplicationType {
        CREATED,
        FINISHED,
        FORMAL_REJECTION,
        MOVED_TO_CHAIRMAN,
        REAPPLIED,
    }

    public enum UserType {
        CREATED, 
        DELETED, 
        RENAMED,
        ROLE_CHANGED,
    }
}
