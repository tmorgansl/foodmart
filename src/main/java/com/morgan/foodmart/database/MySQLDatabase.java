package com.morgan.foodmart.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MySQLDatabase implements EmployeeDatabaseService {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final Connection connection;

  @Autowired
  public MySQLDatabase(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<String> getDepartments() {
    return executeQuerySingleStringColumn(Constants.departmentsQuery);
  }

  @Override
  public List<String> getPayTypes() {
    return executeQuerySingleStringColumn(Constants.payTypesQuery);
  }

  @Override
  public List<String> getEducation() {
    return executeQuerySingleStringColumn(Constants.educationLevelQuery);
  }

  @Override
  public List<EmployeeDetails> getEmployeeDetails(
      String department, String payType, String education) {

    List<EmployeeDetails> employeeDetails = new ArrayList<>();

    try (PreparedStatement stmt =
        this.connection.prepareStatement(Constants.employeeDetailsQuery)) {

      stmt.setString(1, payType);
      stmt.setString(2, department);
      stmt.setString(3, education);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          final EmployeeDetails employee =
              new EmployeeDetails(
                  rs.getString("full_name"),
                  rs.getString("position_title"),
                  rs.getDate("hire_date"),
                  rs.getDate("end_date"),
                  rs.getString("management_role"));

          employeeDetails.add(employee);
        }
        return employeeDetails;
      } catch (SQLException e) {
        final String message =
            String.format(
                "problem getting employee details with fields department: %s, paytype: %s, education: %s",
                department, payType, education);
        log.error(message);
        throw new EmployeeDatabaseException(message, e);
      }
    } catch (SQLException e) {
      final String message =
          String.format(
              "problem getting employee details with fields department: %s, paytype: %s, education: %s",
              department, payType, education);
      log.error(message);
      throw new EmployeeDatabaseException(message, e);
    }
  }

  @PreDestroy
  public void close() throws SQLException {
    this.connection.close();
  }

  private List<String> executeQuerySingleStringColumn(final String query) {
    try (Statement stmt = this.connection.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      List<String> stringColumn = new ArrayList<>();
      while (rs.next()) {
        stringColumn.add(rs.getString(1));
      }

      return stringColumn;
    } catch (SQLException e) {
      final String message = String.format("cannot perform query: %s", query);
      log.error(message);
      throw new EmployeeDatabaseException(message, e);
    }
  }
}
