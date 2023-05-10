import chisel3._
import chiseltest._

import ids.PE
import ids.GEMM

import org.scalatest.flatspec.AnyFlatSpec

class PEtest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "PE"
  it should "PE ex" in {
    test(new PE(width = 8)).withAnnotations(Seq(WriteVcdAnnotation)) { p =>
      p.io.gain.poke(1.U(8.W))
      p.clock.step()

      p.io.in.poke(1.U(8.W))
      p.io.calcedIn.poke(1.U(8.W))

      p.clock.step()

      p.io.out.expect(2.U(8.W))
      p.io.inputBypass.expect(1.U(8.W))

    }
  }
}
class GEMMtest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "GEMM"
  it should "GEMM ex" in {
    test(new GEMM(size = 3, width = 8)).withAnnotations(Seq(WriteVcdAnnotation)) { p =>
      p.io.gain(0)(0).poke(1.U(8.W))
      p.io.gain(0)(1).poke(0.U(8.W))
      p.io.gain(0)(2).poke(0.U(8.W))

      p.io.gain(1)(0).poke(0.U(8.W))
      p.io.gain(1)(1).poke(1.U(8.W))
      p.io.gain(1)(2).poke(0.U(8.W))

      p.io.gain(2)(0).poke(0.U(8.W))
      p.io.gain(2)(1).poke(0.U(8.W))
      p.io.gain(2)(2).poke(1.U(8.W))

      p.clock.step()

      p.io.in(0).poke(1.U(8.W))
      p.clock.step()

      p.io.in(0).poke(2.U(8.W))
      p.io.in(1).poke(4.U(8.W))
      p.clock.step()

      p.io.in(0).poke(3.U(8.W))
      p.io.in(1).poke(5.U(8.W))
      p.io.in(2).poke(7.U(8.W))
      p.clock.step()

      p.io.in(1).poke(6.U(8.W))
      p.io.in(2).poke(8.U(8.W))
      p.clock.step()

      p.io.in(2).poke(9.U(8.W))
      p.clock.step()

      //add clk
      p.clock.step()
      p.clock.step()
      p.clock.step()
      p.clock.step()
      p.clock.step()

    }
  }
}

class GEMMtest_8x8 extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "GEMM_8x8"
  it should "GEMM ex" in {
    test(new GEMM(size = 8, width = 8)).withAnnotations(Seq(WriteVcdAnnotation)) { p =>
      // 공치행렬 선언
      for (i <- 0 until 8) {
        for (j <- 0 until 8) {
          if (i == j) p.io.gain(i)(j).poke(1.U(8.W))
          else p.io.gain(i)(j).poke(0.U(8.W))
        }
      }
      p.clock.step()

      // data injection
      for (i <- 0 until 8) {
        for (j <- 0 until i+1) {
          p.io.in(j).poke(i.U(8.W)) // in 피크
        }
        p.clock.step()
      }

      for (i <- 8 until 16) {
        for (j <- i - 7 until 8) {
          p.io.in(j).poke(i.U(8.W)) // in 피크
        }
        p.clock.step()
      }

      // add clk
      for (i <- 0 until 20)
        p.clock.step()
    }
  }
}
