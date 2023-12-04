package swtp12.modulecrediting.model;

public interface Views {
    public class coursesWithModules {}
    public class modulesWithoutCourse {}

    public class ApplicationOverview {}
    public class ApplicationStudent extends ApplicationOverview{}

    public class ApplicationLogin extends ApplicationStudent {}

    public class RelatedModulesConnection {}

}
