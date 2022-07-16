package utills.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    static ExtentReports extent = ExtentManager.createExtentReports();

    public static synchronized ExtentTest getTest(){
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName, String desc){
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static class ExtentManager {
        public static final ExtentReports extentReports = new ExtentReports();

        public synchronized static ExtentReports createExtentReports(){
            ExtentSparkReporter reporter = new ExtentSparkReporter("./extent-reports/extent-report.html");
            reporter.config().setReportName("Automation Report");
            reporter.config().setTheme(Theme.DARK);
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("Author", "Saurabh Kumar");
            return extentReports;
        }

    }
}
