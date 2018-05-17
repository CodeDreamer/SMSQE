; SER device initialisation  V2.02    1989  Tony Tebby   QJUMP

; Interrupt version for Atari

	section ser

	xdef	ser_init

	xref.l	ser_vers
	xref	iou_idset
	xref	iou_idlk
	xref	iou_lkvm

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_scc'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_par'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'
;+++
; SER driver initialisation.
;
;	a3 c  p   PAR linkage block / SER1 linkage block
;	status return 0
;---
ser_init
seri.reg reg	d1/d2/d3/a0/a1/a2/a3/a4/a5/d7
	movem.l seri.reg,-(sp)
	move.w	sr,d7
	trap	#0
	move.w	#$2700,sr		 ; no interrupts!

	lea	prd_ser1-iod.sqhd(a3),a0
	lea	ser_link,a3		 ; setup ser linkage
	jsr	iou_idset

	moveq	#sms.info,d0
	trap	#1
	moveq	#sys.mtyp,d1
	and.b	sys_mtyp(a0),d1 	 ; machine type

	lea	ser_tabst,a5		 ; tables for ST
	cmp.b	#sys.mmste,d1		 ; mega ste?
	blt.s	seri_setup		 ; ... no
	beq.s	seri_mste

	cmp.b	#sys.mtt,d1		 ; TT
	bne.s	seri_setup		 ; ... no
	lea	ser_tabtt,a5		 ; tables for TT
	bra.s	seri_4port

seri_mste
	lea	ser_tabste,a5		 ; tables for mega ste

seri_4port
	addq.b	#3,prd_port(a3) 	 ; three extra ports

seri_setup
	addq.b	#1,prd_port(a3) 	 ; the basic port
	moveq	#0,d1
	move.l	a5,a4
	add.w	(a5)+,a4		 ; vector table
	bra.s	seri_nvec

seri_vec
	move.w	(a4)+,d1		 ; offset in linkage
	move.l	a4,a1
	add.w	(a4)+,a1		 ;   - address of server
	add.w	d2,a3
	jsr	iou_lkvm		 ; link minimum vectored server
	sub.w	d2,a3
	move.w	(a4)+,a2
	move.l	a1,(a2) 		 ; set vector
seri_nvec
	move.w	(a4)+,d2		 ; vectored interrupt - linkage offset
	bge.s	seri_vec

	move.l	a5,a4
	add.w	(a5)+,a4		 ; set table
	bra.s	seri_nset

seri_set
	move.l	(a4)+,(a3,d1.w) 	 ; set value
seri_nset
	move.w	(a4)+,d1
	bne.s	seri_set

	move.l	a5,a4
	add.w	(a5)+,a4		 ; address table
	bra.s	seri_nadd

seri_add
	move.l	a4,a1
	add.w	(a4)+,a1
	move.l	a1,(a3,d1.w)		 ; set address
seri_nadd
	move.w	(a4)+,d1
	bne.s	seri_add

	move.l	a5,a4
	add.w	(a5),a4 		 ; the last table is the hardware table
	bra.s	seri_nhw

seri_hw
	jsr	-2(a4,d1.w)		 ; do hw specific initialisation
seri_nhw
	move.w	(a4)+,d1
	bne.s	seri_hw

;	 lea	 seri_spca,a1		  ; special condition a
;	 move.l  a1,scc_vbas+vio_scasc
;	 lea	 seri_spcb,a1		  ; special condition b
;	 move.l  a1,scc_vbas+vio_scbsc
	move.l	scc_vbas+vio_scarc,scc_vbas+vio_scasc
	move.l	scc_vbas+vio_scbrc,scc_vbas+vio_scbsc

	jsr	iou_idlk		 ; link in

	move.w	d7,sr
	movem.l (sp)+,seri.reg
	moveq	#0,d0
	rts


; Special condition servers

seri_spca
	move.b	#sccc.rhiu,scc_ctra	; clear in service
	rte
seri_spcb
	move.b	#sccc.rhiu,scc_ctrb	; clear in service
	rte

; SET MFP
;	word		    base address of mfp
;	table of 2 bytes    offset and data to set
;	zero byte
;	table of 2 bytes    offset and data to OR
;	zero byte

sers_mfp
	moveq	#0,d1
	move.w	(a4)+,a1		 ; address of MFP
	bra.s	sers_nsmfp
sers_smfp
	move.b	(a4)+,(a1,d1.w) 	 ; set byte
sers_nsmfp
	move.b	(a4)+,d1		 ; next offset
	bne.s	sers_smfp

	bra.s	sers_nomfp
sers_omfp
	move.b	(a4)+,d0
	or.b	d0,(a1,d1.w)		 ; or byte
sers_nomfp
	move.b	(a4)+,d1		 ; next offset
	bne.s	sers_omfp

	rts

; SET SCC
;	long		    base address of scc
;	table of 2 bytes    offset and data to set
;	zero word

sers_scc
	moveq	#0,d1
	move.w	(a4)+,a1		 ; address of SCC Control reg
	bra.s	sers_nsscc
sers_sscc
	move.b	(a4)+,(a1)		 ; set byte
sers_nsscc
	move.b	(a4)+,(a1)		 ; select register
	bne.s	sers_sscc

	addq.l	#1,a4
	rts

; TABLES

ser_link
	dc.l	0	   ; pre-allocated
	dc.l	1<<iod..ssr+1<<iod..scn serial and name

	novec		   ; no xint
	vecx	ser_poll   ; poll
	novec		   ; no sched
	vecx	ser_io	   ; but a full set of opens
	vecx	ser_open
	vecx	ser_close

	vecx	ser_cnam

; vectors

s1	equ	prd_ser1-prd_ser1
s2	equ	prd_ser2-prd_ser1
s3	equ	prd_ser3-prd_ser1
s4	equ	prd_ser4-prd_ser1

ser_vectt
	dc.w	s3			 ; linkage offset
	dc.w	prd_veci		 ; receive vectored interrupt - offset
	vecx	mfp2_intr		 ;   - address of server
	dc.w	mfp2_vbas+vio_srbf	 ;   - vector

	dc.w	s3			 ; linkage offset
	dc.w	prd_veco		 ; transmit vectored interrupt - offset
	vecx	mfp2_intt		 ;   - address of server
	dc.w	mfp2_vbas+vio_stbe	 ;   - vector

;	 dc.w	 s3			  ; linkage offset
;	 dc.w	 prd_vech		  ; handshake vectored interrupt - offset
;	 vecx	 mfp2_intc		  ;   - address of server
;	 dc.w	 mfp2_vbas+vio_cts	  ;   - vector

ser_vecste
	dc.w	s4			 ; linkage offset
	dc.w	prd_veci		 ; receive vectored interrupt - offset
	vecx	scca_intr		 ;   - address of server
	dc.w	scc_vbas+vio_scarc	 ;   - vector

	dc.w	s4			 ; linkage offset
	dc.w	prd_veco		 ; transmit vectored interrupt - offset
	vecx	scca_intt		 ;   - address of server
	dc.w	scc_vbas+vio_scate	 ;   - vector

	dc.w	s4			 ; linkage offset
	dc.w	prd_vech		 ; handshake vectored interrupt - offset
	vecx	scca_intc		 ;   - address of server
	dc.w	scc_vbas+vio_scaxs	 ;   - vector

	dc.w	s2			 ; linkage offset
	dc.w	prd_veci		 ; receive vectored interrupt - offset
	vecx	sccb_intr		 ;   - address of server
	dc.w	scc_vbas+vio_scbrc	 ;   - vector

	dc.w	s2			 ; linkage offset
	dc.w	prd_veco		 ; transmit vectored interrupt - offset
	vecx	sccb_intt		 ;   - address of server
	dc.w	scc_vbas+vio_scbte	 ;   - vector

	dc.w	s2			 ; linkage offset
	dc.w	prd_vech		 ; handshake vectored interrupt - offset
	vecx	sccb_intc		 ;   - address of server
	dc.w	scc_vbas+vio_scbxs	 ;   - vector

ser_vecst
	dc.w	s1			 ; linkage offset
	dc.w	prd_veci		 ; receive vectored interrupt - offset
	vecx	mfp_intr		 ;   - address of server
	dc.w	mfp_vbas+vio_srbf	 ;   - vector

	dc.w	s1			 ; linkage offset
	dc.w	prd_veco		 ; transmit vectored interrupt - offset
	vecx	mfp_intt		 ;   - address of server
	dc.w	mfp_vbas+vio_stbe	 ;   - vector

	dc.w	s1			 ; linkage offset
	dc.w	prd_vech		 ; handshake vectored interrupt - offset
	vecx	mfp_intc		 ;   - address of server
	dc.w	mfp_vbas+vio_cts	 ;   - vector

	dc.w	-1

ser_settt
	assert	prd_sere,prd_serx-2,prd_hand-3
	dc.w	prd_sere+s3,$ff00,$0300  ; set SER enabled, port and handshake
	dc.w	prd_room+s3,0,prd.room	 ; set room
	dc.w	prd_ilen+s3,0,prd.ilen	 ; and size
	dc.w	prd_hwb+s3,at_mfp2,$0200 ; MFP2
ser_setste
	assert	prd_sere,prd_serx-2,prd_hand-3
	dc.w	prd_sere+s4,$ff00,$04ff  ; set SER enabled, flag and handshake
	dc.w	prd_room+s4,0,prd.room	 ; set room
	dc.w	prd_ilen+s4,0,prd.ilen	 ; and size
	dc.w	prd_hwb+s4,scc_ctra,$ff00 ; SCC channel A

	assert	prd_sere,prd_serx-2,prd_hand-3
	dc.w	prd_sere+s2,$ff00,$02ff  ; set SER enabled, flag and handshake
	dc.w	prd_room+s2,0,prd.room	 ; set room
	dc.w	prd_ilen+s2,0,prd.ilen	 ; and size
	dc.w	prd_hwb+s2,scc_ctrb,$ff00 ; SCC channel B
ser_setst
	assert	prd_sere,prd_serx-2,prd_hand-3
	dc.w	prd_sere+s1,$ff00,$01ff  ; set SER enabled, flag and handshake
	dc.w	prd_room+s1,0,prd.room	 ; set room
	dc.w	prd_ilen+s1,0,prd.ilen	 ; and size
	dc.w	prd_hwb+s1,at_mfp,$0000  ; MFP
	dc.w	0

ser_addtt
	dc.w	prd_iopr+s3
	vecx	ser_rxen		 ; set receive enable
	dc.w	prd_oopr+s3
	vecx	mfp2_oact		 ; and activate xmit
ser_addste
	dc.w	prd_iopr+s4
	vecx	ser_rxen		 ; set receive enable
	dc.w	prd_oopr+s4
	vecx	scca_oact		 ; and activate xmit
	dc.w	prd_iopr+s2
	vecx	ser_rxen		 ; set receive enable
	dc.w	prd_oopr+s2
	vecx	sccb_oact		 ; and activate xmit
ser_addst
	dc.w	prd_iopr+s1
	vecx	ser_rxen		 ; set receive enable
	dc.w	prd_oopr+s1
	vecx	mfp_oact		 ; and activate xmit

	dc.w	0


ser_hwtt
	vec	sers_mfp		 ; set MFP 2
	dc.w	at_mfp2
	dc.b	mfp_uctl,mfp.npty+mfp.lstp+mfp.8bit+mfp.adiv; set usart
					 ; no parity, 1.5 stop bits, 8 bit async
	dc.b	mfp_rstt,0		 ; receiver disabled
	dc.b	mfp_tstt,0		 ; transmitter disabled
	dc.b	mfp_rstt,mfp.rxen	 ; receiver enabled
	dc.b	mfp_tstt,mfp.txen	 ; transmitter enabled
	dc.b	0

	dc.b	mfp_iena,1<<mfp..rxi+1<<mfp..txi ; enable interrupts
	dc.b	mfp_mska,1<<mfp..rxi+1<<mfp..txi ; unmask interrupts
	dc.b	0

ser_hwste
	vec	sers_scc		 ; set SCC
	dc.w	scc_ctra		 ; channel A
	dc.b	scc_wmictl,scci.rest		 ; reset both channels	    9

	dc.b	scc_wmode,sccm.cl16+sccm.lstp	 ; 1.5 stop bits	    4
;	 dc.b	 scc_wint,0;			  ; no rx tx interrupts      1
	dc.b	scc_wvec,scc_vbas>>2		 ; vector		    2
	dc.b	scc_wrctl,sccr.8bit		 ; rx 8 bit disabled	    3
	dc.b	scc_wtctl,scct.8bit		 ; tx 8 bit enabled	    5
	dc.b	scc_wmictl,scci.vis		 ; vector and no interrupt  9
	dc.b	scc_wclk,sccc.tcbd+sccc.rcbd	 ; clocks from baud gen    11
	dc.b	scc_divl,10			 ; 9600 		   12
	dc.b	scc_divh,0			 ;			   13
	dc.b	scc_wgctl,0			 ; no baud rate gen	   14
	dc.b	scc_wgctl,sccg.ebdg		 ; enable baud rate gen    14
	dc.b	scc_wrctl,sccr.8bit+sccr.enb	 ; rx 8 bit enabled	    3
	dc.b	scc_wtctl,scct.8bit+scct.enb	 ; tx 8 bit enabled	    5
	dc.b	scc_ictl,scci.icts		 ; interrupt on cts	   15
	dc.b	sccc.rint,sccc.rint		 ; reset Ext /Status	  0/0
	dc.b	scc_wint,scci.eint+scci.etint+scci.erine ; rx tx interrupts 1
	dc.b	scc_wmictl,scci.vis+scci.mie	 ; vector and interrupt     9
;	 dc.b	 sccc.rerr,sccc.rhiu		  ; clear sc / iu	     0
	dc.w	0

	vec	sers_scc		 ; set SCC
	dc.w	scc_ctrb		 ; channel B

	dc.b	scc_wmode,sccm.cl16+sccm.lstp	 ; 1.5 stop bits	    4
;	 dc.b	 scc_wint,0;			  ; no rx tx interrupts      1
	dc.b	scc_wrctl,sccr.8bit		 ; rx 8 bit disabled	    3
	dc.b	scc_wtctl,scct.8bit		 ; tx 8 bit enabled	    5
	dc.b	scc_wmictl,scci.vis		 ; vector and no interrupt  9
	dc.b	scc_wclk,sccc.tcbd+sccc.rcbd	 ; clocks from baud gen    11
	dc.b	scc_divl,24			 ; 9600 		   12
	dc.b	scc_divh,0			 ;			   13
	dc.b	scc_wgctl,sccg.csrc		 ; no baud rate gen	   14
	dc.b	scc_wgctl,sccg.ebdg+sccg.csrc	 ; enable baud rate gen    14
	dc.b	scc_wrctl,sccr.8bit+sccr.enb	 ; rx 8 bit enabled	    3
	dc.b	scc_wtctl,scct.8bit+scct.enb	 ; tx 8 bit enabled	    5
	dc.b	scc_ictl,scci.icts		 ; interrupt on cts	   15
	dc.b	sccc.rint,sccc.rint		 ; reset Ext /Status	  0/0
	dc.b	scc_wint,scci.eint+scci.etint+scci.erine ; rx tx interrupts 1
	dc.b	scc_wmictl,scci.vis+scci.mie	 ; vector and interrupt     9
;	 dc.b	 sccc.rerr,sccc.rhiu		  ; clear sc / iu	     0
	dc.w	0

ser_hwst
	vec	sers_mfp		 ; set MFP
	dc.w	at_mfp
	dc.b	mfp_uctl,mfp.npty+mfp.lstp+mfp.8bit+mfp.adiv; set usart
					 ; no parity, 1.5 stop bits, 8 bit async
	dc.b	mfp_rstt,0		 ; receiver disabled
	dc.b	mfp_tstt,0		 ; transmitter disabled
	dc.b	mfp_rstt,mfp.rxen	 ; receiver enabled
	dc.b	mfp_tstt,mfp.txen	 ; transmitter enabled
	dc.b	0

	dc.b	mfp_iena,1<<mfp..rxi+1<<mfp..txi ; enable interrupts
	dc.b	mfp_ienb,1<<mfp..csi		 ; including CTS
	dc.b	mfp_mska,1<<mfp..rxi+1<<mfp..txi ; unmask interrupts
	dc.b	0

	novec

; tables for tt

ser_tabtt
	vec	ser_vectt  ; vector interrupt table
	vec	ser_settt  ; set linkage constants table
	vec	ser_addtt  ; set linkage offsets table
	vec	ser_hwtt   ; hardware table

; tables for ste

ser_tabste
	vec	ser_vecste  ; vector interrupt table
	vec	ser_setste  ; set linkage constants table
	vec	ser_addste  ; set linkage offsets table
	vec	ser_hwste   ; hardware table

; tables for st

ser_tabst
	vec	ser_vecst  ; vector interrupt table
	vec	ser_setst  ; set linkage constants table
	vec	ser_addst  ; set linkage offsets table
	vec	ser_hwst   ; hardware table

	end
