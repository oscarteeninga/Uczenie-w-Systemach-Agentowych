import akka.actor.ActorRef

case class Students(students: List[ActorRef]) {
  def !(sender: ActorRef, command: Object): Unit = {
    students.foreach(_.tell(command, sender))
  }

  def count: Int = students.size
}
