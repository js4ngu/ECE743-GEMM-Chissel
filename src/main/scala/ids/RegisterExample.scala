package ids
import chisel3._

//reg 사용법 3가지
class RegisterExample extends Module{
  val io = IO(new Bundle() {
    val in0 = Input(UInt(4.W))
    val in1 = Input(UInt(4.W))
    val in2 = Input(UInt(4.W))

    val out0 = Output(UInt(4.W))
    val out1 = Output(UInt(4.W))
    val out2 = Output(UInt(4.W))
  })

  val register0 = Reg(UInt(4.W))
  val register1 = RegInit(UInt(4.W), 0.U) //초기값 정의 느낌
  val register2 = RegNext(io.in2, 0.U)    //레지스터의 출력을 다음 클럭에서 다른 레지스터의 입력으로 사용 -> 여기에선 다음 사이클에서 io.in2를 출력할듯
  register0 := io.in0
  register1 := io.in1

  io.out0 := register0
  io.out1 := register1
  io.out2 := register2
}
