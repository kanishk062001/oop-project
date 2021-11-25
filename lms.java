import java.io.*;
import java.util.*;
class ClearScreen{
public static void clearScreen() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush(); 
}
}

class DriverCode {

    public void welcome(String username) {
        System.out.println("Welcome " + username);
    }

    public Student search(List<Student> studentList, Integer regId) {
        Student student = new Student();
        boolean found = false;
        for (Student stu : studentList) {

            if (stu.getRegID() == regId) {
                student = stu;
                found = true;
                return student;
            }
        }
        if (!found) {
            System.out.println("Student with this registration ID is not found");
            return student;
        }
        return student;
    }
}

class User implements Serializable {

    String name;
    Integer regId;
    String emailId;
    String password;
    String contactNo;
    Course course;

    User(String name, Integer regId,Course course, String emailId, String password, String contactNo) {
        this.name = name;
        this.regId = regId;
        this.course = course;
        this.emailId = emailId;
        this.password = password;
        this.contactNo = contactNo;
    }

    User() {
        this.name = "NULL";
        this.regId = 0000;
        this.course =new Course();
        this.emailId = "NULL";
        this.password = "1234";
        this.contactNo = "NULL";
    }

    public boolean Login(Integer regId, String password) {
        if (this.regId.equals(regId) && this.password.equals(password)) {
            ClearScreen.clearScreen();
            System.out.println("Congratulations " + this.name+ " for Successful Login");
            return true;
        } else {
            System.out.println("Login Failed");
            return false;
        }
    }

    public User updateProfile() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your email address: ");
        this.emailId = scan.nextLine();
        System.out.println("Enter your Contact Number: ");
        this.contactNo = scan.nextLine();
        System.out.println("Profile Updated");
        return this;

    }

    public void printDetails() {
        System.out.println("Name: " + this.name);
        System.out.println("Registration ID: " + this.regId);
        System.out.println("Email: " + this.emailId);
        System.out.println("Contact Number: " + this.contactNo);

    }
    public Course returnCourse() {
        return this.course;
    }
}

class Student extends User {

    String userType = "STUDENT";
    String sId;
    ArrayList<Integer> marks = new ArrayList<Integer>();
    StudentService studentService;

    Student(String name, Integer regId,Course course, String emailId, String password, String contactNo) {
        super(name, regId, course,emailId, password, contactNo);
        this.sId = "S" + regId;
        studentService = new StudentService(this);
        for(int i = 0; i <3; i++)
            marks.add(0);
    }

    Student() {
        super();
        this.sId = "S" + regId;
        studentService = new StudentService(this);
        for(int i = 0; i <3; i++)
            marks.add(0);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        name = this.name;
    }

    public ArrayList<Integer> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<Integer> arrMarks){
        marks=arrMarks;
    }

    public int getRegID() {
        return regId;
    }

    public String getCourse() {
        return course.getCourseName();
    }

}

class Admin extends User {
    String userType = "TEACHER";
    String tId;
    AdminService adminService;

    Admin(String name, Integer regId, Course course, String emailId, String password, String contactNo) {
        super(name, regId, course, emailId, password, contactNo);
        this.tId = "T" + regId;
        
        adminService = new AdminService(this);
    }

    Admin() {

        super();
        this.tId = "T" + regId;
        adminService = new AdminService(this);
    }

    public String getTId() {
        return this.tId;
    }

    public String getName() {
        return this.name;
    }
    
    /*private Student returnStudent(Integer regId, String name){

    }*/
    public List<Student> updateStudent(List<Student> studentList, Integer regId) {
        DriverCode drivercode = new DriverCode();
        Student studentOld= drivercode.search(studentList,regId);
        //student.setName(name);
        Student studentNew= (Student)studentOld.updateProfile();
        studentNew.printDetails();
        studentList.set(studentList.indexOf(studentOld), studentNew);
        //ArrayList<Integer> marks = adminService.updateMarks(student.getMarks());
        //System.out.println(marks.toString());
        return studentList;
    }

    public ArrayList<Student> removeStudent(Student student, ArrayList<Student> arrstu) {
        arrstu.remove(student);
        return arrstu;
    }
    public String courseName(){
        return course.getCourseName();
    }

}

interface Service<U> {
    public void viewDetails(U user);

    public void viewMarks(Student student);
    public void viewCourse();
    public void viewEnrolledStudent(List<Student> studentList,Course course);
}

class AdminService implements Service<Admin> {
    Admin teacher;

    AdminService(Admin teacher) {
        this.teacher = teacher;
    }

    public void viewDetails(Admin teacher) {
        teacher.printDetails();
    }

    public void viewDetails(Student student) {
        student.printDetails();
        
    }

    public void viewMarks(Student student) {
        //System.out.println(student.getName());
        System.out.println(student.getMarks().toString());
    }

    public ArrayList<Integer> updateMarks(ArrayList<Integer> arrMarks) {
        ListIterator<Integer> itr = arrMarks.listIterator();
        Scanner sc = new Scanner(System.in);
        int j=1;
        while (itr.hasNext()) {
            Integer i = (Integer) itr.next();
            System.out.println("Test "+j+" marks: ");
            System.out.println(i);
            System.out.println("Updated marks: ");
            i = (Integer) sc.nextInt();
            itr.remove();
            
            itr.add(i);
            //System.out.println(i);
            j++;
        }

        return arrMarks;

    }
    public Course updateCourse(Course course){
        ArrayList<Topics> newTopics= course.modifyCourse();
        course= new Course(course.getCourseName(),newTopics);
        return course;
    }
    public void viewEnrolledStudent(List<Student> studentList,Course course){
        for(Student student : studentList){
            if(course.getCourseName().equals(student.getCourse())){
                System.out.println(student.getName());
            }
        }
        
    }
    public void viewCourse(){
        System.out.println(this.teacher.courseName());
    }

}

class StudentService implements Service<Student>, Serializable {
    Student student;

    StudentService(Student student) {
        this.student = student;
    }

    public void viewDetails(Student student) {
        student.printDetails();
    }

    public void viewMarks(Student student) {
        ArrayList<Integer> marks=student.getMarks();
        System.out.println(marks.toString());
    }
    public void viewCourse(){
        System.out.println(this.student.getCourse());
    }
    public void viewEnrolledStudent(List<Student> studentList,Course course){
        for(Student student : studentList){
            if(course.getCourseName().equals(student.getCourse())){
                System.out.println(student.getName());
            }
        }
        
    }

}

class Course implements Serializable {
    String courseName;
    ArrayList<Topics> topics;
    Course(){
        courseName="OOPS";
        topics=new ArrayList<Topics>();
    }
    Course(String courseName){
        this.courseName=courseName;
        topics=new ArrayList<Topics>();
    }
    Course(String courseName, ArrayList<Topics> topics){
        this.courseName=courseName;
        this.topics=topics;
    }
    String getCourseName(){
        return this.courseName;
    }
    public ArrayList<Topics> modifyCourse(){
        ArrayList<Topics> newTopics= new ArrayList<Topics>();
        for(Topics topic : topics){
            System.out.println(topic.getName());
            
        }
        System.out.println("Do you wish to enter topics 1. YES   2.NO");
        boolean answer=true ;
        Scanner scan= new Scanner(System.in);
        if(scan.nextInt()==1){
            while(answer){
                Scanner scani= new Scanner(System.in);
                System.out.println("Enter new topic");
                Topics topic=new Topics(scani.nextLine());
                newTopics.add(topic);
                System.out.println("Do you want to add more : 1.Yes  2.No");
                if(scani.nextInt()==2){
                    answer=false;
                    
                }
            }
            return newTopics;   
        }
        else{
            return newTopics;
        }
    }
    public void getTopics(){
        int i=1;
        for(Topics topic : topics){
            System.out.println(i+". "+topic.getName());
            i++;
        }
    }
}
 


class Topics implements Serializable {
    String topicName;
    Topics(){
        topicName="Project";
    }
    Topics(String topicName){
        this.topicName=topicName;
    }
    public void viewAllTopics(ArrayList <Topics> topics){
        for(Topics topic : topics){
            System.out.println(topic.getName());
        }
    }
    public String getName(){
        return topicName;
    }
    public void changeTopic(String topic){
        this.topicName = topic;
    }
    /*public void modify() {
        System.out.println("1. ADD/UPDATE New Topic ");
        System.out.println("2. REMOVE This Topic ");

        Scanner modifyscanner = new Scanner(System.in);
        int option=modifyscanner.nextInt();
        if(option==1){
            Scanner modifyscanner1 = new Scanner(System.in);
            System.out.println("Previous topic name :"+this.getName());
            System.out.print("Updated topic name : ");
            this.changeTopic(modifyscanner1.nextLine()) ;
            return this;
        }
        else {
        System.out.println("Current topic name to be removed:"+this.getName());
        this.topicName = "";
        return this;
        }
    }*/
}

class lms {
    public static void main(String[] args) throws FileNotFoundException {

        
        List<Student> studentList = new ArrayList<Student>();
        FileInputStream filein = new FileInputStream("studentData.txt");
        boolean cont = true;
        try (ObjectInputStream ois = new ObjectInputStream(filein)) {
            while (cont) {
                Student student = (Student) ois.readObject();
                if (student != null) {
                    studentList.add(student);
                } else {
                    cont = false;
                }
            }
        } catch (Exception e) {
            // System.out.println(e);
        }

        List<Course> courseList = new ArrayList<Course>();
        FileInputStream fileinc = new FileInputStream("courseData.txt");
        boolean contc = true;
        try (ObjectInputStream oisc = new ObjectInputStream(fileinc)) {
            while (contc) {
                Course course= (Course) oisc.readObject();
                if (course != null) {
                    courseList.add(course);
                } else {
                    contc = false;
                }
            }
        } catch (Exception e) {
            // System.out.println(e);
        }


        //Course courseIntialize= new Course();
        //courseList.add(courseIntialize);
          
        //Student student = new Student("Kanishk Jain",1,courseList.get(0),"kanishk112001@gmail.com","hello","8178653882");
        //studentList.add(student);
         

        
        boolean repeat = true;
        
        while (repeat)
        {   
            ClearScreen.clearScreen();
            System.out.println(" Welcome to LMS");
            System.out.println("1. Login as Student");
            System.out.println("2. Login as Teacher");
            System.out.println("3. Exit");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            DriverCode driverCode = new DriverCode();
            switch (choice) 
            {
                case 1:
                    ClearScreen.clearScreen();
                    System.out.println("Welcome Student");
                    System.out.println("1. Existing Student");
                    System.out.println("2. New Student");
                    System.out.println("3. Exit");
                    Scanner scanner0 = new Scanner(System.in);
                    int choice1 = scanner0.nextInt();
                    boolean repeating = true;
                    while (repeating) {
                    //System.out.println("Enter your choice");
                    //int choice1 = scanner.nextInt();
                        switch (choice1) {
                            case 1:
                                ClearScreen.clearScreen();
                                System.out.println("Please enter your registration ID and password");
                                Integer regId = (Integer) scanner.nextInt();
                                Scanner scanner1 = new Scanner(System.in);
                                String password = scanner1.next();
                                Student studentOld = new Student();
                                studentOld = driverCode.search(studentList, regId);
                                if (!studentOld.Login(regId, password)) {
                                    System.out.println("Wrong Password");
                                } 
                                else {
                                    boolean bool = true;
                                    while (bool) {
                                        
                                        System.out.println("");
                                        driverCode.welcome(studentOld.getName());
                                        System.out.println("");
                                        System.out.println("");
                                        System.out.println("1. Update Profile");
                                        System.out.println("2. View Personal Details");
                                        System.out.println("3. View Marks");
                                        System.out.println("4. View Your Course");
                                        System.out.println("5. View Enrolled Students in Course");
                                        System.out.println("6. View Topics of All Courses");
                                        System.out.println("7. Exit");
                                        Scanner scannerS = new Scanner(System.in);
                                        int choice2 = scannerS.nextInt();
                                        ClearScreen.clearScreen();
                                        switch (choice2) {
                                            case 1:
                                                Student studentNew = (Student) studentOld.updateProfile();
                                                studentNew.printDetails();
                                                //bool = false;
                                                studentList.set(studentList.indexOf(studentOld), studentNew);

                                                break;

                                            case 2:
                                                studentOld.printDetails();
                                                //bool = false;
                                                break;

                                            case 3:
                                                System.out.println("Test componet: 3 tests each of 100 marks");
                                                StudentService service = new StudentService(studentOld);
                                                service.viewMarks(studentOld);
                                                //bool = false;
                                                break;

                                            case 4:
                                                StudentService studentservice = new StudentService(studentOld);
                                                studentservice.viewCourse();
                                                break;
                                            
                                            case 5:
                                                StudentService studentservice5 = new StudentService(studentOld);
                                                studentservice5.viewEnrolledStudent(studentList, studentOld.returnCourse());
                                                break;

                                            case 6:
                                                for(Course course9: courseList){
                                                    System.out.println(course9.getCourseName());
                                                    course9.getTopics();
                                                    
                                                }
                                                break;

                                            case 7:
                                                repeating = false;
                                                bool = false;
                                                break;

                                            default:
                                                System.out.println("Invalid option");
                                                System.out.println("Please Re-enter the Option");
                                                break;
                                        }
                                    }
                                }
                        repeating = false;
                        break;
                            

                    case 2:
                        ClearScreen.clearScreen();
                        Scanner sc= new Scanner(System.in);
                        System.out.println("Please register as a new student");
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter your Email ID: ");
                        String email = sc.nextLine();
                        System.out.print("Enter your Password: ");
                        String newPassword = sc.nextLine();
                        System.out.print("Enter your Contact Number: ");
                        String contact = sc.nextLine();
                        Course course= courseList.get(0);
                        Integer newRegId = (Integer) (studentList.get(studentList.size() - 1).getRegID()+1);
                        Student student1 = new Student(name, newRegId,course, email, newPassword, contact);
                        studentList.add(student1);
                        repeating = false;
                        break;

                    case 3:
                        repeating = false;
                        break;

                    default:
                        System.out.println("Invalid Query");

                    }
                }
                break;

            
            case 2:
                ClearScreen.clearScreen();
                Course course= courseList.get(0);
                Admin teacher = new Admin("Amit",100,course,"amitdua@pilani.bits-pilani.ac.in","helloamit","1113334448");
                System.out.println("Please enter your registration ID and password");
                Integer regId = (Integer) scanner.nextInt();
                String password = scanner.next();
                if (!teacher.Login(regId, password)) {
                    System.out.println(" Wrong Password");
                } 
                else {
                    boolean bool = true;
                    while (bool) {
                        System.out.println("");
                        driverCode.welcome(teacher.getName());
                        System.out.println("1. Update Profile");
                        System.out.println("2. Update marks");
                        System.out.println("3. View Marks of all Students");
                        System.out.println("4. View Details of a Student");
                        System.out.println("5. Delete Student");
                        System.out.println("6. View Enrolled Students in "+ teacher.courseName());
                        System.out.println("7. View All available courses");
                        System.out.println("8. Modify Course(Note once entered all topics of course will be erased) ");
                        System.out.println("9. View Topics of Course" );
                        System.out.println("10. Remove course");
                        System.out.println("11. Exit");
                        AdminService adminService= new AdminService(teacher);
                        Scanner scannerT = new Scanner(System.in);
                        int choice2 = scannerT.nextInt();
                        ClearScreen.clearScreen();
                        //scannerT.close();
                        switch (choice2){
                            case 1:
                                Scanner scnew = new Scanner(System.in);
                                System.out.println("Enter the registration ID of the Student to be updated");
                                Integer stuRegId=(Integer) scnew.nextInt();
                                for(Student student1: studentList){
                                    if(student1.getRegID()==stuRegId){

                                        studentList = teacher.updateStudent(studentList,stuRegId);
                                        break;
                                    }
                                }
                                
                                break;

                            case 2:
                                ArrayList<Integer> arrmarks= new ArrayList<Integer>();
                                System.out.println("Enter the registration ID of the Student whose marks are updated");
                                Scanner scnew2= new Scanner(System.in);
                                Integer stu2RegId=(Integer) scnew2.nextInt();
                                DriverCode drivercode = new DriverCode();
                                Student student2=drivercode.search(studentList,stu2RegId);
                                arrmarks= student2.getMarks();
                                arrmarks= adminService.updateMarks(arrmarks);
                                student2.setMarks(arrmarks);
                                studentList.set(studentList.indexOf(student2),student2);
                                break;

                            case 3:
                                for(Student localstudent: studentList){
                                    System.out.print(localstudent.getName()+" :");
                                    adminService.viewMarks(localstudent);
                                }
                                break;

                            case 4:
                                System.out.println("Enter the registration ID of the Student to be viewed");
                                Scanner scnew4= new Scanner(System.in);
                                Integer stu4RegId=(Integer) scnew4.nextInt();
                                DriverCode drivercode4 = new DriverCode();
                                Student student4=drivercode4.search(studentList,stu4RegId);
                                adminService.viewDetails(student4);
                                System.out.print("Marks: ");
                                adminService.viewMarks(student4);
                                break;

                            
                            case 5 :
                                Scanner scnew5= new Scanner(System.in);
                                System.out.println("Enter the registration ID of the Student to be Deleted");
                                Integer stu5RegId=(Integer) scnew5.nextInt();
                                DriverCode drivercode5 = new DriverCode();
                                Student student5=drivercode5.search(studentList,stu5RegId);
                                Collections.reverse(studentList);
                                studentList.remove(student5);
                                Collections.reverse(studentList);
                                break;
                            

                            case 6:
                                AdminService adminservice6= new AdminService(teacher);
                                adminservice6.viewEnrolledStudent(studentList, teacher.returnCourse());
                                break;

                            case 7:
                                for(Course course7:courseList)
                                    System.out.println(course7.getCourseName());
                                    break;

                            case 8:
                                 
                                Scanner scan8= new Scanner(System.in);
                                
                                System.out.println("Enter the name of course to be modified : ");
                                String courseName8=scan8.nextLine();
                                for(Course cors: courseList){
                                    if(cors.getCourseName().equals(courseName8)){
                                        
                                        //Course course8 = new Course(cors.getCourseName(),cors.topics);
                                        ArrayList<Topics> topics8= cors.modifyCourse();
                                        Course coursenew= new Course(courseName8,topics8);
                                        courseList.set(courseList.indexOf(cors),coursenew);
                                        break;
                                        
                                    }
                                    else{
                                        Course course8= new Course(courseName8);
                                        ArrayList<Topics> topics8= course8.modifyCourse();
                                        Course coursenew= new Course(courseName8,topics8);
                                        courseList.add(coursenew);
                                        break;
                                    }
                                }
                                break;
                                

                            case 9: 
                                for(Course course9: courseList){
                                    System.out.println(course9.getCourseName());
                                    course9.getTopics();
                                }
                                break;

                            case 10: 
                                Scanner scanner10= new Scanner(System.in);
                                System.out.println("Enter the name of course to be deleted: ");
                                String subject=scanner10.nextLine();
                                boolean bool10=true;
                                Collections.reverse(courseList);
                                for(Course course10: courseList){
                                    if(course10.getCourseName().equals(subject)){
                                        courseList.remove(course10);
                                        bool10=false;
                                        break;
                                    }
                                }
                                if(bool10){
                                    System.out.println("Invalid Course");
                                }
                                Collections.reverse(courseList);
                                break;

                    
                            case 11:
                                bool=false;
                                break;
                            
                            default:
                                System.out.println("Invalid Query");
                        }
                        
                    }
                }
                break;


            case 3:
                repeat=false;
                break;

            default: 
                System.out.println("Invalid Query");
            
            }
            
        }
        
        /*
         * Student student = new Student("halini",2,"gmail","hello","985645665");
         * studentList.add(student); StudentService ss=new StudentService(student);
         */
        FileOutputStream fout = new FileOutputStream("studentData.txt");
        try (ObjectOutputStream os = new ObjectOutputStream(fout)) {
            for (Student stu : studentList) {
                try {
                    os.writeObject(stu);
                    /*
                     * ss.viewDetails(stu); System.out.println("saved");
                     */
                } catch (NotSerializableException e) {
                    System.out.println("An object was not serializable, it has not been saved.");
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        FileOutputStream foutc = new FileOutputStream("courseData.txt");
        try (ObjectOutputStream osc = new ObjectOutputStream(foutc)) {
            for (Course cor :courseList) {
                try {
                    osc.writeObject(cor);
                    /*
                     * ss.viewDetails(stu); System.out.println("saved");
                     */
                } catch (NotSerializableException e) {
                    System.out.println("An object was not serializable, it has not been saved.");
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        /*for (Student s : studentList) {
            s.printDetails();
        }*/

    

    }
}

