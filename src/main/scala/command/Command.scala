package command

import org.nd4j.linalg.dataset.DataSet

sealed trait Command

case object Teached extends Command
case class Teach(lesson: DataSet) extends Command
case class Learn(lesson: DataSet) extends Command
case object Learned extends Command

case class Exam(exam: DataSet) extends Command
case class Examined(predictions: List[Int]) extends Command

sealed trait Manage
case class BeginYear(lessons: List[DataSet], exams: List[DataSet]) extends Manage
case object TeachingEnded extends Manage
case class BeginExam(exam: DataSet) extends Manage
case class ExamEnded(predictions: List[Int]) extends Manage

