package com.inmeta.champs.web;

import com.inmeta.champs.model.Activity;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guri Lunnan
 */

@Controller
public class ExportController extends BaseController {

    @RequestMapping("/admin/saveData.htm")
    public ModelAndView dataHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            int fromYear = Integer.parseInt(request.getParameter("FromYear"));
            int toYear = Integer.parseInt(request.getParameter("ToYear"));
            int[] years = new int[toYear - fromYear + 1];
            for (int i = 0; i < years.length; i++) {
                years[i] = fromYear + i;
            }

            String fromMonth = request.getParameter("FromMonth");
            String toMonth = request.getParameter("ToMonth");
            List<String> fromMonthList = new ArrayList<String>();
            List<String> toMonthList = new ArrayList<String>();
            List<String> from_toMonthList = new ArrayList<String>();
            int fromMonthInt = 0;
            int toMonthInt = 0;
            String filename = "backup_" + fromMonth + fromYear + "_" + toMonth + toYear;

            String[] months = activityRepository.findReverseMonthList();

            for (int i = 0; i < months.length; i++) {
                if (months[i].equalsIgnoreCase(toMonth)) {
                    toMonthInt = i;
                    for (int j = i; j < months.length; j++) {
                        toMonthList.add(months[j]);
                    }
                }
            }

            for (int i = months.length - 1; i >= 0; i--) {
                if (months[i].equalsIgnoreCase(fromMonth)) {
                    fromMonthInt = i;
                    for (int j = 0; j <= i; j++) {
                        fromMonthList.add(months[j]);
                    }
                }
            }

            for (int i = toMonthInt; i <= fromMonthInt; i++) {
                from_toMonthList.add(months[i]);
            }


            HSSFWorkbook hwb = new HSSFWorkbook();

            if (request.getParameter("EmployeeName").equals("All data - månedsview")) {
                for (int i = years.length - 1; i >= 0; i--) {
                    if ((years.length - 1) == 0) {
                        for (String month : from_toMonthList) {
                            writeSheet(hwb, years[i], month);
                        }
                    } else if (i == years.length - 1 && i > 0) {
                        for (String month : toMonthList) {
                            writeSheet(hwb, years[i], month);
                        }
                    } else if (i > 0 && i != years.length - 1) {
                        for (String month : months) {
                            writeSheet(hwb, years[i], month);
                        }
                    } else if (i == 0 && i != years.length - 1) {
                        for (String month : fromMonthList) {
                            writeSheet(hwb, years[i], month);
                        }
                    }
                }
            } else if (request.getParameter("EmployeeName").equals("Alle ansatte")) {
                filename = filename + "_" + "allEmployees";
                List<Employee> employees = activityRepository.findEmployees();
                for (Employee employee : employees) {
                    String name = employee.getName();
                    writeEmployeeSheet(hwb, name, fromYear, toYear, fromMonth, toMonth);
                }

            } else {
                String name = request.getParameter("EmployeeName");
                filename = filename + "_" + name;
                writeEmployeeSheet(hwb, name, fromYear, toYear, fromMonth, toMonth);

            }


            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "inline; filename=" + filename);
            OutputStream os = response.getOutputStream();

            hwb.write(os);
            os.flush();
            os.close();
            return new ModelAndView("admin/home");
        } else return new ModelAndView("permissionDenied");
    }

    private void writeEmployeeSheet(HSSFWorkbook hwb, String employeeName, int fromYear, int toYear, String fromMonth, String toMonth) {
        List<String> fromMonthList = new ArrayList<String>();
        List<String> toMonthList = new ArrayList<String>();
        List<String> from_toMonthList = new ArrayList<String>();
        int fromMonthInt = 0;
        int toMonthInt = 0;

        String[] months = activityRepository.findReverseMonthList();

        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(toMonth)) {
                toMonthInt = i;
                for (int j = i; j < months.length; j++) {
                    toMonthList.add(months[j]);
                }
            }
        }

        for (int i = months.length - 1; i >= 0; i--) {
            if (months[i].equalsIgnoreCase(fromMonth)) {
                fromMonthInt = i;
                for (int j = 0; j <= i; j++) {
                    fromMonthList.add(months[j]);
                }
            }
        }

        for (int i = toMonthInt; i <= fromMonthInt; i++) {
            from_toMonthList.add(months[i]);
        }

        int[] years = new int[toYear - fromYear + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = fromYear + i;
        }

        HSSFSheet sheet = hwb.createSheet(employeeName);
        sheet.setDefaultColumnWidth((short) 20);
        HSSFCellStyle headerStyle = hwb.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setWrapText(true);

        HSSFCellStyle cellStyle = hwb.createCellStyle();
        cellStyle.setWrapText(true);

        HSSFRow rowhead = sheet.createRow((short) 0);
        HSSFRichTextString firstCategory = new HSSFRichTextString("Tidsrom");
        List<ActivityType> activityTypes = activityRepository.findActivityTypes();
        HSSFCell colHeading1 = rowhead.createCell((short) 0);
        colHeading1.setCellStyle(headerStyle);
        colHeading1.setCellValue(firstCategory);
        short i = 1;
        for (ActivityType a : activityTypes) {
            HSSFRichTextString category = new HSSFRichTextString(a.getCategory());
            HSSFCell colHeading = rowhead.createCell(i);
            colHeading.setCellStyle(headerStyle);
            colHeading.setCellValue(category);
            i++;
        }

        i = 1;
        for (int j = years.length - 1; j >= 0; j--) {
            if ((years.length - 1) == 0) {
                for (String month : from_toMonthList) {
                    List<Activity> activities = activityRepository.findActivities(years[j], month);
                    HSSFRow row = sheet.createRow(i);
                    short k = 0;
                    HSSFRichTextString month_year = new HSSFRichTextString(month + " " + years[j]);
                    HSSFCell cell = row.createCell(k);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(month_year);
                    k++;
                    for (ActivityType a : activityTypes) {
                        HSSFRichTextString activityName = new HSSFRichTextString("");
                        for (Activity activity : activities) {
                            if (employeeName.equals(activity.getEmployee().getName()) && a.getCategory().equals(activity.getActivityType().getCategory())) {
                                String actName = activity.getActivityType().getActivityName();
                                String temp = activityName.getString();
                                if (temp != "")
                                    temp = temp + "\n";
                                activityName = new HSSFRichTextString(temp + actName);
                            }
                        }
                        HSSFCell cell2 = row.createCell(k);
                        cell2.setCellStyle(cellStyle);
                        cell2.setCellValue(activityName);
                        k++;
                    }
                    i++;
                }
            } else if (j == years.length - 1 && j > 0) {
                for (String month : toMonthList) {
                    List<Activity> activities = activityRepository.findActivities(years[j], month);
                    HSSFRow row = sheet.createRow(i);
                    short k = 0;
                    HSSFRichTextString month_year = new HSSFRichTextString(month + "_" + years[j]);
                    HSSFCell cell = row.createCell(k);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(month_year);
                    k++;
                    for (ActivityType a : activityTypes) {
                        HSSFRichTextString activityName = new HSSFRichTextString("");
                        for (Activity activity : activities) {
                            if (employeeName.equals(activity.getEmployee().getName()) && a.getCategory().equals(activity.getActivityType().getCategory())) {
                                String actName = activity.getActivityType().getActivityName();
                                String temp = activityName.getString();
                                if (temp != "")
                                    temp = temp + "\n";
                                activityName = new HSSFRichTextString(temp + actName);
                            }
                        }
                        HSSFCell cell2 = row.createCell(k);
                        cell2.setCellStyle(cellStyle);
                        cell2.setCellValue(activityName);
                        k++;
                    }
                    i++;
                }
            } else if (i > 0 && i != years.length - 1) {
                for (String month : months) {
                    List<Activity> activities = activityRepository.findActivities(years[j], month);
                    HSSFRow row = sheet.createRow(i);
                    short k = 0;
                    HSSFRichTextString month_year = new HSSFRichTextString(month + "_" + years[j]);
                    HSSFCell cell = row.createCell(k);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(month_year);
                    k++;
                    for (ActivityType a : activityTypes) {
                        HSSFRichTextString activityName = new HSSFRichTextString("");
                        for (Activity activity : activities) {
                            if (employeeName.equals(activity.getEmployee().getName()) && a.getCategory().equals(activity.getActivityType().getCategory())) {
                                String actName = activity.getActivityType().getActivityName();
                                String temp = activityName.getString();
                                if (temp != "")
                                    temp = temp + "\n";
                                activityName = new HSSFRichTextString(temp + actName);
                            }
                        }
                        HSSFCell cell2 = row.createCell(k);
                        cell2.setCellStyle(cellStyle);
                        cell2.setCellValue(activityName);
                        k++;
                    }
                    i++;
                }
            } else if (i == 0 && i != years.length - 1) {
                for (String month : fromMonthList) {
                    List<Activity> activities = activityRepository.findActivities(years[j], month);
                    HSSFRow row = sheet.createRow(i);
                    short k = 0;
                    HSSFRichTextString month_year = new HSSFRichTextString(month + "_" + years[j]);
                    HSSFCell cell = row.createCell(k);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(month_year);
                    k++;
                    for (ActivityType a : activityTypes) {
                        HSSFRichTextString activityName = new HSSFRichTextString("");
                        for (Activity activity : activities) {
                            if (employeeName.equals(activity.getEmployee().getName()) && a.getCategory().equals(activity.getActivityType().getCategory())) {
                                String actName = activity.getActivityType().getActivityName();
                                String temp = activityName.getString();
                                if (temp != "")
                                    temp = temp + "\n";
                                activityName = new HSSFRichTextString(temp + actName);
                            }
                        }
                        HSSFCell cell2 = row.createCell(k);
                        cell2.setCellStyle(cellStyle);
                        cell2.setCellValue(activityName);
                        k++;
                    }
                    i++;
                }
            }
        }
    }

    private void writeSheet(HSSFWorkbook hwb, int year, String month) {
        HSSFSheet sheet = hwb.createSheet(month + "_" + year);
        sheet.setDefaultColumnWidth((short) 20);
        HSSFCellStyle headerStyle = hwb.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        headerStyle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);
        headerStyle.setWrapText(true);

        HSSFCellStyle cellStyle = hwb.createCellStyle();
        cellStyle.setWrapText(true);

        HSSFRow rowhead = sheet.createRow((short) 0);
        HSSFRichTextString firstCategory = new HSSFRichTextString("Employee");

        List<ActivityType> activityTypes = activityRepository.findActivityTypes();
        HSSFCell colHeading1 = rowhead.createCell((short) 0);
        colHeading1.setCellStyle(headerStyle);
        colHeading1.setCellValue(firstCategory);

        short i = 1;
        for (ActivityType a : activityTypes) {
            HSSFRichTextString category = new HSSFRichTextString(a.getCategory());
            HSSFCell colHeading = rowhead.createCell(i);
            colHeading.setCellStyle(headerStyle);
            colHeading.setCellValue(category);
            i++;
        }

        List<Employee> employees = activityRepository.findEmployees();
        List<Activity> activities = activityRepository.findActivities(year, month);

        i = 1;
        for (Employee e : employees) {
            HSSFRow row = sheet.createRow(i);
            short j = 0;
            HSSFRichTextString empName = new HSSFRichTextString(e.getName());
            HSSFCell cell = row.createCell(j);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(empName);
            j++;
            for (ActivityType a : activityTypes) {
                HSSFRichTextString activityName = new HSSFRichTextString("");
                for (Activity activity : activities) {
                    if (e.getName().equals(activity.getEmployee().getName()) && a.getCategory().equals(activity.getActivityType().getCategory())) {
                        String actName = activity.getActivityType().getActivityName();
                        String temp = activityName.getString();
                        if (temp != "")
                            temp = temp + "\n";
                        activityName = new HSSFRichTextString(temp + actName);
                    }
                }
                HSSFCell cell2 = row.createCell(j);
                cell2.setCellStyle(cellStyle);
                cell2.setCellValue(activityName);
                j++;
            }
            i++;
        }
    }

}
