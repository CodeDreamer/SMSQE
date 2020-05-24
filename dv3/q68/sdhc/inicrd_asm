; Q68 Initilaise an SHDC card (via bit-banging)      1.03 (C) W. Lenerz 2017-2020
;
; 1.03	2020-03-29 get sysvars from sms base, use delay loop  (adapted from MK's code for qlsd)
; 1.02	2018-05-08 card type is set in sysvars.
; 1.01	try to detect SD cards as well.

	section dv3

	xdef	inicrd

	include 'dev8_keys_java'
	include 'DEV8_dv3_keys'
	include 'DEV8_keys_err'
	include 'DEV8_keys_sys'
	include 'DEV8_dv3_hd_keys'
	include 'dev8_keys_q68'
	include 'dev8_smsq_smsq_base_keys'

inireg reg     d1-d4/a2/a6

; algo :
; put into spi mode
; cmd0
; acmd41
; cmd58


;+++
; This routine initialises an sdhc card
; *** read/write via bit bang
;
;	d1 c  p card number (0 or q68_coff)
;	a3 c  p device defn block
;
;	status return 0 or ERR.MCHK
;---
inicrd	movem.l inireg,-(a7)
	move.l	sms.sysb,a6		; a6 = sysvars
	move.l	#q68_timer,a2		; point to Q68 40 MHz timer
	move.l	#q68_1sec,d0		; one second timeout
	move.l	(a2),d3 		; current time
lock_wait
	bset	#7,sys_cdiu(a6) 	; prevent poll from accessing device
	beq.s	lock_ok 		; got the lock, proceed
	move.l	(a2),d2 		; current time
	sub.l	d3,d2			; difference
	cmp.l	d0,d3			; time expired ?
	blt.s	lock_wait		; no
	movem.l (a7)+,inireg
	moveq	#err.fdiu,d0		; yes, device is still being used
	rts
lock_ok
	move.l	#mmc1_cs,a2		; point to card physical addresses
	add.w	d1,a2			; a2 points to the correct addresses for this drive
	sf	drv_cs(a2)
	move.l	d1,d4
	beq.s	set_typ
	moveq	#1,d4
set_typ add.w	#sys_q8ct,d4		; point card in sysvars
	sf	(a6,d4.w)		; preset that card is SDHC

;;;;;  debug code ;;;;;---------------------------------
	genif debug = 1

	cmp.l	#'gold',q68_jflg	; debug?
	bne.s	nodebug 		; no...->
	move.l	d0,-(a7)		; will be popped up in java
	moveq	#5,d0
	dc.w	jva.trpa		; debug
	bra	out			; and continue ok
nodebug
	endgen
;;;;; end debug code ;;;;;;-----------------------------

	st	drv_clk(a2)
	st	drv_dout(a2)

; now put card into SPI mode		; pretend to send many bytes, toggles clock
	moveq	#15,d2
inilp1	moveq	#-1,d0
	bsr	sendbyte
	dbf	d2,inilp1

	st	drv_cs(a2)		; enable the card

; send cmd0 101 times or until correct response
	moveq	#100,d2 		; nbr of loop iterations +1
inilp2	move.l	#$40000000,d0
	move.w	#$95,d1
	bsr	snd_cmd 		; send command 0 - put to idle
	bne.s	lp2dbf			; command sending went wrong ->
	subq.b	#1,d1			; sent OK, but correct response?
	beq.s	reply1			; ... yes ->
lp2dbf	dbf	d2,inilp2		; ... no, try again
	bra	ini_err 		; if we get here, no correct answer from card

; here I got a valid reply , so cmd0 was successful
; now try to send command8
reply1
	move.l	#$48000001,d0		; prepare cmd8
	move.w	#$aa87,d1
	bsr	snd_cmd 		; send command 8, return 1 + 4 more bytes
	bne	ini_old 		; tx error, could be old card
	cmp.b	#1,d1			; correct response?
	bne	ini_old 		; no, could be old card, sd v. 1
	bsr	readbyte		; 2
	bsr	readbyte		; 3
	bsr	readbyte		; 4
	move.b	d0,d2			; keep 4th response byte
	bsr	readbyte		; 5
	cmp.b	#1,d2			; 4th response byte must be 1
	bne.s	ini_err 		; but it isn't
	cmp.b	#$aa,d0 		; 5th response byte must be $aa
	bne.s	ini_err 		; but is isn't

; now do cmd55 followed by cmd41  (acmd41)
	move.w	#$9fff,d2		; should be enough on a slow machine

inilp3	move.l	#$77000000,d0		; send command 55 (special command to follow)
	move.w	#$87,d1
	bsr	snd_cmd
	bne.s	ini_err 		; pb on send command
	move.l	#$69400000,d0		; send acmd41 (sd_send_op_cond)
	move.w	#$87,d1
	bsr.s	snd_cmd 		; reply will be 0 if success
	bne.s	ini_err
	tst.b	d1			; check for correct reply from card
	beq.s	reply2			; got correct reply
	dbf	d2,inilp3		; didn't, so try again
	bra.s	ini_err 		; if we get here, reply never was 0
reply2
	move.l	#$7a000000,d0		; send command 58 -read ocr
	move.w	#$87,d1
	bsr.s	snd_cmd 		;
	bne.s	ini_err
	bsr.s	readbyte		; 1 get 4 more response bytes
	move.b	d0,d2			; keep response 1
	bsr.s	readbyte		; 2
	bsr.s	readbyte		; 3
	bsr.s	readbyte		; 4
	andi.b	#$40,d2 		; is this an SDHC card?
	bne.s	ini_ok			; ... yes, done

blk_sz	move.l	#$50000002,d0		; ... no, try to set block size
	move.w	#$ff,d1
	bsr.s	snd_cmd 		; set block size
	cmp.b	#0,d1			; correct reply?
	bne.s	ini_err
	move.b	#sys..q8sd,(a6,d4.w)	; card is SD card, in byte mode
     
ini_ok	moveq	#0,d0
ini_out clr.b	drv_cs(a2)		; unselect drive
out	movem.l (a7)+,inireg
	bclr	#7,sys_cdiu(a6) 	; release the sector xfer buffer & card
	tst.l	d0
	rts
		
ini_err st	(a6,d4.w)		; card type remains undetermined
	moveq	#err.mchk,d0
	bra.s	ini_out
	
;+++
; This routine reads a byte from an sdhc card through bit-banging.
; A byte is read bit by bit, the MSB comes first.
;
;      d0  r	byte read
;      d1    s
;      a2 c  p	pointer to interface base for the selected card
;
;      no error return
;+++

readbyte
	moveq	#7,d1			; dbf for 8 bits
	moveq	#0,d0			; d0 will contain result
rblp	clr.b	drv_clk(a2)		; set clock low
	tst.b	drv_din(a2)		; got a bit  ?
	beq.s	nextbit 		; ... no
	bset	d1,d0			; ... yes, show it
nextbit st	drv_clk(a2)		; set clock high
	dbf	d1,rblp
	rts

;+++
; This routine sends a command to an sdhc card. The command is 6 bytes long.
; Bytes are sent via bitbang.
; On entry d0 contains the first 4 bytes and d1.w the last two.
;
;	d0 cr	first 4 command bytes / error code
;	d1 cr	.W : 2 last command bytes / return byte
;	d3    s
;	a2 c  p pointer to interface base
;
;	status return 0 or ERR.MCHK (card didn't reply)
;---
; I could loop this, but it's just a few bytes anyway

snd_cmd
	move.w	d1,-(a7)		; sendbyte destroys d1
	move.l	d0,d3
	moveq	#-1,d0
	bsr.s	sendbyte		; (pretend to) send a byte (sends 8 clock pulses)
	move.l	d3,d0
	rol.l	#8,d0
	bsr.s	sendbyte		; send first byte
	rol.l	#8,d0
	bsr.s	sendbyte		; send next byte
	rol.l	#8,d0
	bsr.s	sendbyte		; send 3rd byte
	rol.l	#8,d0
	bsr.s	sendbyte		; send 4th byte
	move.w	(a7)+,d3
	move.w	d3,d0
	ror.w	#8,d0
	bsr.s	sendbyte		; penultimate byte
	move.b	d3,d0
	bsr.s	sendbyte		; last byte
; now wait for response
	move.w	#99,d3
waitlp	bsr.s	readbyte		; get response byte, myst be <>-1 if success
	cmp.b	#-1,d0			; was reply -1 (=bad)?
	bne.s	got_rply		; ...  no, so we're done ok
	dbf	d3,waitlp		; ...  yes, command not finished yet
	moveq	#err.mchk,d0		; if we get here, card didn't reply in time
	rts
got_rply
	move.b	d0,d1			; return byte
	moveq	#0,d0			; all OK
	rts


;++++
; This routine sends a byte to an sdhc card through bit-banging.
; A byte is sent bit by bit, the MSb comes first.
;
;      d0 c  p byte to send
;      d1    s
;      a2 c  p	pointer to interface base for the selected card
;
;      no error return
;++++

sendbyte
	moveq	#7,d1			; dbf for 8 bits
sb_lp
	btst	d1,d0			; is this bit set?
	beq.s	not_set 		; ... no
	st	drv_dout(a2)		; ...yes output to high, bit was set
	bra.s	common
not_set clr.b	drv_dout(a2)		; output to low, bit was not set
common	clr.b	drv_clk(a2)		; clock low
	st	drv_clk(a2)		; clock high
	dbf	d1,sb_lp		; do for all bits
	rts

;++++++++++++++++++++++++++++++
;
; try to init a v.1 card
;
;++++++++++++++++++++++++++++++

ini_old 				; do acmd41
	move.w	#$9fff,d2		; should be enough on a slow machine
inilp4	move.l	#$77000000,d0		; send command 55 (special command to follow)
	move.w	#$87,d1
	bsr.s	snd_cmd
	bne	ini_err 		; pb on send command
	move.l	#$69400000,d0		; send acmd41 (sd_send_op_cond)
	move.w	#$87,d1
	bsr.s	snd_cmd 		; reply will be 0 if success
	bne	ini_err 		; if not, real problem
	tst.b	d1			; check for correct reply from card
	beq	blk_sz			; got correct reply, set block size
	dbf	d2,inilp4		; didn't, so try again
	bra	ini_err 		; if we get here, reply never was 0

	end
	      















;; debug

err1	moveq	#-1,d0
	bra	ini_out

err2	moveq	#-2,d0
	bra	ini_out

err3	moveq	#-3,d0
	bra	ini_out

err4	moveq	#-4,d0
	bra	ini_out

err5	moveq	#-5,d0
	bra	ini_out

err6	moveq	#-6,d0
	bra	ini_out

err7	moveq	#-7,d0
	bra	ini_out

err8	moveq	#-8,d0
	bra	ini_out

			   

err9	moveq	#-9,d0
	bra	ini_out

err10	 moveq	 #-10,d0
	bra	ini_out

err11	 moveq	 #-11,d0
	bra	ini_out

			   

	end

 
void GenerateCRCTable()
{
  int i, j;
  uint8_t CRCPoly = 0x89;  // the value of our CRC-7 polynomial
 
  // generate a table value for all 256 possible byte values
  for (i = 0; i < 256; ++i) {
    CRCTable[i] = (i & 0x80) ? i ^ CRCPoly : i;
    for (j = 1; j < 8; ++j) {
	CRCTable[i] <<= 1;
	if (CRCTable[i] & 0x80)
	    CRCTable[i] ^= CRCPoly;
    }
  }
}
 
// adds a message byte to the current CRC-7 to get a the new CRC-7
uint8_t CRCAdd(uint8_t CRC, uint8_t message_byte)
{
    return CRCTable[(CRC << 1) ^ message_byte];
}
 
// returns the CRC-7 for a message of "length" bytes
uint8_t getCRC(uint8_t message[], int length)
{
  int i;
  uint8_t CRC = 0;

  for (i = 0; i < length; ++i)
    CRC = CRCAdd(CRC, message[i]);

  return CRC;
}
