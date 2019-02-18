package com.morgan.foodmart.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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

  private void buildMockSQLExceptionStatementQuery(final String query) throws SQLException {
    Statement statement = mock(Statement.class);

    when(statement.executeQuery(query)).thenThrow(new SQLException());
    when(connection.createStatement()).thenReturn(statement);
  }

  private void buildMockStatementQuery(final String query, final List<String> output)
      throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);

    OngoingStubbing<Boolean> stubNext = when(resultSet.next());

    for (String s : output) {
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
}
