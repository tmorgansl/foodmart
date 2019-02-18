package com.morgan.foodmart.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeDatabaseServiceTest {
  private static Connection connection;
  private static EmployeeDatabaseService employeeDatabaseService;
  private static final List<EmployeeDetails> employeeDetails =
      Arrays.asList(
          new EmployeeDetails(
              "test employee 1", "software engineer", parseDate("01-02-2015"), null, "head office"),
          new EmployeeDetails(
              "test employee 2",
              "senior software engineer",
              parseDate("04-07-2018"),
              parseDate("01-01-2019"),
              "warehouse"));

  private static final String department = "marketing";
  private static final String payType = "monthly";
  private static final String educationLevel = "graduate";

  @Before
  public void init() {
    connection = mock(Connection.class);
    employeeDatabaseService = new MySQLDatabase(connection);
  }

  @Test
  public void testGetDepartmentsReturnsExpectedDepartments() throws SQLException {
    final List<String> expectedDepartments = Arrays.asList("human resources", "marketing");

    buildMockStatementQuery(Constants.departmentsQuery, expectedDepartments);

    final List<String> departments = employeeDatabaseService.getDepartments();

    assertEquals(expectedDepartments, departments);
  }

  @Test(expected = EmployeeDatabaseException.class)
  public void testGetDepartmentsSQLExceptionThrowsEmployeeDatabaseException() throws SQLException {
    buildMockSQLExceptionStatementQuery(Constants.departmentsQuery);

    employeeDatabaseService.getDepartments();
  }

  @Test
  public void testGetPayTypesReturnsExpectedPayTypes() throws SQLException {
    final List<String> expectedPayTypes = Arrays.asList("hourly", "monthly");

    buildMockStatementQuery(Constants.payTypesQuery, expectedPayTypes);

    final List<String> payTypes = employeeDatabaseService.getPayTypes();

    assertEquals(expectedPayTypes, payTypes);
  }

  @Test(expected = EmployeeDatabaseException.class)
  public void testGetPayTypesSQLExceptionThrowsEmployeeDatabaseException() throws SQLException {
    buildMockSQLExceptionStatementQuery(Constants.payTypesQuery);

    employeeDatabaseService.getPayTypes();
  }

  @Test
  public void testGetEducationLevelReturnsExpectedEducationLevels() throws SQLException {
    final List<String> expectedEducationLevels = Arrays.asList("graduate", "masters");

    buildMockStatementQuery(Constants.educationLevelQuery, expectedEducationLevels);

    final List<String> educationLevel = employeeDatabaseService.getEducation();

    assertEquals(expectedEducationLevels, educationLevel);
  }

  @Test(expected = EmployeeDatabaseException.class)
  public void testGetEducationLevelSQLExceptionThrowsEmployeeDatabaseException()
      throws SQLException {
    buildMockSQLExceptionStatementQuery(Constants.educationLevelQuery);

    employeeDatabaseService.getEducation();
  }

  @Test
  public void testGetEmployeeDetailsReturnsExpectedResult() throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);

    PreparedStatement statement = mock(PreparedStatement.class);

    when(statement.executeQuery()).thenReturn(resultSet);
    when(connection.prepareStatement(Constants.employeeDetailsQuery)).thenReturn(statement);

    OngoingStubbing<Boolean> stubNext = when(resultSet.next());

    for (EmployeeDetails ignored : employeeDetails) {
      stubNext = stubNext.thenReturn(true);
    }

    stubNext.thenReturn(false);

    // TODO: This should be refactored for readability and loop employeeDetails list
    // Not easy to do with the mockito interface
    when(resultSet.getString(Constants.managementRoleColumn))
        .thenReturn(employeeDetails.get(0).getManagementRole())
        .thenReturn(employeeDetails.get(1).getManagementRole());
    when(resultSet.getString(Constants.fullNameColumn))
        .thenReturn(employeeDetails.get(0).getFullName())
        .thenReturn(employeeDetails.get(1).getFullName());
    when(resultSet.getString(Constants.positionTitleColumn))
        .thenReturn(employeeDetails.get(0).getPositionTitle())
        .thenReturn(employeeDetails.get(1).getPositionTitle());
    when(resultSet.getDate(Constants.hireDataColumn))
        .thenReturn(new java.sql.Date(employeeDetails.get(0).getHireDate().getTime()))
        .thenReturn(new java.sql.Date(employeeDetails.get(1).getHireDate().getTime()));
    when(resultSet.getDate(Constants.endDateColumn))
        .thenReturn(null)
        .thenReturn(new java.sql.Date(employeeDetails.get(1).getEndDate().getTime()));

    final List<EmployeeDetails> actualEmployeeDetails =
        employeeDatabaseService.getEmployeeDetails(department, payType, educationLevel);

    assertEquals(employeeDetails, actualEmployeeDetails);

    final List<String> inputs = Arrays.asList(payType, department, educationLevel);
    for (int i = 0; i < inputs.size(); i++) {
      verify(statement).setString(i + 1, inputs.get(i));
    }
  }

  @Test(expected = EmployeeDatabaseException.class)
  public void testGetEmployeeDetailsSQLExceptionThrowsEmployeeDatabaseException()
      throws SQLException {
    PreparedStatement statement = mock(PreparedStatement.class);

    when(statement.executeQuery()).thenThrow(new SQLException());
    when(connection.prepareStatement(Constants.employeeDetailsQuery)).thenReturn(statement);

    employeeDatabaseService.getEmployeeDetails(department, payType, educationLevel);
  }

  private void buildMockSQLExceptionStatementQuery(final String query) throws SQLException {
    Statement statement = mock(Statement.class);

    when(statement.executeQuery(query)).thenThrow(new SQLException());
    when(connection.createStatement()).thenReturn(statement);
  }

  private void buildMockStatementQuery(final String query, final List<String> output)
      throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);

    OngoingStubbing<Boolean> stubNext = when(resultSet.next());

    for (String ignored : output) {
      stubNext = stubNext.thenReturn(true);
    }

    stubNext.thenReturn(false);

    OngoingStubbing<String> stubGetString = when(resultSet.getString(1));

    for (String s : output) {
      stubGetString = stubGetString.thenReturn(s);
    }

    Statement statement = mock(Statement.class);

    when(statement.executeQuery(query)).thenReturn(resultSet);
    when(connection.createStatement()).thenReturn(statement);
  }

  private ResultSet buildMockPreparedStatementQuery(final String query) throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);

    PreparedStatement statement = mock(PreparedStatement.class);

    when(statement.executeQuery()).thenReturn(resultSet);
    when(connection.prepareStatement(query)).thenReturn(statement);
    return resultSet;
  }

  private static Date parseDate(final String date) {
    try {
      return new SimpleDateFormat("dd-MM-yyyy").parse(date);
    } catch (ParseException e) {
      return null;
    }
  }
}
