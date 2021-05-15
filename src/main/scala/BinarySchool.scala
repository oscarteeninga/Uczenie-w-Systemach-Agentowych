import agents.student.BinaryStudent
import agents.Director
import agents.students.BinaryStudents
import akka.actor.{ActorSystem, Props}
import command.BeginYear
import network.Book

object BinarySchool extends App {
  val system = ActorSystem("school")

  val studentList = (1 to 5).toList.map(id => system.actorOf(Props(BinaryStudent(id, id % 10)), "Student" + id))
  val students = system.actorOf(Props(BinaryStudents(studentList)), "Students")
  val director = system.actorOf(Props(Director(students)), "Director")

  val training = (1 to 10).toList.map(_ => Book.mnistTrain.next())
  val test = (1 to 3).toList.map(_ => Book.mnistTest.next())

  director ! BeginYear(training, test)

  Thread.sleep(20000)
  system.terminate()
}
