; (Super)GoldCard patch code vectors

	section patch
	xdef	gl_proc
	xdef	gl_hires

patch	macro	address
	xref	[address]
	dc.w	[address]-*
	endm

patchx	macro
	dc.w	*-base
	endm

base
	dc.l	$1c000			hardware base		$00
	dc.w	0			assume 68020 timing
gl_proc dc.b	0			and 68000 processor
gl_hires dc.b	0			highres graphics?
	patch	gl_mdvtm		Microdrive routines
	patch	gl_mdvrd
	patch	gl_mdvwr
	patch	gl_mdvvr

	patch	gl_mdvsh					$10
	patch	gl_mdvf
	patch	gl_mdvsl
	patch	gl_mdvds
	dc.w	0			clock
	patch	gl_extr 		extra rom code
	patch	sb_chan
	patch	gl_graph

	patch	gl_ext_rom		init ROMs in ext. I/O	$20
	patch	sgc_privv
	patch	I2C_entry		I2C
	patch	I2C_raw
	patch	ser_xdly		transmit delay
	patch	sgc_8049_rte
	patch	sgc_ser_sched
	patch	sgc_qdos_trp0

	patch	gl_init 		card initialisation	$30
	patch	sgc_trp3
	patch	sgc_trap1
	patch	sgc_trp2
	patch	sgc_min_sched
	patch	sgc_int2_rte
	patch	sgc_qdos_sched
	patch	sgc_boot_sched

	patch	sgc_i2						$40
	patch	sgc_aerr
	patch	sgc_div0
	patch	sgc_chk
	patch	sgc_trpv
	patch	sgc_trac
	patch	sgc_min_trp0
	patch	sgc_p4e

	patch	sgc_trp4					$50
	patch	sgc_i2c_entry
	patch	sgc_i2c_raw
	patch	gl_min_reset3
	patch	sgc_alin
	patch	sgc_flin
	patch	gl_trap1
	dc.w	0

	patch	sgc_ilin					$60
	patch	sgc_trp5
	patch	sgc_trp6
	patch	sgc_trp7
	patch	sgc_trp8
	patch	sgc_trp9
	patch	sgc_trpa
	patch	sgc_trpb
	patch	sgc_trpc					$70
	patch	sgc_trpd
	patch	sgc_trpe
	patch	sgc_trpf

	patch	sgc_nds_bytes
	patch	nds_bytes	; will be patched to sgc_nds_bytes for SGC
	patch	sgc_ndr_1byte
	patch	ndr_1byte	; will be patched to sgc_ndr_1byte for SGC
	patch	sgc_nds_sscout					$80
	patch	nds_sscout	; will be patched to sgc_nds_sscout for SGC

	patch	gl_f1f2
	patch	gl_bvchnt
	patch	glm_cls
	patch	glm_cls2
	patch	glm_ptr_gen
	dc.w	0

	dc.w	0						$90
	dc.w	0
	patch	glm_cls3
	patch	glm_cls4

	xdef	err_bp

err_bp
	moveq	#-15,d0
	rts

	end
