package ids
import chisel3._
class PISO extends Module {
  val io = IO(new Bundle() {
    val previousInput = Input(UInt(32.W))
    val paralleInputs = Input(Vec(4, UInt(32.W)))
    val load = Input(Bool())
    val out = Output(UInt(32.W))
  })
  val registerOfVector = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))

  when(io.load){
    registerOfVector := io.paralleInputs
  }.otherwise{
    registerOfVector(0) := io.previousInput
    registerOfVector(1) := registerOfVector(0)
    registerOfVector(2) := registerOfVector(1)
    registerOfVector(3) := registerOfVector(2)
  }

  io.out := registerOfVector(3)
}
