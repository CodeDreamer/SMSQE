; Gold card patch I2C

	section patch

	xdef	I2C
	xdef	I2C_entry
	xdef	I2C_raw
;+++
; New code for I2C drivers
;---
I2C
* Version for the Miracle Gold Card:
* 16MHz clock.
* R/W takes 4 cycles, with a whole word accessed.
* Every ninth read causes an extra 2 cycles for RAM refresh.
* Byte accesses to the ROM take 11 cycles
* Instead of the normal 4 cycles, accesses involving the ROM take 11 cycles for
* a byte or 19 cycles for a word

* Timing requirements for the i2c bus:

clock equ 16000 KHz, kilo cycles per second CPU clock rate

cycles_ns macro n ; equate label to number of cycles for required nanoseconds
[.lab] equ (([n])*clock-1)/1000000+1 round up
 endm

* f SCL (100kHz) must not be exceeded.
t_BUF cycles_ns 4700+1000 ; is fine, as we'll be between controls.
t_HD_STA cycles_ns 4000+300 ; is similar.
t_LOW cycles_ns 4700+300 ; is one main constraint.
t_HIGH cycles_ns 4000+1000 ; is the other main constraint.
t_SU_STA cycles_ns 4700+1000 ; is "between controls", so it's easy.
* t_HD_DAT we will assume zero, as we're not into CBUS compatibility (yet?).
* t_SU_DAT cycles_ns 250 ; means we can use ".w" instructions
* N.B. If the ROM byte accessing were speeded up by more than a factor of two,
* the use of ".w" instructions would have to be dropped.
t_SU_STO cycles_ns 4700+1000 ; we can cope with.
t_HEAVY cycles_ns 5000 ; the largest of the above, for our kill bus code

* Our waveform for SCL should come out as 80/80 cycles, to get it all right.

* We will work on the basis of using no write accesses to RAM, so we can use
* this code during boot up.

* While addressing a device, we will check that we are seeing on SDAin what we
* are sending on SDAout, and return ERR.FF if there is a mismatch, i.e. the
* board isn't an I2C Minerva.

* If the acknowledge for a device fails we report ERR.NF which on boot up will
* just mean the battery backed up RAM and clock is not fitted.

* The first thing on boot up will be to waggle SDA and SCL about with an
* "initialise" sequence, just in case the machine has been rebooted in the
* midst of a read sequence.

* The RAM/clock chip is device $A0 giving a good chance of recognising old
* Minerva boards, as we will be expecting to see a mix of ones and zeros on
* SDAin as we address it.

* We will try to make the interface pretty clever, but not TOO clever.
* Sequencing will be done by bytes from a read only "control" buffer.

* The device address will be held in a register, but a control will enable
* this to be replaced from the control buffer.

* The crux of the matter comes when we are asked to do multi-byte transfers to
* or from a RAM buffer. This part of the code is the only part that MUST be
* able to run as close to the maximum clock rate as we can manage. All other
* actions can limp along as slow as is convenient.

* It will be possible to carry out a sequence containing multiple reads and/or
* writes in an uninterruptable fashion. The prime one needed at present being
* to suspend the clock by writing to location 0, reading back locations and
* then releasing the clock to count again. Although this could be done by
* several calls, we will allow for it to be done in a single call, saving us
* some time when time is in fact a little important: we would like to be able
* to use the 1/100th second timing information with some accuracy.

* Write sequences will have the option of sending data bytes from either the
* data buffer or the control buffer. When reading, the bytes will go to either
* the data buffer or be rolled into a register.

* The control buffer will be organised as a series of individual bytes:

* Parameter build byte:
*
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  7  |  6  |  5  |  4	|  3  |  2  |  1  |  0	| 
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  0  |        seven parameter data bits	|
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*
* The contents of the parameter byte are shifted left seven bits, and this byte
* is "OR"'ed into it. A contiguous sequence of five of these can be used to set
* up a full 32 bits of parameter. Only two uses of this are currently made.
* A single byte is used before a special command which is to copy it to the
* device group register, so we can change devices during a sequence. The other
* usage is to set up the byte count for a normal i/o command. This will only
* make use of a 16-bit count, and may need anything from zero to three of these
* parameter build bytes. The parameter register is always cleared to zero after
* each of the normal i/o and special byte types has been processed.

* Normal input/output byte:
*
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  7  |  6  |  5  |  4	|  3  |  2  |  1  |  0	| 
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  1  |  0  |  S  |  R	|  B  |  P  |  A  |  0	|
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*
* The bits of this byte are essentially handled from left to right, to allow
* the most typical i/o sequence to be handled in its entirety.
*
* S = 0: no START required
* S = 1: send START and device
* R = 0: write mode, or R = 1: read mode
* B = 0: if R=0, write from control, or R=1 read to register
* B = 1: write/read uses data buffer
* P = 1: send STOP sequence
* A = 1: send acknowledge on last read (R=1) byte
*
* R=0 and A=1 is invalid, as is R=1, P=1 and A=1. Also bit 0 must be clear. If
* these conditions are not met, an err.bp is reported after processing all but
* the P bit.
*
* The parameter value specifies the exact byte count for a write sequence, but
* on a read (R=1) sequence, it counts only those bytes to be acknowledged. If
* R=1 and A=0, the final byte with standard non-acknowledge is extra.

* Write sequence data byte:
*
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  7  |  6  |  5  |  4	|  3  |  2  |  1  |  0	| 
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|		    data byte			|
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*
* If a normal i/o byte requests writes from this control buffer, it will be
* immediately followed by the appropriate number of data bytes to be written.

* Special i/o and control byte:
*
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  7  |  6  |  5  |  4	|  3  |  2  |  1  |  0	| 
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*	|  1  |  1  |  G  |  V	|  D  |  C  |  1  |  Q	|
*	+-----+-----+-----+-----+-----+-----+-----+-----+
*
* Once again, the bits are handled from left to right, and these control all
* the exceptional cases we wish to cope with. Note that the SDA and SCL setting
* will occur simultaneously, hence to be valid, only one should differ from its
* currently known state. If V=0, the state will always be both ones before they
* are applied, so the combination of V, D and S all zero is always invalid.
*
* G = 0: set device group addresses as 2 * current parameter value
* G = 1: assume device group is already in its register
* V = 0: kill bus (assume NOTHING about bus, ensure in standard free state)
* V = 1: assume the bus is valid, whatever state it is in
* D = d: set SDA 
* C = c: set SCL
* Q = quit
*
* Note that bit 1 is reserved and must be set, or an err.bp is reported after
* precessing the G and V bits, but before setting the D/C combination
*
* The control buffer must finish up with a special command that has its quit
* (lsb) set. Normally this will be all ones, but where the bus is not being
* released between calls a value of $FB, keeping SDA high and SCL low, will be
* typical.

* The general rules for the bus go as follows:
* Before a START+device, SDA should be high.
* After a START+device, SDA will be high and SCL will be low.
* For a read/write, SDA high and SCL low are required and are left the same.
* Before a STOP, SCL low is expected and both SDA and SCL will be left high.
* Before an initialise, SDA and SCL are irrelevant, after, they are high.
* When using the "special" command, only one of SDA and SCL should be changed
* at one time. When it includes an initialise, both should not be sent low.

* When errors are returned, the rules are modified.
* An err.ff means that the bus did not seem to be responding to the device
* address correctly. This should mean it's an old Minerva board.
* An err.nf just means that the addressed i2c device is not present. A stop
* sequence will have been sent to leave SDA and SCL high.
* An err.te means a write sequence failed to get its acknowledge on a byte,
* and a STOP will have been issued to leave SDA and SCL high.
* An err.bp means the control buffer had a duff command in it. A normal i/o
* sequence will not have had any STOP sent and a special will have left SDA and
* SCL unchanged unless an initialise was done, when both will be high.

* The call sequence for this code should be in supervisor mode with interrupts
* turned off for maximum speed. If speed is not a constraint, it may not only
* be called with interrpts on, and even in user mode, but, as it preserves a6,
* it may then even be called direct from SuperBasic.

* The "front end" entry saves more registers, on the assumption that there is
* a valid stack available. Eight bytes further in is the entry point that zaps
* loads more registers, and so on, but requires NO stack.

* Registers are expected as follows:

* d0 Returns (with CCR) any error code, zero for success.
* d1 If read to register, bytes roll in here, lsb is last. Otherwise untouched.
* d2 0 or maybe device and/or maybe (msb's) of first parameter (see below)
* d3 On return, the control buffer address, updated to wherever we got to.
* d4 Destroyed.
* d5 Lsw only destroyed.
* d6 Lsb only destroyed.
* d7 Untouched.
* a0 The return address.
* a1 Data buffer address. If used, it will be updated, otherwise untouched.
* a3 On entry, the control buffer address.
* a2-a5 These are set here to the four byte addresses of the interface.
* a6 Untouched.
* a7 Untouched.

* The device and/or part or all of the initial parameter may be passed in d2.
* An initial device should be in bits 22-16, and the initial parameter may be
* in bits 15-0, or partially in bits 8-0 or bits 1-0.
* If the command byte sequence sets the device, bits 31-16 will be copied from
* the current value of the parameter (bits 15-0), then it will be zeroed, as it
* will be after any normal i/o sequence.
* The parameter value (bits 15-0) is rolled left 7 bits as each new parameter
* setting byte (positive) is encountered, to include it.
* On exit, bits 31-16 will be as one might expect, containing whatever was
* there to start with, or the copied parameter value if the device was set.
* Bits 15-0 will be the final value of the parameter, at whatever point the
* sequence was terminated. Typically, after a succesful transfer of data, it
* will be zero.

err.nf	equ	-7
err.te	equ	-13
err.ff	equ	-14
err.bp	equ	-15

* front end for somewhat less destructive access. Preserves d3-d6, a0/a2/a4-a5.
drv_end
	move.l	d3,a3
	movem.l (sp)+,d3-d6/a0/a2/a4-a5
	rts

;
; Vector II_DRIVE at $172 points to here-$4000: Gold Card should therefore
; overwrite ($172)+$3FF8 with this code (I think!) J.O.
;
I2C_entry
ii_drive				  
	movem.l d3-d6/a0/a2/a4-a5,-(sp)   
	lea	drv_end,a0

; vector II_RAW at $170 pointes to here-$4000
* entry point needing no RAM at all
I2C_raw
ii_raw
	nop
	nop
;**	   moveq   #err.ff,d0
;**	   jmp	   (a0)

	move.l	a3,d3
	lea	$BFDC,a4
	lea	1(a4),a5
	lea	2(a4),a2
	lea	3(a4),a3
	bra.s	new_cmd

* macro to waste time, specified as cycles.
waste macro r,n
 ifnum [n] < 1 goto w0
 ifnum [n] < 5 goto w4
 ifnum [n] < 7 goto w6
 ifnum [n] < 23 goto wshort
 ifnum [n] < 25 goto w24
 ifnum [n] < 139 goto wlong
 error have no macro for a wait this long
wlong maclab
	moveq	#([n]-11)/2,[r] 4 cycles
	lsr.l	[r],[r]   8+c*2 cycles (c<64)
 goto wend
w24 maclab
	lsr.l	#8,[r]	  8+8*2 cycles (longest single word instruction)
 goto wend
wshort maclab
	lsr.b	#([n]-5)/2,[r] 6+c*2 cycles
 goto wend
w6 maclab
	clr.l	[r]	      6 cycles
 goto wend
w4 maclab
	nop		      4 cycles
 goto wend
w0 maclab
 ifnum [n] > -2 goto wend
 warning [n] cycles requested
wend maclab
 endm

*	|  1  |  1  |  G  |  V	|  D  |  C  |  1  |  Q	|
special
	bmi.s	grpok	  b10 8 skip if G (group) bit is already set
	swap	d2	      4 set device group data
	clr.w	d2	      4 zero the parameter value
grpok
	moveq	#9,d0	      4 counter and msb's zero
	asl.b	#2,d6	     10 check V bit
	bcs.s	verok	  b10 8 skip if V (validate) bit is set
	moveq	#(t_HEAVY-15-6+1)/2,d4
verlp
	tst.b	(a2)	     15 SDA high after 1st, set SCL low
			;10 ===
	rol.b	d4,d0  t_LOW-15 at least
	tst.b	(a3)	     15 SDA high, set SCL high = NAK
			;11 ===
	ror.b	d4,d0  t_BUF-15 at least
	tst.b	(a5)	     15 SCL high, set SDA low = START
			;01 ===
	rol.b	d4,d0 SU_STO-15 at least
	tst.b	(a3)	     15 SCL high, set SDA high = STOP
			;11 ===
	ror.b	d4,d0  t_BUF-29 at least
	subq.b	#1,d0	      4
	bne.s	verlp	  b10 8 execute kill sequence 9 times
verok
	move.b	d6,d0	      4
	lsr.b	#6,d0	     18 put D/C bits into register
	bcc.s	err_bp	  b10 8 object to spare bit zero
	tst.b	0(a4,d0.w)   21 set D/C as requested
			;DC ===
	lsl.b	#4,d6	     14
	bcc.s	new_cmd   8 b10
	moveq	#0,d0
	jmp	(a0)		return OK

* 101xxxxx - send start sequence plus device
dostart
	tst.b	(a3)	     15 SDA high, assure that SCL is high
			;11 ===
	waste	d0  t_SU_STA-15
	tst.b	(a5)	     15 SCL high, set SDA low for START
			;01 ===
	waste	d0  t_HD_STA-51
	move.l	d2,d4	      4 copy device
	lsr.l	#8,d4	     24 shift down to bits 15-8
	move.b	d6,d4	      4 copy command, bit 7 = 1, bit 6 = W/R
	add.b	d4,d4	      4 W/R into bit 7, X=1
	tst.b	(a4)	     15 SDA low, set SCL low
			;00 ===
	roxr.w	#7,d4	     20 bit 9 = 1, bits 7-1 device, bit 0 = W/R 
	bra.s	wdev	    b10 go use common code

parm
	lsl.w	#7,d2		shift any existing parameter data up
	or.b	d6,d2		put in next seven bits
new_cmd
	exg	a1,d3	      6
	move.b	(a1)+,d6      8 get next command byte
	exg	a1,d3	      6
	bpl.s	parm	  b10 8 if msb is clear, rest is 7 bits of parameter
	asl.b	#2,d6	     10 check next two bits, 6=special, 5=normal
	bcs.s	special   b10 8 11xxxxxx - go do special command sequence
	bmi.s	dostart   b10 8 101xxxxx - start sequence wanted
started
	moveq	#0,d4	      4 flag to say started (for write addr/data)
	asl.b	#2,d6	     10 roll out R/W and buffer to msb
	bcs.l	reader	 b10 12 go do read sequence
	bmi.s	writer	  b10 8 if write from control buffer, we're ready
	exg	a1,d3	      6 use data buffer instead of command buffer
	bra.s	writer	    b10 go start writing from data buffer

wloop
	move.b	(a1)+,d4      8 get next byte from buffer
wdev
	clr.w	d5	      4 clear data offset
	add.b	d4,d4	      4 roll out first bit
	roxl.b	#2,d5	     10 set 0 for SDA low, 2 for SDA high
	addq.b	#1,d4	      4 1st time set marker in lsb
wbent
	waste	d0     t_LOW-81 ; 2+ cycle refresh is not considered
	tst.w	0(a4,d5.w)   29 SCL low, set SDA data, then set SCL high
			;D1 ===
	move.b	1(a4,d5.w),d0 21 SDA data, SCL high, get SDAin
	bchg	d5,d0	      8 if SDAout is 0, toggle SDAin
	lsl.b	#7,d0	     20 see if SDAin = SDAout (waste a bit)
	bmi.s	wmatch	  b10 8 if matched, we have may have duff ack!
	waste	d0    t_HIGH-57
	tst.b	(a2)	     15 SDA high, set SCL low
			;10 ===
	tst.w	d4	      4 check if device address or NOT the ack
	bne.s	sortit	  b10 8 if data write and proper ack, go do next
writer
	dbra	d2,wloop b10 14 continue with data write
	add.b	d6,d6	      4 did we swap control/data buffers
	bcs.s	chkstop   b10 8 no, skip swap
	exg	a1,d3	      6 put back control and data pointers
chkstop
	clr.w	d2	      4 reset parameter
	add.b	d6,d6	      4 do we want to send a stop
	beq.s	chkcmd	  b10 8 spare bit(s) are clear, so command was valid
err_bp
	moveq	#err.bp,d0
	jmp	(a0)

sortit
	tst.b	d4	      4 check if this was the ack for the device
	beq.s	started   b10 8 if so, get on back to decoding this command
err_ff
	moveq	#err.ff,d0	say that the i2c is not working
	; as this is disastrous, don't get worked up about timing, etc!
err_com
	tst.b	(a4)	     15 SCL low, set SDA low
	waste	d4     t_LOW-70 ; 72 74
	bra.s	stop	    b10 common stop code will notice d0<>0

chkcmd
	bcc.s	new_cmd   b10 8 no stop, leave SDA high and get next command
	tst.b	(a4)	     15 send SDA low
	moveq	#0,d0	      4 after the stop has been sent, do next command
stop
	tst.b	(a5)	     15 SDA low, set SCL high
			;01 ===
	waste	d4  t_SU_STO-15
	tst.b	(a3)	     15 SCL high, set SDA high: STOP
			;11 ===
	tst.l	d0	      4 make sure CCR is set for errors
	beq.s	new_cmd     b10 go get next command
	jmp	(a0)		go home

wbloop
	clr.w	d5	      4 clear data offset
	add.b	d4,d4	      4 roll out next bit
	roxl.b	#2,d5	     10 set 0 for SDA low, 2 for SDA high
	bra.s	wbent	    b10 enter main loop

wmatch
	waste	d0    t_HIGH-80
	tst.b	0(a4,d5.w)   21 SDA data, set SCL low
			;D0 ===
	tst.b	d4	      4 check if this was still a data bit
	bne.s	wbloop	  b10 8 nice data bit, so keep steaming along
	moveq	#err.nf,d0    4 if nack on a device address, just say not found
	tst.w	d4	      8 check if this was the device address
	bne.s	err_com   b10 8 it was a device address, so stop all this
err_te
	moveq	#err.te,d0    4 transmission error if device nack's our data!
	bra.s	err_com     b10

* read sequence

rloop
	waste	d0     t_LOW-58
	move.b	(a3),d5      15 SDA high, set SCL high and sample SDAin
			;11 ===
	waste	d0    t_HIGH-15
	tst.b	(a2)	     15 SDA high, set SCL low
			;10 ===
	waste	d0	     21 balance loop
	asr.b	#1,d5	      8 put data into X
	addx.b	d4,d4	      4 roll into result byte
rslow
	bcc.s	rloop	  b10 8 if not at marker yet, loop
	waste	d0     t_LOW-64
	tst.w	(a4)	     23 SCL low, set SDA low, set SCL high
		;00	;01 === 
	rol.b	#8,d6	     22 check if we are reading to memory (waste a bit)
	bmi.s	rmem	  b10 8 if so, go put it in
	rol.l	#8,d1	     24 roll data return
	move.b	d4,d1	      4 put in byte
rrent
	waste	d0    t_HIGH-73
	tst.b	(a4)	     15 SDA low, set SCL low
			;00 ===
	tst.b	(a2)	     15 SCL low, set SDA high
reader
	moveq	#1,d4	      4 set marker bit
	subq.w	#1,d2	      4 byte count (waste a bit)
	bcc.s	rslow	  b10 8 get to rloop slower (waste a bit)
	bclr	#5,d6	     14 check acknowledge bit
chkstp
	bne.s	chkstop   b10 8 final byte acknowledged, STOP not permitted
rnlp
	waste	d0     t_LOW-68
	move.b	(a3),d5      15 SDA high, set SCL high and sample SDAin
			;11 ===
	waste	d0    t_HIGH-15
	tst.b	(a2)	     15 SDA high, set SCL low
			;10 ===
	waste	d0	     -1 balance loop
	asl.b	#8,d5	     22 roll data out into X (waste a bit)
	roxr.b	#8,d4	     22 roll SDAin into register
	bcc.s	rnlp	  b10 8 if not at marker yet, loop
	waste	d0     t_LOW-66
	move.b	(a3),d5      15 SDA high, set SCL high for no ack, get SDAin
			;11 ===
	asl.b	#1,d6	      8 check if we are reading to memory (waste a bit)
	bcs.s	rnmem	  b10 8 if so, go put it in
	rol.l	#8,d1	     24 roll data return
	move.b	d4,d1	      4 put in byte
rnrent
	waste	d0    t_HIGH-59
	tst.b	(a2)	     15 SDA high, set SCL low
			;10 ===
	ror.b	#1,d5	      8 look at final SDAin
	bpl.s	err_te	 b10 14 be annoyed if our nack was not seen!
	bra.s	chkstp	    b10 if nack was OK, go check for stop required

rmem
	waste	d0	      8 balance loop
	move.b	d4,(a1)+      8 store this byte (slower if screen memory)
	bra.s	rrent	    b10

rnmem
	waste	d0	      8 balance loop
	move.b	d4,(a1)+      8 store last byte (slower if screen memory)
	bra.s	rnrent	    b10

	end
