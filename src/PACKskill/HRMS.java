package PACKskill;

import java.io.*;
import java.util.*;

public class HRMS {

    // ================= EMPLOYEE =================
    static class Employee implements Serializable {
        int id;
        String name, designation, department, contact;

        Employee(int id, String name, String designation, String department, String contact) {
            this.id = id;
            this.name = name;
            this.designation = designation;
            this.department = department;
            this.contact = contact;
        }

        public String toString() {
            return id + " | " + name + " | " + designation + " | " + department + " | " + contact;
        }
    }

    // ================= ATTENDANCE =================
    static class Attendance implements Serializable {
        int empId;
        String date, status;

        Attendance(int empId, String date, String status) {
            this.empId = empId;
            this.date = date;
            this.status = status;
        }

        public String toString() {
            return empId + " | " + date + " | " + status;
        }
    }

    // ================= LEAVE =================
    static class Leave implements Serializable {
        int empId;
        String type, startDate, endDate, status;

        Leave(int empId, String type, String startDate, String endDate, String status) {
            this.empId = empId;
            this.type = type;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
        }

        public String toString() {
            return empId + " | " + type + " | " + startDate + " to " + endDate + " | " + status;
        }
    }

    // ================= FILE UTIL =================
    static class FileUtil {
        static <T> void save(String file, List<T> data) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(data);
            } catch (Exception e) {
                System.out.println("Error saving data");
            }
        }

        static <T> List<T> load(String file) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<T>) ois.readObject();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
    }

    // ================= AUTH =================
    static boolean login(String u, String p) {
        return u.equals("admin") && p.equals("1234");
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== HRMS LOGIN =====");
        System.out.print("Username: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();

        if (!login(u, p)) {
            System.out.println("Invalid login!");
            return;
        }

        List<Employee> employees = FileUtil.load("emp.dat");
        List<Attendance> attendanceList = FileUtil.load("att.dat");
        List<Leave> leaveList = FileUtil.load("leave.dat");

        while (true) {
            System.out.println("\n===== HRMS MENU =====");
            System.out.println("1.Employee\n2.Attendance\n3.Leave\n4.Search\n5.Reports\n6.Exit");

            int ch = sc.nextInt();

            switch (ch) {

                // ================= EMPLOYEE =================
                case 1:
                    System.out.println("1.Add 2.View 3.Delete");
                    int e = sc.nextInt();

                    if (e == 1) {
                        System.out.print("ID: ");
                        int id = sc.nextInt();
                        System.out.print("Name: ");
                        String name = sc.next();
                        System.out.print("Designation: ");
                        String des = sc.next();
                        System.out.print("Dept: ");
                        String dep = sc.next();
                        System.out.print("Contact: ");
                        String con = sc.next();

                        employees.add(new Employee(id, name, des, dep, con));
                        FileUtil.save("emp.dat", employees);
                        System.out.println("Employee added!");
                    }

                    else if (e == 2) {
                        employees.forEach(System.out::println);
                    }

                    else if (e == 3) {
                        System.out.print("Enter ID: ");
                        int id = sc.nextInt();
                        employees.removeIf(emp -> emp.id == id);
                        FileUtil.save("emp.dat", employees);
                        System.out.println("Deleted!");
                    }
                    break;

                // ================= ATTENDANCE =================
                case 2:
                    System.out.println("1.Mark 2.View");
                    int a = sc.nextInt();

                    if (a == 1) {
                        System.out.print("Emp ID: ");
                        int id = sc.nextInt();
                        System.out.print("Date: ");
                        String date = sc.next();
                        System.out.print("Status (Present/Absent/Leave): ");
                        String status = sc.next();

                        attendanceList.add(new Attendance(id, date, status));
                        FileUtil.save("att.dat", attendanceList);
                        System.out.println("Attendance recorded!");
                    }

                    else {
                        attendanceList.forEach(System.out::println);
                    }
                    break;

                // ================= LEAVE =================
                case 3:
                    System.out.println("1.Apply 2.View 3.Approve/Reject");
                    int l = sc.nextInt();

                    if (l == 1) {
                        System.out.print("Emp ID: ");
                        int id = sc.nextInt();
                        System.out.print("Type: ");
                        String type = sc.next();
                        System.out.print("Start: ");
                        String s = sc.next();
                        System.out.print("End: ");
                        String en = sc.next();

                        leaveList.add(new Leave(id, type, s, en, "Pending"));
                        FileUtil.save("leave.dat", leaveList);
                        System.out.println("Leave applied!");
                    }

                    else if (l == 2) {
                        leaveList.forEach(System.out::println);
                    }

                    else {
                        System.out.print("Emp ID: ");
                        int id = sc.nextInt();
                        System.out.print("Status (Approved/Rejected): ");
                        String st = sc.next();

                        for (Leave lv : leaveList) {
                            if (lv.empId == id) {
                                lv.status = st;
                            }
                        }

                        FileUtil.save("leave.dat", leaveList);
                        System.out.println("Updated!");
                    }
                    break;

                // ================= SEARCH =================
                case 4:
                    System.out.print("Search: ");
                    String key = sc.next();

                    for (Employee emp : employees) {
                        if (emp.name.contains(key) ||
                            String.valueOf(emp.id).equals(key) ||
                            emp.department.contains(key)) {
                            System.out.println(emp);
                        }
                    }
                    break;

                // ================= REPORTS =================
                case 5:
                    System.out.println("Total Employees: " + employees.size());
                    System.out.println("Total Attendance Records: " + attendanceList.size());
                    System.out.println("Total Leaves: " + leaveList.size());
                    break;

                case 6:
                    System.exit(0);
            }
        }
    }
}
