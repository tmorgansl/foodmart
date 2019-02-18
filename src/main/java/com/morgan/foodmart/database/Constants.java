package com.morgan.foodmart.database;

class Constants {
  private Constants() {}

  static final String employeeDetailsQuery =
      "SELECT e.full_name,"
          + "e.position_title,"
          + "e.hire_date,"
          + "e.end_date,"
          + "e.management_role "
          + "FROM foodmart.employee e "
          + "INNER JOIN foodmart.`position` p ON p.position_id = e.position_id "
          + "INNER JOIN foodmart.department d ON d.department_id = e.department_id "
          + "WHERE p.pay_type = ? "
          + "AND department_description = ? "
          + "AND education_level = ?";

  static final String departmentsQuery =
      "SELECT department_description FROM foodmart.department GROUP BY department_description";

  static final String payTypesQuery = "SELECT pay_type FROM foodmart.`position` GROUP BY pay_type";

  static final String educationLevelQuery =
      "SELECT education_level FROM foodmart.employee GROUP BY education_level";
}
