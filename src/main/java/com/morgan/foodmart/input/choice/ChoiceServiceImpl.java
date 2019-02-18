package com.morgan.foodmart.input.choice;

import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChoiceServiceImpl implements ChoiceService {

  private final Scanner scanner;

  @Autowired
  public ChoiceServiceImpl() {
    this.scanner = new Scanner(System.in, "UTF-8");
  }

  @Override
  public String getChoice(List<String> choices, String helpMessage) {
    final int maxNumberChoices = choices.size();
    System.out.println(helpMessage);
    for (int i = 0; i < maxNumberChoices; i++) {
      final String line = String.format("%d) %s", i + 1, choices.get(i));
      System.out.println(line);
    }

    int choice = 0;

    while (choice < 1 || choice > maxNumberChoices) {
      System.out.print(String.format("Your choice [1-%d]", maxNumberChoices));

      final String choiceString = scanner.next();

      try {
        choice = Integer.parseInt(choiceString);
      } catch (NumberFormatException e) {
        System.out.println("Invalid choice");
        continue;
      }

      if (choice > maxNumberChoices) {
        System.out.println("Invalid choice");
      }
    }

    return choices.get(choice - 1);
  }
}
