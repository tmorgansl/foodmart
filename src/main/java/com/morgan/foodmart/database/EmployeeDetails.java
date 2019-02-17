package com.morgan.foodmart.database;

import java.util.Date;
import java.util.Objects;

public class EmployeeDetails {
  private final String fullName;
  private final String positionTitle;
  private final Date hireDate;
  private final Date endDate;
  private final String managementRole;

  EmployeeDetails(
      String fullName, String positionTitle, Date hireDate, Date endDate, String managementRole) {
    this.fullName = fullName;
    this.positionTitle = positionTitle;
    this.hireDate = hireDate;
    this.endDate = endDate;
    this.managementRole = managementRole;
  }

  public String getFullName() {
    return fullName;
  }

  public String getPositionTitle() {
    return positionTitle;
  }

  public Date getHireDate() {
    if (hireDate != null) {
      return (Date) hireDate.clone();
    }
    return null;
  }

  public Date getEndDate() {
    if (endDate != null) {
      return (Date) endDate.clone();
    }
    return null;
  }

  public String getManagementRole() {
    return managementRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeDetails that = (EmployeeDetails) o;
    return Objects.equals(fullName, that.fullName)
        && Objects.equals(positionTitle, that.positionTitle)
        && Objects.equals(hireDate, that.hireDate)
        && Objects.equals(endDate, that.endDate)
        && Objects.equals(managementRole, that.managementRole);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, positionTitle, hireDate, endDate, managementRole);
  }
}
