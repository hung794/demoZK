package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.entity.Employee;
import org.example.entity.EmployeeDTO;
import org.example.entity.EmployeeSearch;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zsoup.helper.StringUtil;
import org.zkoss.zul.*;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.example.utils.ApiUtil.callApi;

public class WindowController extends Window {

    private Window window;
    private final String url = "http://localhost:8081/api/v1/employee";


    public void viewList() {
        String json = callApi(url + "/search", null, "POST").getEntity(String.class);
        Gson gson = new Gson();
        Type employeeType = new TypeToken<ArrayList<Employee>>() {
        }.getType();
        List<Employee> employeeList = gson.fromJson(json, employeeType);
        showList(employeeList);
    }

    public void saveEmployee(String id, String name, String address, String salary) {
        Gson gson = new Gson();
        double salaryNumber = 0.0;
        if (StringUtil.isNumeric(salary)) {
            salaryNumber = Double.parseDouble(salary);
        }
        EmployeeDTO dto = new EmployeeDTO(name, address, salaryNumber);
        String dtoJson = gson.toJson(dto);
        String urlFull;
        String message;
        if (id != null && !id.isEmpty()) {
            urlFull = url + "/update?id=" + id;
            message = "Update employee success!";
        } else {
            urlFull = url + "/create";
            message = "Create employee success";
        }
        String json = callApi(urlFull, dtoJson, "POST").getEntity(String.class);
        Employee employee = gson.fromJson(json, Employee.class);
        if (employee != null) {
            Messagebox.show(message);
            window.detach();
            viewList();
        }
        else {
            Messagebox.show("Error!");
        }
    }

    public void searchEmployee(String nameSearch, String addressSearch, String startSalarySearch, String endSalarySearch) {
        Gson gson = new Gson();
        EmployeeSearch search = new EmployeeSearch();
        search.setName(nameSearch);
        search.setAddress(addressSearch);
        if (startSalarySearch != null && !startSalarySearch.isEmpty()) {
            search.setStartSalary(Double.parseDouble(startSalarySearch));
        }
        if (endSalarySearch != null && !endSalarySearch.isEmpty()) {
            search.setEndSalary(Double.parseDouble(endSalarySearch));
        }
        String searchJson = gson.toJson(search);
        String json = callApi(url + "/search", searchJson, "POST").getEntity(String.class);
        Type employeeType = new TypeToken<ArrayList<Employee>>() {
        }.getType();
        List<Employee> employeeList = gson.fromJson(json, employeeType);
        showList(employeeList);
    }

    public void deleteEmployee(int id) {
        Messagebox.show("Are you sure to delete this employee?", "Delete Employee", Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION, evt -> {
                    if (Messagebox.ON_YES.equals(evt.getName())) {
                        String json = callApi(url + "/delete?id=" + id, null, "GET").getEntity(String.class);
                        Messagebox.show(json);
                        viewList();
                    }
                }
        );
    }

    private void showList(List<Employee> employeeList) {
        Listbox lb = (Listbox) this.getFellow("lstEmployee");
        lb.getItems().clear();
        for (Employee e : employeeList) {
            Listitem li = new Listitem();
            li.appendChild(new Listcell(String.format("%01d", e.getId())));
            li.appendChild(new Listcell(e.getName()));
            li.appendChild(new Listcell(e.getAddress()));
            li.appendChild(new Listcell(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                    .format(e.getSalary())));
            Button updateButton = new Button("Update");
            updateButton.setZclass("update-button z-button");
            updateButton.addEventListener(Events.ON_CLICK, event -> {
                openUpdateDialog(e);
            });
            Button deleteButton = new Button("Delete");
            deleteButton.setZclass("delete-button z-button");
            deleteButton.addEventListener(Events.ON_CLICK, event -> {
                deleteEmployee(e.getId());
            });
            Listcell cell = new Listcell();
            cell.appendChild(updateButton);
            cell.appendChild(deleteButton);
            li.appendChild(cell);
            lb.appendChild(li);
        }
    }

    public void openCreateDialog() {
        window = (Window) Executions.createComponents(
                "/modal.zul", null, null);
        window.setTitle("Create Employee");
        addRowToModal(null, null, null, null);
        window.doModal();
    }

    public void openUpdateDialog(Employee e) {
        window = (Window) Executions.createComponents(
                "/modal.zul", null, null);
        window.setTitle("Update Employee " + e.getId());
        addRowToModal(String.format("%01d", e.getId()), e.getName(), e.getAddress(), String.format(String.format("%.0f", e.getSalary())));
        window.doModal();
    }

    public void closeDialog() {
        window.detach();
    }

    private void addRowToModal(String id, String name, String address, String salary) {
        Rows rows = (Rows) window.getFellow("formRows");
        rows.getChildren().clear();
        Row rowId = new Row();
        rowId.setVisible(false);
        Textbox boxId = new Textbox();
        boxId.setVisible(false);
        boxId.setId("id");
        if (id != null) {
            boxId.setValue(id);
        }
        rowId.appendChild(boxId);
        rows.appendChild(rowId);
        Row rowName = new Row();
        rowName.appendChild(new Label("Employee Name:"));
        Textbox boxName = new Textbox();
        if (name != null && !name.isEmpty()) {
            boxName.setValue(name);
        }
        boxName.setId("name");
        boxName.setWidth("200px");
        rowName.appendChild(boxName);
        Row rowAddress = new Row();
        rowAddress.appendChild(new Label("Employee Address:"));
        Textbox boxAddress = new Textbox();
        if (address != null && !address.isEmpty()) {
            boxAddress.setValue(address);
        }
        boxAddress.setId("address");
        boxAddress.setWidth("200px");
        rowAddress.appendChild(boxAddress);
        Row rowSalary = new Row();
        rowSalary.appendChild(new Label("Employee Salary:"));
        Textbox boxSalary = new Textbox();
        if (salary != null && !salary.isEmpty()) {
            boxSalary.setValue(salary);
        }
        boxSalary.setId("salary");
        boxSalary.setWidth("200px");
        rowSalary.appendChild(boxSalary);
        rows.appendChild(rowName);
        rows.appendChild(rowAddress);
        rows.appendChild(rowSalary);
    }
}