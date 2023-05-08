import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import ids.PISORegisterBuffer

class nInputPISOtest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "PISORegisterBuffer"
  it should "PISORegisterBuffer ex" in {
    test(new PISORegisterBuffer(vectorSize = 5, width = 32)).withAnnotations(Seq(WriteVcdAnnotation)) { p =>
      p.io.load.poke(true.B)
      p.io.parallelInputs(0).poke(1.U(5.W))
      p.io.parallelInputs(1).poke(2.U(5.W))
      p.io.parallelInputs(2).poke(3.U(5.W))
      p.io.parallelInputs(3).poke(4.U(5.W))
      p.io.parallelInputs(4).poke(5.U(5.W))
      p.clock.step()

      p.io.load.poke(false.B)
      p.io.SerialOutput.expect(5.U(5.W))
      p.clock.step()
      p.io.SerialOutput.expect(4.U(5.W))
      p.clock.step()
      p.io.SerialOutput.expect(3.U(5.W))
      p.clock.step()
      p.io.SerialOutput.expect(2.U(5.W))
      p.clock.step()
      p.io.SerialOutput.expect(1.U(5.W))
      p.clock.step()
      p.io.SerialOutput.expect(0.U(5.W))
    }
  }
}