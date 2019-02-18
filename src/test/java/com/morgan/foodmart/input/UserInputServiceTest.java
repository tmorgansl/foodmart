package com.morgan.foodmart.input;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.morgan.foodmart.database.EmployeeDatabaseService;
import com.morgan.foodmart.input.choice.ChoiceService;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserInputServiceTest {
  private static EmployeeDatabaseService employeeDatabaseService;
  private static ChoiceService choiceService;
  private static UserInputService userInputService;
  private static final List<String> departments = Arrays.asList("marketing", "store management");
  private static final List<String> payType = Arrays.asList("monthly", "hourly");
  private static final List<String> educationLevel = Arrays.asList("bachelors", "masters");

  @Before
  public void init() {
    employeeDatabaseService = mock(EmployeeDatabaseService.class);
    choiceService = mock(ChoiceService.class);

    when(employeeDatabaseService.getDepartments()).thenReturn(departments);
    when(employeeDatabaseService.getPayTypes()).thenReturn(payType);
    when(employeeDatabaseService.getEducation()).thenReturn(educationLevel);

    userInputService = new UserInputServiceImpl(employeeDatabaseService, choiceService);
  }

  @Test
  public void testReturnsCorrectUserInput() {

    final String expectedDepartment = departments.get(0);
    final String expectedPayType = payType.get(1);
    final String expectedEduationLevel = educationLevel.get(0);
    final InputChoices expectedInputChoices =
        new InputChoices(expectedDepartment, expectedPayType, expectedEduationLevel);

    when(choiceService.getChoice(departments, Constants.departmentHelpMessage))
        .thenReturn(expectedDepartment);
    when(choiceService.getChoice(payType, Constants.payTypeHelpMessage))
        .thenReturn(expectedPayType);
    when(choiceService.getChoice(educationLevel, Constants.educationLevelHelpMessage))
        .thenReturn(expectedEduationLevel);

    final InputChoices inputChoices = userInputService.getUserInputChoices();

    assertEquals(expectedInputChoices, inputChoices);

    verify(employeeDatabaseService).getDepartments();
    verify(employeeDatabaseService).getPayTypes();
    verify(employeeDatabaseService).getEducation();
    verify(choiceService).getChoice(departments, Constants.departmentHelpMessage);
    verify(choiceService).getChoice(payType, Constants.payTypeHelpMessage);
    verify(choiceService).getChoice(educationLevel, Constants.educationLevelHelpMessage);
  }
}
