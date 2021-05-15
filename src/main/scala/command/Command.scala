package command

import org.nd4j.linalg.dataset.DataSet

sealed trait Command

sealed trait Teaching extends Command
case object Teached extends Teaching
case class Teach(lesson: DataSet) extends Teaching
case class Learn(lesson: DataSet) extends Teaching
case object Learned extends Teaching

sealed trait Examining extends Command
case class Exam(exam: DataSet) extends Examining
case class Examined(predictions: List[Int]) extends Examining

sealed trait Manage extends Command
case class BeginYear(lessons: List[DataSet], exams: List[DataSet]) extends Manage
case object TeachingEnded extends Manage
case class BeginExam(exam: DataSet) extends Manage
case class ExamEnded(predictions: List[Int]) extends Manage

