package com.morgan.foodmart.input.choice;

import java.util.List;

public interface ChoiceService {
  public String getChoice(List<String> choices, String helpMessage);
}
