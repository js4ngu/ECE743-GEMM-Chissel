package ids
import chisel3._
class PE(width:Int)extends Module{ // 얘가 그거임 단위 모듈이랄까...
  val io = IO(new Bundle() {
    val gain = Input(UInt(width.W))

    val in = Input(UInt(width.W))
    val calcedIn = Input(UInt(width.W))

    val inputBypass = Output(UInt(width.W))
    val out = Output(UInt(width.W))
  })

  val gainReg = RegInit(0.U(width.W))
  val inputBuffer = RegInit(0.U(width.W))
  val bypassBuffer = RegInit(0.U(width.W))
  val Buffer  = RegInit(0.U(width.W))

  gainReg := io.gain
  inputBuffer := io.in
  //Buffer := (gainReg * io.in) + io.calcedIn
  Buffer := (gainReg * inputBuffer) + io.calcedIn

  //bypassBuffer := io.in
  bypassBuffer := inputBuffer

  io.out := Buffer
  io.inputBypass := bypassBuffer

}

class GEMM(size : Int, width: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(size, UInt(width.W)))
    val gain = Input(Vec(size, Vec(size, UInt(width.W))))
    val out = Output(Vec(size, UInt(width.W)))
  })

  val PEVec = Array.tabulate(size, size)((i, j) => Module(new PE(width)))

  //wiring
  //세로
  for (i <- 1 until size)
    for (j <- 0 until size)
      PEVec(i)(j).io.calcedIn := PEVec(i-1)(j).io.out

  //가로 : Bypass
  for (i <- 0 until size)
    for (j <- 1 until size)
      PEVec(i)(j).io.in := PEVec(i)(j-1).io.inputBypass

  //output
  for (i <- 0 until size)
    io.out(i) := PEVec(size-1)(i).io.out

  //data_injection
  //first row calced data Init
  for (j <- 0 until size)
    PEVec(0)(j).io.calcedIn := (0.U(width.W))

  //gain
  for (i <- 0 until size)
    for (j <- 0 until size + 0)
      PEVec(i)(j).io.gain := io.gain(i)(j)

  //data
  for (i <- 0 until size)
      PEVec(i)(0).io.in := io.in(i)

}