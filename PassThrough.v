module PassThrough(
  input        clock,
  input        reset,
  input  [3:0] io_in,
  output [3:0] io_out
);
  assign io_out = io_in; // @[PassThrough.scala 9:10]
endmodule
