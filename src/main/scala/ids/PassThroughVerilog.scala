package ids
import chisel3.stage.ChiselStage
object PassThroughVerilog extends App{
  (new ChiselStage).emitVerilog(new PassThrough)
}
