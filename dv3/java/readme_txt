there are 2 cct files here :

use ...fd  normally - it only links in the floppy part

use ...all for debugging the SMSQE dv3 hard disk drivers.
in that case :
	add the swindriver class to the java source code
	modify the monitor & trapdispatcher classes to include the driver and
	driver calls
	modify	dev8_smsq_java_driver_dv3e_asm to include hd_init
	the driver will be called WHN (not WIN).
