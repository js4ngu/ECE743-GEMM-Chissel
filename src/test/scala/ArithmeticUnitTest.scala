package ids
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ArithmeticUnitTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Arithmetic Unit"
  it should "calc input depending on selection" in {
    test(new ArithmeticUnit).withAnnotations(Seq(WriteVcdAnnotation)) { Unit =>
      Unit.io.in0.poke(4.U)
      Unit.io.in1.poke(1.U)
      Unit.io.selection.poke(true.B)
      Unit.io.out.expect(5.U)

      Unit.io.in0.poke(4.U)
      Unit.io.in1.poke(2.U)
      Unit.io.selection.poke(false.B)
      Unit.io.out.expect(8.U)
    }
  }
}
