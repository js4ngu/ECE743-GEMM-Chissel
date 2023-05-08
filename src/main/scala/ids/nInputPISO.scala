package ids

import chisel3._

class PISORegister(width:Int)extends Module{ // 얘가 그거임 단위 모듈이랄까...
  val io = IO(new Bundle() {
    val previousInput = Input(UInt(width.W))
    val loadInput = Input(UInt(width.W))
    val load : Bool = Input(Bool())
    val output = Output(UInt(width.W))
  })
  val loadReg = RegInit(0.U(width.W))
  loadReg := Mux(io.load, io.loadInput, io.previousInput)
  io.output := loadReg
}

class PISORegisterBuffer(vectorSize: Int, width: Int) extends Module {
  val io = IO(new Bundle {
    val parallelInputs = Input(Vec(vectorSize, UInt(width.W)))
    val load = Input(Bool())
    val SerialOutput = Output(UInt(width.W))
  })

  val PISORegisterofVec = Vector.fill(vectorSize)(Module(new PISORegister(width)))

  for (i <- 0 until vectorSize)
    PISORegisterofVec(i).io.load := io.load

  for (i <- 0 until vectorSize)
    PISORegisterofVec(i).io.loadInput := io.parallelInputs(i)

  for (i <- 0 until vectorSize - 1)
    PISORegisterofVec(i + 1).io.previousInput := PISORegisterofVec(i).io.output

  PISORegisterofVec(0).io.previousInput := 0.U
  io.SerialOutput := PISORegisterofVec(vectorSize - 1).io.output
}
