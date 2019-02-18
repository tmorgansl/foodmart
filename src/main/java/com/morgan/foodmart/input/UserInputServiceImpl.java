package com.morgan.foodmart.input;

import com.morgan.foodmart.database.EmployeeDatabaseService;
import com.morgan.foodmart.input.choice.ChoiceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInputServiceImpl implements UserInputService {

  private final EmployeeDatabaseService databaseService;
  private final ChoiceService choiceService;

  @Autowired
  UserInputServiceImpl(
      final EmployeeDatabaseService databaseService, final ChoiceService choiceService) {
    this.databaseService = databaseService;
    this.choiceService = choiceService;
  }

  @Override
  public InputChoices getUserInputChoices() {
    final List<String> departments = databaseService.getDepartments();
    final List<String> payTypes = databaseService.getPayTypes();
    final List<String> eduacationLevels = databaseService.getEducation();

    final String department = choiceService.getChoice(departments, Constants.departmentHelpMessage);
    final String payType = choiceService.getChoice(payTypes, Constants.payTypeHelpMessage);
    final String educationLevel =
        choiceService.getChoice(eduacationLevels, Constants.educationLevelHelpMessage);

    return new InputChoices(department, payType, educationLevel);
  }
}
