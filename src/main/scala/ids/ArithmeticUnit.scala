package ids
import chisel3._

class ArithmeticUnit extends Module{
    val io = IO(new Bundle() {
      val in0 = Input(UInt(16.W))
      val in1 = Input(UInt(16.W))
      val selection = Input(Bool())
      val out = Output(UInt(32.W))
    })

  val add = Wire(UInt(16.W))
  add := io.in0 + io.in1

  val mul = Wire(UInt(16.W))
  mul := io.in0 * io.in1

  io.out := Mux(io.selection, add, mul)
}
