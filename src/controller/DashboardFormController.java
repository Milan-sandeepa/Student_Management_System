package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardFormController {
    public AnchorPane root;
    public JFXButton btnSave;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXTextField txtStudentId;
    public JFXTextField txtStudentName;
    public JFXTextField txtStudentAddress;
    public JFXTextField txtStudentContact;
    public TableView<Student> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;
    public JFXTextField txtStudentEmail;
    public JFXTextField txtStudentNic;

    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNic.setCellValueFactory(new PropertyValueFactory("nic"));

        try {
            loadAllStudents();
            tblStudent.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllStudents() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Student");
        ObservableList<Student> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Student(
                            result.getString("student_id"),
                            result.getString("student_name"),
                            result.getString("email"),
                            result.getString("contact"),
                            result.getString("address"),
                            result.getString("nic")
                    )
            );
        }
        tblStudent.setItems(obList);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        Student s = new Student(
                txtStudentId.getText(),txtStudentName.getText(),txtStudentEmail.getText(),txtStudentContact.getText(),txtStudentAddress.getText(),
                txtStudentNic.getText()
        );

        try {
            if (CrudUtil.execute("INSERT INTO Student VALUES (?,?,?,?,?,?)",s.getId(),s.getName(),s.getEmail(),s.getContact(),s.getAddress(),
            s.getNic())){
                tblStudent.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            loadAllStudents();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Student s = new Student(
                txtStudentId.getText(),txtStudentName.getText(),txtStudentEmail.getText(),txtStudentContact.getText(),txtStudentAddress.getText(),
                txtStudentNic.getText()
        );

        try{
            boolean isUpdated = CrudUtil.execute("UPDATE Student SET student_name=? , email=? ,contact=? ,address=? , nic=? WHERE student_id=?",s.getName(),s.getEmail(),s.getContact(),s.getAddress(),
                    s.getNic(),s.getId());
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }


        }catch (SQLException | ClassNotFoundException e){

        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try{
            if (CrudUtil.execute("DELETE FROM Student WHERE student_id=?",txtStudentId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {

    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result =  CrudUtil.execute("SELECT * FROM Student WHERE student_id=?",txtStudentId.getText());
            if (result.next()) {
                txtStudentName.setText(result.getString(2));
                txtStudentEmail.setText(result.getString(3));
                txtStudentContact.setText(result.getString(4));
                txtStudentAddress.setText(result.getString(5));
                txtStudentNic.setText(result.getString(6));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
