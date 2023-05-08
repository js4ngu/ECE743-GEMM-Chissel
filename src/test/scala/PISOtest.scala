package ids
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class PISOtest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "PISO"
  it should "PISO ex" in {
    test(new PISO).withAnnotations(Seq(WriteVcdAnnotation)) { p =>
      p.io.load.poke(true.B)
      p.io.previousInput.poke(9.U)

      p.io.paralleInputs(0).poke(1.U)
      p.io.paralleInputs(1).poke(2.U)
      p.io.paralleInputs(2).poke(3.U)
      p.io.paralleInputs(3).poke(4.U)

      p.clock.step()

      p.io.load.poke(false.B)
      p.io.out.expect(4.U)

      p.clock.step()
      p.io.out.expect(3.U)

      p.clock.step()
      p.io.out.expect(2.U)

      p.clock.step()
      p.io.out.expect(1.U)

      p.clock.step()
      p.io.out.expect(9.U)
    }
  }
}
