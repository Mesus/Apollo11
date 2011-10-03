package springapp.web;

import com.gurilunnan.champs.model.*;
import com.gurilunnan.champs.persistence.ActivityRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import sun.text.normalizer.IntTrie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 29.09.11
 * Time: 09:06
 * To change this template use File | Settings | File Templates.
 */
public class ActivityListController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActivityRepository activityRepository = new ActivityRepository();
        List<ActivityResult> activityResults;
        List<ActivityResult> resultList = new ArrayList<ActivityResult>();
        List<Employee> employeeList;
        List<ActivityType> activityTypeList;
        ModelAndView modelAndView = new ModelAndView("activityList");
        boolean isVisible = true;
        String category;

        int count;

        int year = Integer.parseInt(request.getParameter("Year"));

        employeeList = activityRepository.findEmployees();
        activityTypeList = activityRepository.findActivityTypes(isVisible);
        activityResults = activityRepository.findActivityResults(year);

        for (Employee employee : employeeList) {
            for (ActivityType activityType : activityTypeList) {
                if (activityType.isVisible()) {
                    count = 0;
                    for (ActivityResult a : activityResults) {
                        if (employee.getName().equals(a.getEmployee().getName()) && activityType.getCategory().equals(a.getActivityType().getCategory())) {
                            if (activityType.isNumeric()) {
                                count = count + Integer.parseInt(a.getActivityType().getActivityName());
                            } else {
                                count = count + a.getCount();
                            }
                        }
                    }

                    ActivityResult activityResult = new ActivityResult();
                    activityResult.setCount(count);
                    activityResult.setActivityType(activityType);
                    activityResult.setEmployee(employee);
                    activityResult.setYear(year);
                    resultList.add(activityResult);

                }
            }

        }
        for (ActivityResult h : resultList) {
            System.out.println(h.getEmployee().getName() + h.getActivityType().getCategory() + h.getCount());
        }
        category = request.getParameter("Category");
        if (category == null) {
            category = "Konsulent";
        }
        employeeList = sortResult(resultList, category);
        modelAndView.addObject(activityTypeList);
        modelAndView.addObject(employeeList);
        modelAndView.addObject("resultList", resultList);
        modelAndView.addObject("Year", year);
        return modelAndView;
    }

    public List<Employee> sortResult(List<ActivityResult> unsortedList, String category) throws IOException, ServletException {
        ActivityRepository activityRepository = new ActivityRepository();
        List<Employee> employeeList = activityRepository.findEmployees();
        List<ActivityResult> temp = new ArrayList<ActivityResult>();
        List<Employee> result = new ArrayList<Employee>();

        if (category.equals("Konsulent")) {
            return employeeList;
        }
        for (ActivityResult a : unsortedList) {
            if (a.getActivityType().getCategory().equals(category)) {

                temp.add(a);
            }
        }
        Collections.sort(temp);
        for (ActivityResult activityResult : temp) {
            result.add(activityResult.getEmployee());
        }

        return result;
    }
}
