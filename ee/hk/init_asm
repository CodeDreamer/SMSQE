; Initialise the HOTKEY system	V2.02	  1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hk_init
	xdef	hk_name

	xref.l	hk_vers
	xref.l	th_vers

	xref	hk_thuse
	xref	hk_thfre
	xref	hk_threm

	xref	th_entry
	xref	th_exec
	xref	gu_achpp

	xref	hk_stufl
	xref	hk_stufc
	xref	hk_stufp 
	xref	hktx_llrc
	xref	met_hks2

	include 'dev8_keys_sys'
	include 'dev8_keys_k'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_sms'
	include 'dev8_ee_hk_data'
	include 'dev8_mac_assert'

	section version
hk_vmess
	dc.w	'HOTKEY System II V'
	dc.l	hk_vers
	dc.b	' ',$a
	dc.w	'THING System V'
	dc.l	th_vers
	dc.b	' ',$a
hk_vmend

	section hotkey

hk_name dc.w	6,'HOTKEY'

alt_key 
	move.l	sys_ckyq(a6),a2 ; code to check
	move.l	a3,a4
	move.l	$14(a4),d0
alt_keye

hki_tab
	dc.w	hkd_plad	; first address to fill in

	xref	hk_poll
	dc.w	hk_poll-*
	dc.w	-1
	xref	hk_fitem
	dc.w	hk_fitem-*
	xref	hk_cjob
	dc.w	hk_cjob-*
	xref	hk_kjob
	dc.w	hk_kjob-*
	xref	hk_set
	dc.w	hk_set-*
	xref	hk_remv
	dc.w	hk_remv-*
	xref	hk_do
	dc.w	hk_do-*
	xref	hk_sstbf
	dc.w	hk_sstbf-*
	xref	hk_getbf
	dc.w	hk_getbf-*
	xref	hk_guard
	dc.w	hk_guard-*
	dc.w	0
;+++
; Initialise HOTKEY thing and polling server
;
;	d1-d7/a0-a2/a4/a5 smashed
;	a3  r	address of hotkey thing (polling linkage block)
;	status return 0 ok
;	status return -ve standard
;	status return +ve, d0=0, linkage already exists
;---
hk_init
	trap	#0
	moveq	#sms.info,d0		 ; get system information
	trap	#1
	move.l	a0,a3			 ; save it
	lea	sys_poll(a0),a0 	 ; polling list
	lea	hk_poll(pc),a5		 ; ... our polling routine
hki_chk
	move.l	a0,a1
hki_chk1
	move.l	(a0),d0 		 ; last link?
	beq.s	hki_allc		 ; ... yes
	move.l	d0,a0			 ; no
	move.l	hkd_plad-hkd_pllk(a0),a0
	lea	alt_key,a2		 ; check if ALTKEY
	cmp.l	(a2)+,(a0)+
	bne.s	hki_us			 ; ... no
	cmp.l	(a2)+,(a0)+
	bne.s	hki_us			 ; ... no
	cmp.w	(a2)+,(a0)+
	bne.s	hki_us			 ; ... no
	move.l	d0,a0			 ; ... yes
	move.l	(a0),(a1)		 ; unlink
	bra.s	hki_chk1
hki_us
	move.l	d0,a0
	cmp.l	hkd_plad-hkd_pllk(a0),a5 ; our polling routine?
	bne.s	hki_chk 		 ; ... no
	bra.l	hki_nop 		 ; ... yes, do not bother any more

hki_allc
	tst.l	sys_thgl(a3)		 ; thing already present?
	bne.s	hki_aldm		 ; ... yes,
	moveq	#hkd.thg+$18,d0 	 ; allocate thing itself
	jsr	gu_achpp
	bne.l	hki_exit
	move.l	a0,sys_thgl(a3) 	 ; point to it
	lea	hkd.thg(a0),a2
	move.l	a2,th_thing(a0) 	 ; point to vector
	lea	th_verid(a0),a0
	move.l	#th_vers,(a0)+		 ; set version
	move.w	#5,(a0)+		 ; and name
	move.l	#'THIN',(a0)+
	move.b	#'G',(a0)+

	move.l	#'THG%',(a2)+		 ; ID
	subq.l	#1,(a2)+		 ; type
	lea	th_entry,a0
	move.l	a0,(a2)+		 ; th_entry
	lea	th_exec,a0
	move.l	a0,(a2)+		 ; th_exec

hki_aldm
	moveq	#hkd.dmy,d0		 ; allocate dummy linkage
	jsr	gu_achpp
	bne.l	hki_exit 
	move.l	a0,a4			 ; save base of dummy

	move.l	#hkd.len,d0		 ; allocate link block
	jsr	gu_achpp
	bne.l	hki_exit

	lea	hkd.thg(a0),a3		 ; real linkage block
	move.l	#thh.flag,thh_flag(a3)	 ; flagged
   assert  th_thing,th_use-4,th_free-8,th_remov-$10,th_verid-$16,th_name-$1a
	lea	th_thing(a0),a2
	move.l	a3,(a2)+		 ; thing pointer
	lea	hk_thuse,a1		 ; use code
	move.l	a1,(a2)+
	lea	hk_thfre,a1
	move.l	a1,(a2)+
	move.l	a1,(a2)+
	lea	hk_threm,a1		 ; remove code
	move.l	a1,(a2)+
	clr.w	(a2)+			 ; shareable ha ha
	move.l	#hk_vers,(a2)+		 ; version
	lea	hk_name,a1		 ; and name
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+

	move.l	a0,a1			 ; thing address
	moveq	#sms.lthg,d0		 ; link in
	jsr	th_entry
	bne.l	hki_exit

	moveq	#sms.info,d0		 ; get sysvar base again
	trap	#do.sms2

	lea	sys_poll(a0),a0 	 ; address of first polling link
hki_loop
	move.l	a0,a1			 ; save link
	move.l	(a0),d0
	beq.s	hki_sadd		 ; end of list, link in us
	move.l	d0,a0			 ; next address
	swap	d0			 ; in ROM
	tst.w	d0
	bne.s	hki_loop

	move.l	(a0)+,(a4)+		 ; transfer link
	move.l	(a0)+,(a4)+
	subq.l	#8,a4
	move.l	a4,(a1) 		 ; and set previous link
	move.l	a1,a0
	bra.s	hki_loop		 ; carry on

hki_sadd
	lea	hki_tab,a2		 ; set addresses
	move.l	a3,a4
	add.w	(a2)+,a4
hki_sloop
	move.w	(a2)+,d0		 ; next to set
	beq.s	hki_link		 ; all done
	lea	-2(a2,d0.w),a1		 ; actual address
	move.l	a1,(a4)+		 ; ... set it
	bra.s	hki_sloop

hki_link
	lea	hkd_pllk(a3),a4 	 ; link our polling driver into end
	move.l	a4,(a0)

	move.l	#hkd.id,hkd_id(a3)	 ; set ID
	move.w	#hkd.sbfl,hkd_sbfl(a3)	 ; set buffer length

	lea	hkd_llrc(a3),a4 	 ; set last line recall
	move.l	a4,hkd_ptrb(a3)
	move.w	#hki.id,(a4)+
	move.w	#hki.llrc,(a4)+ 	 ; hotkey item
	clr.l	(a4)+			 ; pointer
	jsr	hktx_llrc		 ; get name
	move.w	(a1)+,d2
	move.w	d2,(a4)+
hki_sllrc
	move.w	(a1)+,(a4)+		 ; copy characters of last line recall
	subq.w	#2,d2
	bgt.s	hki_sllrc

	move.l	hk_stufl,d1		 ; allocate hotkey buffer
	move.l	d1,d0
	jsr	gu_achpp
	bne.s	hki_exit
	lea	hkd_bbas(a3),a4 	 ; set stuffer

 assert  hkd_bbas,hkd_btop-4,hkd_biid-$8,hkd_btyp-$a,hkd_bpnt-$c,hkd_bnam-$10
	move.l	a0,(a4)+		 ; set base
	add.l	a0,d1
	subq.l	#1,d1			 ; top inclusive
	move.l	d1,(a4)+		 ; and top

	move.l	a4,hkd_ptrb+4(a3)	 ; point hotkey 2 to ID
	move.w	#hki.id,(a4)+		 ; ID
	move.w	#hki.stbf,(a4)+ 	 ; type
	move.l	d1,(a4)+		 ; pointer
	clr.w	(a4)+			 ; and name

	move.l	a4,hkd_ptrb+8(a3)	 ; point hotkey 3 to previous ID
	move.w	#hki.id,(a4)+		 ; ID
	move.w	#hki.stpr,(a4)+ 	 ; type
	move.l	d1,(a4)+		 ; pointer
	clr.w	(a4)+			 ; and name

	lea	hkd_tabl(a3),a4 	 ; hotkey table
	move.b	#1,k.nl(a4)		 ; ALT ENTER

	moveq	#0,d1
	move.b	hk_stufc,d1		 ; Stuffer
	move.b	#2,(a4,d1.w)
	move.b	hk_stufp,d1		 ; previous stuffer
	move.b	#3,(a4,d1.w)
	moveq	#0,d0
hki_exit
	and.w	#$dfff,sr
	move.l	d0,d4
	beq.s	hki_rts
	lea	met_hks2,a1		  ; write error message
	sub.l	a0,a0
	move.w	ut.wtext,a2
	jsr	(a2)
	move.l	d4,d0
	move.w	ut.werms,a2
	jmp	(a2)


hki_nop
	moveq	#0,d0			 ; d0 = 0, OK
	moveq	#1,d1			 ; status positive
	bra.s	hki_exit
hki_nimp
	moveq	#err.nimp,d0
hki_rts
	rts
	end
