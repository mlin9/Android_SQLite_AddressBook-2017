package michaellin.lab_assignment_9;

/**
 * Created by Jimmy on 11/5/2017.
 */

public class Student {
    private int id;
    private String name;
    private String grade;

    public Student()
    {
        super();
    }

    public Student(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Student(int id, String name, String grade)
    {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getStudent()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }
}
